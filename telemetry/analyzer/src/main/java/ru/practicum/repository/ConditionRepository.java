package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Condition;
import ru.practicum.model.Scenario;

import java.util.List;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {

    List<Condition> findAllByScenario(Scenario scenario);

    @Query("SELECT c FROM Condition c JOIN FETCH c.sensor WHERE c.scenario.hubId = :hubId")
    List<Condition> findAllByHubId(@Param("hubId") String hubId);

    void deleteByScenario(Scenario scenario);

}