package bytron.mipueblo.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

//We don't need the @Data annotation because we are using @Getter and @Setter
//@Data
@Getter
@Setter
public class LoginForm {
    @NotEmpty(message = "Correo no puede ser vacio")
    @Email(message = "Correo Invalido")
    private String email;
    @NotEmpty(message = "Contrasena no puede ser vacia")
    private String password;

}
