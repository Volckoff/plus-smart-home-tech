package ru.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "conditions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    ConditionTypeAvro type;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation")
    ConditionOperationAvro operation;

    @Column(name = "condition_value")
    Integer value;

    @ElementCollection
    @CollectionTable(name = "scenario_conditions", 
                    joinColumns = @JoinColumn(name = "condition_id"))
    @MapKeyJoinColumn(name = "scenario_id")
    @Column(name = "sensor_id")
    Map<Scenario, String> scenarioSensorMap;
}