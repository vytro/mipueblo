package bytron.mipueblo.enumeration;

import lombok.Getter;

@Getter
public enum EventType {
    LOGIN_ATTEMPT("Intento de Inicio de Sesion"),
    LOGIN_ATTEMPT_FAILURE("Fallo de Inicio de Sesion"),
    LOGIN_ATTEMPT_SUCCESS("Inicio de Sesion Exitoso"),
    PROFILE_UPDATE("Actualizacion de Perfil"),
    PROFILE_PICTURE_UPDATE("Actualizacion de Foto de Perfil"),
    ROLE_UPDATE("Actualizacion de Rol"),
    ACCOUNT_SETTINGS_UPDATE("Actualizacion de Configuracion de Cuenta"),
    PASSWORD_UPDATE("Actualizacion de Contraseña"),
    MFA_UPDATE("Actualizacion de MFA");

    private final String description;

    EventType(String description) {
        this.description = description;
    }

}
