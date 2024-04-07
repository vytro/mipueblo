package bytron.mipueblo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class UserEvent {

    private Long id;
//    from Events table
    private String type;
    private String description;
//    from UserEvents table
    private String device;
    private String ipAddress;
    private LocalDateTime createdAt;

}
