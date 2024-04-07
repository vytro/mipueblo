package bytron.mipueblo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)

public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String phone;
    private String title;
    private String bio;
    private String imageUrl;
    //if the account is active or inactive
    private boolean enabled;
    private boolean isNotLocked;
    //if user is using multifactor authentication
    private boolean isUsingMfa;
    private LocalDateTime createdAt;

}
