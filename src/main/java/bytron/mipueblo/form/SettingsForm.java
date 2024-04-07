package bytron.mipueblo.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettingsForm {

    @NotNull(message = "Activado no puede ser vacio o nulo")
    private Boolean enabled;
    @NotNull(message = "No Bloqueado no puede ser vacio o nulo")
    private Boolean notLocked;

}
