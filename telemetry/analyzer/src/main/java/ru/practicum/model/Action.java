package ru.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "actions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    ActionTypeAvro type;

    @Column(name = "action_value")
    Integer value;

    @ElementCollection
    @CollectionTable(name = "scenario_actions", 
                    joinColumns = @JoinColumn(name = "action_id"))
    @MapKeyJoinColumn(name = "scenario_id")
    @Column(name = "sensor_id")
    Map<Scenario, String> scenarioSensorMap;
}