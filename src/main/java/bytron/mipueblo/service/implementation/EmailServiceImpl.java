package bytron.mipueblo.service.implementation;

import bytron.mipueblo.enumeration.VerificationType;
import bytron.mipueblo.exception.ApiException;
import bytron.mipueblo.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    @Override
    public void sendVerificationEmail(String firstName, String email, String verificationUrl, VerificationType verificationType) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("insert");
            message.setTo(email);
            message.setText(getEmailMessage(firstName, verificationUrl, verificationType));
//            message.setSubject(String.format("MiPueblo - %s Verification", StringUtils.capitalize(verificationType.getType())));
            message.setSubject("MiPueblo - Verificacion");
            mailSender.send(message);
            log.info("Email sent to: " + firstName);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private String getEmailMessage(String firstName, String verificationUrl, VerificationType verificationType) {
        switch(verificationType){
            case PASSWORD -> {return "Hola " + firstName + "\n\n" +
                    "Hemos recibido una solicitud para restablecer la contraseña de tu cuenta. Haz clic en el siguiente enlace para restablecer tu contraseña:\n" +
                    verificationUrl + "\n\n" +
                    "Si no solicitaste restablecer tu contraseña, ignora este mensaje.\n\n" +
                    "Gracias,\n" +
                    "El equipo de MiPueblo";}
            case ACCOUNT -> {return "Hola " + firstName + "\n\n" +
                    "Tu nueva cuenta ha sido creada. Haz clic en el siguiente enlace para verificar su cuenta:\n" +
                    verificationUrl + "\n\n" +
                    "Si no creaste una nueva cuenta, ignora este mensaje.\n\n" +
                    "Gracias,\n" +
                    "El equipo de MiPueblo";}
            default -> throw new ApiException("No se pudo enviar el correo de verificación. Tipo de verificación no válido.");
        }
    }
}
