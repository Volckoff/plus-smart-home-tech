package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Action;
import ru.practicum.model.Scenario;

import java.util.List;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

    List<Action> findAllByScenario(Scenario scenario);

    @Query("SELECT a FROM Action a JOIN FETCH a.sensor WHERE a.scenario.hubId = :hubId")
    List<Action> findAllByHubId(@Param("hubId") String hubId);

    void deleteByScenario(Scenario scenario);

}