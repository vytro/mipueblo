package bytron.mipueblo.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordForm {

    @NotEmpty(message = "Contrasena actual no puede ser vacio")
    private String currentPassword;
    @NotEmpty(message = "Nueva Contrasena no puede ser vacio")
    private String newPassword;
    @NotEmpty(message = "Contrasena confirmada no puede ser vacio")
    private String confirmNewPassword;

}
