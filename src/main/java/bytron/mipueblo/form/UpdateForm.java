package bytron.mipueblo.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateForm {

    @NotNull(message = "ID no puede ser vacio o nulo")
    private Long id;
    @NotEmpty(message = "Nombre no puede ser vacio")
    private String firstName;
    @NotEmpty(message = "Apellido no puede ser vacio")
    private String lastName;
    @NotEmpty(message = "Correo no puede ser vacio")
    @Email(message = "Correo Invalido")
//    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Correo Invalido")
    private String email;
    @Pattern(regexp = "^\\d{11}$", message = "Telefono Invalido") //59177903057  17037316232
    private String phone;
    private String address;
    private String title;
    private String bio;
}
