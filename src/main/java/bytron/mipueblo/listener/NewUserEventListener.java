package bytron.mipueblo.listener;

import bytron.mipueblo.event.NewUserEvent;
import bytron.mipueblo.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static bytron.mipueblo.utils.RequestUtils.getDevice;
import static bytron.mipueblo.utils.RequestUtils.getIpAddress;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewUserEventListener {
    private final EventService eventService;
    private final HttpServletRequest request;

    @EventListener
    public void onNewUserEvent(NewUserEvent event) {
//        log.info("NewUserEvent is fired: " + event.getEmail() + " " + event.getType() + " " + request.getRemoteAddr());
        eventService.addUserEvent(event.getEmail(), event.getType(), getDevice(request), getIpAddress(request));
    }
}
