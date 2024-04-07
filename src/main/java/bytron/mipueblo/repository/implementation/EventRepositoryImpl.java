package bytron.mipueblo.repository.implementation;

import bytron.mipueblo.domain.UserEvent;
import bytron.mipueblo.dto.UserDTO;
import bytron.mipueblo.enumeration.EventType;
import bytron.mipueblo.exception.ApiException;
import bytron.mipueblo.repository.EventRepository;
import bytron.mipueblo.rowmapper.UserEventRowMapper;
import bytron.mipueblo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

import static bytron.mipueblo.query.EventQuery.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EventRepositoryImpl implements EventRepository {
    private final NamedParameterJdbcTemplate jdbc;
    private final UserService userService;
    @Override
    public Collection<UserEvent> getEventByUserId(Long userId) {
        return jdbc.query(SELECT_EVENTS_BY_USER_ID_QUERY, Map.of("id", userId), new UserEventRowMapper());
    }

//    @Override
//    public void addUserEvent(String email, EventType eventType, String device, String ipAddress) {
//        jdbc.update(INSERT_EVENT_BY_USER_EMAIL_QUERY, Map.of("email", email, "type", eventType.toString(), "device", device, "ipAddress", ipAddress));
//    }

    @Override
    public void addUserEvent(String email, EventType eventType, String device, String ipAddress) {
//        log.info("EventRepositoryImpl: "+email + " " + eventType + " " + device + " " + ipAddress);

        //og works
//        jdbc.update(INSERT_EVENT_BY_USER_EMAIL_QUERY, Map.of("email", email, "type", eventType.toString(), "device", device, "ipAddress", ipAddress));
        UserDTO userDTO = userService.getUserByEmail(email);
        if (userDTO != null) {
            jdbc.update(INSERT_EVENT_BY_USER_EMAIL_QUERY, Map.of("email", email, "type", eventType.toString(), "device", device, "ipAddress", ipAddress));
        } else {
            throw new ApiException("User with email " + email + " does not exist.");
        }
    }

    @Override
    public void addUserEvent(Long userId, EventType eventType, String device, String ipAddress) {

    }
}
