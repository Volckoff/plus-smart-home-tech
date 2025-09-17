package ru.practicum.sevices;

import ru.practicum.event.hub.HubEvent;
import ru.practicum.event.sensor.SensorEvent;

public interface EventService {

    void publishSensorEvent(SensorEvent event);

    void publishHubEvent(HubEvent event);

}
