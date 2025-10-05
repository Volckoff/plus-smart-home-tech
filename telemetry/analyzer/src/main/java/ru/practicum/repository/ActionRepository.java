package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Action;
import ru.practicum.model.Scenario;

import java.util.List;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

    @Query("SELECT a FROM Action a WHERE KEY(a.scenarioSensorMap) = :scenario")
    List<Action> findAllByScenario(@Param("scenario") Scenario scenario);

    @Query("SELECT a FROM Action a WHERE KEY(a.scenarioSensorMap).hubId = :hubId")
    List<Action> findAllByHubId(@Param("hubId") String hubId);

    @Modifying
    @Query(value = "DELETE FROM scenario_actions WHERE scenario_id = :scenarioId", nativeQuery = true)
    void deleteByScenario(@Param("scenarioId") Long scenarioId);

}