package bytron.mipueblo.service.implementation;

import bytron.mipueblo.domain.Role;
import bytron.mipueblo.domain.User;
import bytron.mipueblo.dto.UserDTO;
import bytron.mipueblo.dtomapper.UserDTOMapper;
import bytron.mipueblo.form.UpdateForm;
import bytron.mipueblo.repository.RoleRepository;
import bytron.mipueblo.repository.UserRepository;
import bytron.mipueblo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository<User> userRepository;

    private final RoleRepository<Role> roleRoleRepository;

    @Override
    public UserDTO createUser(User user) {
//        return UserDTOMapper.fromUser(userRepository.create(user));
        return mapToUserDTO(userRepository.create(user));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
//        return UserDTOMapper.fromUser(userRepository.getUserByEmail(email));
        return mapToUserDTO(userRepository.getUserByEmail(email));
    }

    @Override
    public void sendVerificationCode(UserDTO user) {
        userRepository.sendVerificationCode(user);
    }

    //shouldn't be dealing with the user in Service layer
//    @Override
//    public User getUser(String email) {
//        return userRepository.getUserByEmail(email);
//    }

    @Override
    public UserDTO verifyCode(String email, String code) {
//        return UserDTOMapper.fromUser(userRepository.verifyCode(email, code));
        return mapToUserDTO(userRepository.verifyCode(email, code));
    }

    @Override
    public void resetPassword(String email) {
        userRepository.resetPassword(email);
    }

    @Override
    public UserDTO verifyPasswordKey(String key) {
        return mapToUserDTO(userRepository.verifyPasswordKey(key));
    }

    @Override
    public void updatePassword(Long userId, String password, String confirmPassword) {
        userRepository.renewPassword(userId, password, confirmPassword);
    }

//    @Override
//    public void renewPassword(String key, String password, String confirmPassword) {
//        userRepository.renewPassword(key, password, confirmPassword);
//    }

    @Override
    public UserDTO verifyAccountKey(String key) {
        return mapToUserDTO(userRepository.verifyAccountKey(key));
    }

    @Override
    public UserDTO updateUserDetails(UpdateForm user) {
        return mapToUserDTO(userRepository.updateUserDetails(user));
    }

    @Override
    public UserDTO getUserById(Long userId) {
        return mapToUserDTO(userRepository.get(userId));
    }

    @Override
    public void updatePassword(Long id, String currentPassword, String newPassword, String confirmNewPassword) {
        userRepository.updatePassword(id, currentPassword, newPassword, confirmNewPassword);
    }

    @Override
    public void updateUserRole(Long userId, String roleName) {
        roleRoleRepository.updateUserRole(userId, roleName);
    }

    @Override
    public void updateAccountSettings(Long userId, Boolean enabled, Boolean notLocked) {
        userRepository.updateAccountSettings(userId, enabled, notLocked);
    }

    @Override
    public UserDTO toggleMfa(String email) {
        return mapToUserDTO(userRepository.toggleMfa(email));
    }

    @Override
    public void updateImage(UserDTO userDTO, MultipartFile image) {
        userRepository.updateImage(userDTO, image);
    }

    private UserDTO mapToUserDTO(User user){
        return UserDTOMapper.fromUser(user, roleRoleRepository.getRoleByUserId(user.getId()));
    }
}
