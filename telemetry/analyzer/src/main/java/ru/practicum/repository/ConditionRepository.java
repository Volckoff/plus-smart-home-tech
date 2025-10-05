package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Condition;
import ru.practicum.model.Scenario;

import java.util.List;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {

    @Query("SELECT c FROM Condition c WHERE KEY(c.scenarioSensorMap) = :scenario")
    List<Condition> findAllByScenario(@Param("scenario") Scenario scenario);

    @Query("SELECT c FROM Condition c WHERE KEY(c.scenarioSensorMap).hubId = :hubId")
    List<Condition> findAllByHubId(@Param("hubId") String hubId);

    @Modifying
    @Query(value = "DELETE FROM scenario_conditions WHERE scenario_id = :scenarioId", nativeQuery = true)
    void deleteByScenario(@Param("scenarioId") Long scenarioId);

}