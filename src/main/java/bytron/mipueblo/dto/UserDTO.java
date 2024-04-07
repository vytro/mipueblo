package bytron.mipueblo.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
//    private String password;
    private String address;
    private String phone;
    private String title;
    private String bio;
    private String imageUrl;
    private boolean enabled;
    private boolean isNotLocked;
    //to make sure it's not usingMfa
//    @JsonProperty("isUsingMfa")
    private boolean isUsingMfa;
    private LocalDateTime createdAt;

    private String roleName;
    private String permissions;

}
