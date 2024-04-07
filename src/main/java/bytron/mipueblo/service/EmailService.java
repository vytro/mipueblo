package bytron.mipueblo.service;

import bytron.mipueblo.enumeration.VerificationType;

public interface EmailService {
    void sendVerificationEmail(String firstName, String email, String verificationUrl, VerificationType verificationType);

}
