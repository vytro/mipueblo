package bytron.mipueblo.service;

import bytron.mipueblo.domain.User;
import bytron.mipueblo.dto.UserDTO;
import bytron.mipueblo.form.UpdateForm;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
//    void createUser(User user);

    UserDTO createUser(User user);
    UserDTO getUserByEmail(String email);
    void sendVerificationCode(UserDTO user);

//    User getUser(String email);

    UserDTO verifyCode(String email, String code);

    void resetPassword(String email);

    UserDTO verifyPasswordKey(String key);

    void updatePassword(Long userId, String password, String confirmPassword);

    UserDTO verifyAccountKey(String key);

    UserDTO updateUserDetails(UpdateForm user);

    UserDTO getUserById(Long userId);

    void updatePassword(Long userId, String currentPassword, String newPassword, String confirmNewPassword);

    void updateUserRole(Long userId, String roleName);

    void updateAccountSettings(Long userId, Boolean enabled, Boolean notLocked);

    UserDTO toggleMfa(String email);

    void updateImage(UserDTO userDTO, MultipartFile image);

}
