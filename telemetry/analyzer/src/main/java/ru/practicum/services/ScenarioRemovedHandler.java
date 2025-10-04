package ru.practicum.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Scenario;
import ru.practicum.repository.ActionRepository;
import ru.practicum.repository.ConditionRepository;
import ru.practicum.repository.ScenarioRepository;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScenarioRemovedHandler implements HubEventHandler {

    private final ScenarioRepository scenarioRepository;
    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;

    @Override
    @Transactional
    public void handle(HubEventAvro hubEventAvro) {
        ScenarioRemovedEventAvro scenarioRemovedEvent = (ScenarioRemovedEventAvro) hubEventAvro.getPayload();
        Optional<Scenario> scenario = scenarioRepository.findByHubIdAndName(hubEventAvro.getHubId(), scenarioRemovedEvent.getName());
        if (scenario.isPresent()) {
            actionRepository.deleteByScenario(scenario.get());
            conditionRepository.deleteByScenario(scenario.get());
            scenarioRepository.delete(scenario.get());
        }
    }

    @Override
    public String getTypeOfPayload() {
        return ScenarioRemovedEventAvro.class.getSimpleName();
    }
}
