package bytron.mipueblo.service.implementation;

import bytron.mipueblo.domain.UserEvent;
import bytron.mipueblo.enumeration.EventType;
import bytron.mipueblo.repository.EventRepository;
import bytron.mipueblo.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    @Override
    public Collection<UserEvent> getEventByUserId(Long userId) {
        return eventRepository.getEventByUserId(userId);
    }

    @Override
    public void addUserEvent(String email, EventType eventType, String device, String ipAddress) {
        eventRepository.addUserEvent(email, eventType, device, ipAddress);
    }

    @Override
    public void addUserEvent(Long userId, EventType eventType, String device, String ipAddress) {

    }
}
