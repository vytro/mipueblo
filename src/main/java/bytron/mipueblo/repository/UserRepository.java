package bytron.mipueblo.repository;

import bytron.mipueblo.domain.User;
import bytron.mipueblo.dto.UserDTO;
import bytron.mipueblo.form.UpdateForm;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

public interface UserRepository<T extends User> {
    /* Basic CRUD operations */
    T create(T data);

    //read a lot
    Collection<T> list(int page, int pageSize);

    //read just 1
    T get(Long id);

    T update(T data);
    Boolean delete(Long id);

    /* More complex operations */

    User getUserByEmail(String email);

    void sendVerificationCode(UserDTO user);

    User verifyCode(String email, String code);

    void resetPassword(String email);

    T verifyPasswordKey(String key);

    void renewPassword(String key, String password, String confirmPassword);
    void renewPassword(Long userId, String password, String confirmPassword);

    T verifyAccountKey(String key);

    T updateUserDetails(UpdateForm user);

    void updatePassword(Long id, String currentPassword, String newPassword, String confirmNewPassword);

    void updateAccountSettings(Long userId, Boolean enabled, Boolean notLocked);

    User toggleMfa(String email);

    void updateImage(UserDTO userDTO, MultipartFile image);

}
