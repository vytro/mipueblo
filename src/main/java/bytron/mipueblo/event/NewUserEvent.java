package bytron.mipueblo.event;

import bytron.mipueblo.enumeration.EventType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class NewUserEvent extends ApplicationEvent {

    private String email;
    private EventType type;


    public NewUserEvent(String email, EventType type) {
        super(email);
        this.email = email;
        this.type = type;
    }

}
