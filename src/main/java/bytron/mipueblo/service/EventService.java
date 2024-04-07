package bytron.mipueblo.service;

import bytron.mipueblo.domain.UserEvent;
import bytron.mipueblo.enumeration.EventType;

import java.util.Collection;

public interface EventService {
    Collection<UserEvent> getEventByUserId(Long userId);
    void addUserEvent(String email, EventType eventType, String device, String ipAddress);
    void addUserEvent(Long userId, EventType eventType, String device, String ipAddress);
}
