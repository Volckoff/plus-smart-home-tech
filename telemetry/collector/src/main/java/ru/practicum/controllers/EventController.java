package ru.practicum.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.hub.HubEvent;
import ru.practicum.event.sensor.SensorEvent;
import ru.practicum.sevices.EventService;

@RestController
@Slf4j
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        log.info("Получено событие датчика: {}", event);
        eventService.publishSensorEvent(event);
        log.info("Событие датчика обработано: {}", event);
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @RequestBody HubEvent event) {
        log.info("Получено событие датчика: {}", event);
        eventService.publishHubEvent(event);
        log.info("Событие датчика обработано: {}", event);
    }
}
