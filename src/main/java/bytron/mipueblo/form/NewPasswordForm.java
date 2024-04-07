package bytron.mipueblo.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPasswordForm {
    @NotNull(message = "ID no puede ser vacio o nulo")
    private Long userId;
    @NotEmpty(message = "Contrasena no puede ser vacio")
    private String password;
    @NotEmpty(message = "Contrasena confirmado  no puede ser vacio")
    private String confirmPassword;

}
