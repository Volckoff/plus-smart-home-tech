package ru.practicum.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.grpc.HubRouterClient;
import ru.practicum.model.Action;
import ru.practicum.model.Condition;
import ru.practicum.model.Scenario;
import ru.practicum.repository.ActionRepository;
import ru.practicum.repository.ConditionRepository;
import ru.practicum.repository.ScenarioRepository;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScenarioAnalysisService {

    private final ScenarioRepository scenarioRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;
    private final HubRouterClient hubRouterClient;

    @Transactional(readOnly = true)
    public void analyzeSnapshot(SensorsSnapshotAvro snapshot) {
        String hubId = snapshot.getHubId();
        log.info("Analyzing scenarios for hub: {}", hubId);

        // Получаю все сценарии для хаба
        List<Scenario> scenarios = scenarioRepository.findByHubId(hubId);

        if (scenarios.isEmpty()) {
            log.info("No scenarios found for hub: {}", hubId);
            return;
        }

        List<Condition> allConditions = conditionRepository.findAllByHubId(hubId);
        List<Action> allActions = actionRepository.findAllByHubId(hubId);

        Map<Scenario, List<Condition>> conditionsByScenario = allConditions.stream()
                .flatMap(condition -> condition.getScenarioSensorMap().keySet().stream()
                        .map(scenario -> Map.entry(scenario, condition)))
                .collect(Collectors.groupingBy(Map.Entry::getKey, 
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        Map<Scenario, List<Action>> actionsByScenario = allActions.stream()
                .flatMap(action -> action.getScenarioSensorMap().keySet().stream()
                        .map(scenario -> Map.entry(scenario, action)))
                .collect(Collectors.groupingBy(Map.Entry::getKey, 
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        for (Scenario scenario : scenarios) {
            List<Condition> conditions = conditionsByScenario.getOrDefault(scenario, Collections.emptyList());
            List<Action> actions = actionsByScenario.getOrDefault(scenario, Collections.emptyList());
            analyzeScenario(scenario, conditions, actions, snapshot);
        }
    }

    private void analyzeScenario(Scenario scenario, List<Condition> conditions, List<Action> actions, SensorsSnapshotAvro snapshot) {
        log.info("Analyzing scenario: {} for hub: {}", scenario.getName(), scenario.getHubId());

        log.info("Scenario conditions: {}", conditions.size());
        log.info("Scenario actions: {}", actions.size());

        boolean allConditionsMet = checkAllConditions(conditions, snapshot);
        if (allConditionsMet) {
            log.info("All conditions met for scenario: {}, executing actions", scenario.getName());
            executeActions(actions, scenario.getName(), scenario.getHubId());
        } else {
            log.info("Conditions not met for scenario: {}", scenario.getName());
        }
    }

    private boolean checkAllConditions(List<Condition> conditions, SensorsSnapshotAvro snapshot) {
        if (conditions.isEmpty()) {
            return false;
        }
        for (Condition condition : conditions) {
            if (!checkCondition(condition, snapshot)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkCondition(Condition condition, SensorsSnapshotAvro snapshot) {
        String sensorId = condition.getScenarioSensorMap().values().iterator().next();
        Integer sensorValue = findSensorValue(sensorId, snapshot);

        if (sensorValue == null) {
            log.warn("Sensor {} not found in snapshot", sensorId);
            return false;
        }

        boolean result = switch (condition.getOperation()) {
            case EQUALS -> sensorValue.equals(condition.getValue());
            case GREATER_THAN -> sensorValue > condition.getValue();
            case LOWER_THAN -> sensorValue < condition.getValue();
        };

        log.info("Condition check: sensor {} {} {} = {} (actual: {}, expected: {})",
                sensorId, condition.getOperation(), condition.getValue(), result, sensorValue, condition.getValue());
        return result;
    }

    private Integer findSensorValue(String sensorId, SensorsSnapshotAvro snapshot) {
        Map<String, SensorStateAvro> sensorsState = snapshot.getSensorsState();
        SensorStateAvro sensorState = sensorsState.get(sensorId);
        if (sensorState == null) {
            log.warn("Sensor {} not found in snapshot", sensorId);
            return null;
        }
        return extractSensorValue(sensorState);
    }

    private Integer extractSensorValue(SensorStateAvro sensorState) {
        Object data = sensorState.getData();
        Integer value = null;
        switch (data) {
            case ClimateSensorAvro climateSensor -> {
                value = climateSensor.getTemperatureC();
                log.info("Extracted temperature value: {} from ClimateSensor", value);
            }
            case LightSensorAvro lightSensor -> {
                value = lightSensor.getLuminosity();
                log.info("Extracted luminosity value: {} from LightSensor", value);
            }
            case MotionSensorAvro motionSensor -> {
                value = motionSensor.getMotion() ? 1 : 0;
                log.info("Extracted motion value: {} from MotionSensor", value);
            }
            case SwitchSensorAvro switchSensor -> {
                value = switchSensor.getState() ? 1 : 0;
                log.info("Extracted switch value: {} from SwitchSensor", value);
            }
            default -> log.warn("Unknown sensor data type: {} for sensor",
                    data != null ? data.getClass().getSimpleName() : "null");
        }
        return value;
    }

    private void executeActions(List<Action> actions, String scenarioName, String hubId) {
        for (Action action : actions) {
            String sensorId = action.getScenarioSensorMap().values().iterator().next();
            log.info("Executing action: device={}, type={}, value={}",
                    sensorId, action.getType(), action.getValue());

            hubRouterClient.sendDeviceAction(hubId, scenarioName, action);
        }
    }
}