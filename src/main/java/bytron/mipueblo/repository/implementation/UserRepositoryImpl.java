package bytron.mipueblo.repository.implementation;

import bytron.mipueblo.domain.Role;
import bytron.mipueblo.domain.User;
import bytron.mipueblo.domain.UserPrincipal;
import bytron.mipueblo.dto.UserDTO;
import bytron.mipueblo.enumeration.VerificationType;
import bytron.mipueblo.exception.ApiException;
import bytron.mipueblo.form.UpdateForm;
import bytron.mipueblo.repository.RoleRepository;
import bytron.mipueblo.repository.UserRepository;
import bytron.mipueblo.rowmapper.UserRowMapper;
import bytron.mipueblo.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static bytron.mipueblo.constant.Constants.DATE_FORMAT;
import static bytron.mipueblo.enumeration.RoleType.*;
import static bytron.mipueblo.enumeration.VerificationType.ACCOUNT;
import static bytron.mipueblo.enumeration.VerificationType.PASSWORD;
import static bytron.mipueblo.query.UserQuery.*;
import static bytron.mipueblo.utils.SmsUtils.sendSMS;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Map.of;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.time.DateUtils.addDays;

@Repository
@RequiredArgsConstructor
@Slf4j //logger
public class UserRepositoryImpl implements UserRepository<User>, UserDetailsService {

    //jdbc database template to execute queries in the database
    private final NamedParameterJdbcTemplate jdbc;
    private final RoleRepository<Role> roleRepository;
    private final BCryptPasswordEncoder encoder;

    private final EmailService emailService;

    @Override
    public User create(User user) {
        //check if the email is unique
        if (getEmailCount(user.getEmail().trim().toLowerCase()) > 0)
            throw new ApiException("Correo ya existe. Intente denuevo.");
        //if that's the case then save new user
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbc.update(INSERT_USER_QUERY, parameters, holder);
            user.setId(requireNonNull(holder.getKey()).longValue());
            roleRepository.addRoleToUser(user.getId(), ROLE_USER.name());
            //send verification URL
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
            jdbc.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY, of("userId", user.getId(), "url", verificationUrl));
            //send email to user with verification URL
            sendEmail(user.getFirstName(), user.getEmail(), verificationUrl, ACCOUNT);
            //emailService.sendVerification(user.getFirstName(), user.getEmail(), verificationUrl, ACCOUNT);
            user.setEnabled(false);
            user.setNotLocked(true);
            System.out.println(verificationUrl); //for testing purposes
            //return the newly created user
            return user;
            //if any errors, throw exception with proper message
            //if didn't get anything then return that exception in particular

            //redundant to catch twice here and in RoleRepositoryImpl.java
//        } catch (EmptyResultDataAccessException exception) {
//            throw new ApiException("No se encontro role: " + ROLE_USER.name());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Error, intente de nuevo.");
        }
    }

    private void sendEmail(String firstName, String email, String verificationUrl, VerificationType verificationType) {
        CompletableFuture.runAsync(() -> {
            emailService.sendVerificationEmail(firstName, email, verificationUrl, verificationType);
        });
    }


    @Override
    public Collection<User> list(int page, int pageSize) {
        return null;
    }

    @Override
    public User get(Long id) {
        try {
            return jdbc.queryForObject(SELECT_USER_BY_ID, of("id",id), new UserRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No se encontro usuario por id: " + id);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Error, intente de nuevo.");
        }
    }

    @Override
    public User update(User data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    private Integer getEmailCount(String email) {
        return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, of("email", email), Integer.class);
    }

    private SqlParameterSource getSqlParameterSource(User user) {
        return new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("password", encoder.encode(user.getPassword()));
    }

    //og works
//    private String getVerificationUrl(String key, String type){
//        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/" + type + "/" + key).toUriString();
//    }

    private String getVerificationUrl(String key, String type){
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .scheme("http")
                .host("localhost")
                .port(4200)
                .path("/user/verify/" + type + "/" + key)
                .toUriString();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email);
        if(user == null) {
            log.info("Usuario no encontrado en la base de datos");
            throw new UsernameNotFoundException("Usuario no encontrado en la base de datos");
        }
        else {
            log.info("Usuario encontrado en la base de datos: {}", email);
//            return new UserPrincipal(user, roleRepository.getRoleByUserId(user.getId()).getPermission());
            return new UserPrincipal(user, roleRepository.getRoleByUserId(user.getId()));
//            user.setAuthorities(getUserAuthorities(user.getId()));
//            return user;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        try {

            User user = jdbc.queryForObject(SELECT_USER_BY_EMAIL_QUERY, of("email",email), new UserRowMapper());
            return user;

        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No se encontro usuario por correo: " + email);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Error, intente de nuevo.");
        }
    }

    @Override
    public void sendVerificationCode(UserDTO user) {
        String expirationDate = DateFormatUtils.format(addDays(new Date(), 1), DATE_FORMAT);
//        String verificationCode = UUID.randomUUID().toString();
        String verificationCode = randomAlphabetic(8).toUpperCase();
        //delete existing code if the user already has one or is expired.
        try {

            jdbc.update(DELETE_VERIFICATION_CODE_BY_USER_ID, of("id", user.getId()));
            jdbc.update(INSERT_VERIFICATION_CODE_QUERY, of("userId", user.getId(), "code", verificationCode, "expirationDate", expirationDate));
            //comment this out during development, or change using_mfa from 1 to 0 in the database
            sendSMS(user.getPhone(), "MiPueblo Codigo de Verificacion: " + verificationCode);
            log.info("Codigo de Verficacion: {}", verificationCode);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Error, intente de nuevo.");
        }
    }

    @Override
    public User verifyCode(String email, String code) {
        if (isVerificationCodeExpired(code))
            throw new ApiException("Este codigo esta caducado. Por favor logea nuevamente.");
        try{
            User userByCode = jdbc.queryForObject(SELECT_USER_BY_CODE_QUERY, of("code", code), new UserRowMapper());
            User userByEmail = jdbc.queryForObject(SELECT_USER_BY_EMAIL_QUERY, of("email", email), new UserRowMapper());
            if(userByCode.getEmail().equalsIgnoreCase(userByEmail.getEmail())){
                jdbc.update(DELETE_CODE, of("code", code));
//                jdbc.update(UPDATE_USER_VERIFIED_QUERY, of("id", userByEmail.getId()));
                return userByCode;
            }else{
                throw new ApiException("Codigo de verificacion invalido. Intente de nuevo.");
            }
        }catch(EmptyResultDataAccessException exception){
            throw new ApiException("No se pudo encontrar un record.");
        }catch(Exception exception){
            throw new ApiException("Error, intente de nuevo.");
        }


//        return null;
    }

    @Override
    public void resetPassword(String email) {
        if(getEmailCount(email.trim().toLowerCase()) <= 0)
            throw new ApiException("No se encontro usuario por correo.");
        try{
            String expirationDate = DateFormatUtils.format(addDays(new Date(), 1), DATE_FORMAT);
            User user = getUserByEmail(email);
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), PASSWORD.getType());
            //delete anything the user had in the application if they had anything
            //uncomment back again, only useful for testing and using the url again
            jdbc.update(DELETE_PASSWORD_VERIFICATION_BY_USER_ID_QUERY, of("userId", user.getId()));
            jdbc.update(INSERT_PASSWORD_VERIFICATION_QUERY, of("userId", user.getId(),
                    "url", verificationUrl, "expirationDate", expirationDate));
            sendEmail(user.getFirstName(), email, verificationUrl, PASSWORD);
            log.info("Verification URL: {}", verificationUrl);
        }catch(Exception exception){
            throw new ApiException("Error, intente de nuevo. URImpl");
        }
    }

    @Override
    public User verifyPasswordKey(String key) {
        if(isLinkExpired(key, PASSWORD))
            throw new ApiException("Este link esta caducado. Por favor logea nuevamente.");
        try{
            String url = getVerificationUrl(key, PASSWORD.getType());
            User user =  jdbc.queryForObject(SELECT_USER_BY_PASSWORD_URL_QUERY, of("url", url), new UserRowMapper());
            //if the user clicks the link once they can reset their password
            //but if they click it again, the link is going to say not valid
            //depends on use case, or business case, rarely used I think
            //jdbc.update(DELETE_PASSWORD_VERIFICATION_BY_USER_ID_QUERY, of("userId", user.getId()));
            return user;
        }catch(EmptyResultDataAccessException exception){
            log.error(exception.getMessage());
            throw new ApiException("Este enlace no es valido.Resetea tu contrasena nuevamente.");
        }catch(Exception exception){
            log.error(exception.getMessage());
            throw new ApiException("Error, intente de nuevo.");
        }
    }

    @Override
    public void renewPassword(String key, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new ApiException("Las contrasenas no coinciden. Intente nuevamente.");
        }
        try {
            String pass = encoder.encode(password);
            String url = getVerificationUrl(key, PASSWORD.getType());
            jdbc.update(UPDATE_USER_PASSWORD_BY_URL_QUERY, of("password", pass, "url", url));
            //delete the verification link after the user has reset their password
            jdbc.update(DELETE_VERIFICATION_BY_URL_QUERY, of("url", getVerificationUrl(key, PASSWORD.getType())));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Error, intente de nuevo.");
        }
    }

    @Override
    public void renewPassword(Long userId, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new ApiException("Las contrasenas no coinciden. Intente nuevamente.");
        }
        try {
            String pass = encoder.encode(password);
            jdbc.update(UPDATE_USER_PASSWORD_BY_USER_ID_QUERY, of("id", userId, "password", pass));
            //delete the verification link after the user has reset their password
            //commented out to keep testing the link
            //jdbc.update(DELETE_PASSWORD_VERIFICATION_BY_USER_ID_QUERY, of("userId", userId));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Error, intente de nuevo.");
        }
    }

    @Override
    public User verifyAccountKey(String key) {
        try{
            String url = getVerificationUrl(key, ACCOUNT.getType());
            User user = jdbc.queryForObject(SELECT_USER_BY_ACCOUNT_URL_QUERY, of("url", url), new UserRowMapper());
            //only true in the database and not in the user object
            jdbc.update(UPDATE_USER_ENABLED_QUERY, of("enabled", true,"id", user.getId()));
            // we should delete after updating, really useful to have
            return user;
        }catch(EmptyResultDataAccessException exception){
            throw new ApiException("Este enlace no es valido. Resetea tu contrasena nuevamente");
        }catch(Exception exception){
            throw new ApiException("Error, intente de nuevo.");
        }
    }

    private Boolean isLinkExpired(String key, VerificationType password) {
        try{
            String url = getVerificationUrl(key, password.getType());
            return jdbc.queryForObject(SELECT_EXPIRATION_BY_URL, of("url", url), Boolean.class);
        }catch(EmptyResultDataAccessException exception){
            log.error(exception.getMessage());
            throw new ApiException("Este enlace no es valido.Resetea tu contrasena nuevamente.");
        }catch(Exception exception){
            log.error(exception.getMessage());
            throw new ApiException("Error, intente de nuevo.");
        }
    }

    private Boolean isVerificationCodeExpired(String code) {
        try{
            return jdbc.queryForObject(SELECT_CODE_EXPIRATION_QUERY, of("code", code), Boolean.class);
        }catch(EmptyResultDataAccessException exception){
            throw new ApiException("Codigo invalido, intente nuevamente.");
        }catch(Exception exception){
            throw new ApiException("Error, intente de nuevo.");
        }
    }

    @Override
    public User updateUserDetails(UpdateForm user) {
        try{
            jdbc.update(UPDATE_USER_DETAILS_QUERY, getUserDetailsSqlParameterSource(user));
            return get(user.getId());
        }catch(EmptyResultDataAccessException exception){
            throw new ApiException("No se encontro usuario por id: " + user.getId());
        }catch(Exception exception){
            log.error(exception.getMessage());
            throw new ApiException("No se pudo actualizar.");
        }
    }

    @Override
    public void updatePassword(Long id, String currentPassword, String newPassword, String confirmNewPassword) {
        if (!newPassword.equals(confirmNewPassword)) {
            throw new ApiException("Las contrasenas no coinciden. Intente nuevamente.");
        }
        User user = get(id);
        if (encoder.matches(currentPassword, user.getPassword())) {
            try {
//                String pass = encoder.encode(newPassword); //had this before, but it's redundant, commenting it
                                                            //out made the code run faster...
                jdbc.update(UPDATE_USER_PASSWORD_BY_ID_QUERY, of("userId", id, "password", encoder.encode(newPassword)));
            } catch (Exception exception) {
//                log.error(exception.getMessage()); //already in HandledException
                throw new ApiException("Error, intente de nuevo.");
            }
        } else {
            throw new ApiException("Contrasena actual incorrecta. Intente nuevamente.");
        }
    }

    @Override
    public void updateAccountSettings(Long userId, Boolean enabled, Boolean notLocked) {
        try{
            jdbc.update(UPDATE_USER_SETTINGS_QUERY, of("userId", userId, "enabled", enabled, "notLocked", notLocked));
        }catch(Exception exception){
            log.error(exception.getMessage());
            throw new ApiException("Error, intente de nuevo.");
        }
    }

    @Override
    public User toggleMfa(String email) {
        User user = getUserByEmail(email);
        if(isBlank(user.getPhone())){ //if its blank or whitespace
            throw new ApiException("Necesita un numero de telefono para cambiar la autenticacion multifactor.");
        }
        user.setUsingMfa(!user.isUsingMfa());
        try{
            jdbc.update(TOGGLE_USER_MFA_QUERY, of("email", email, "isUsingMfa", user.isUsingMfa()));
            return user;
        }catch(Exception exception){
            log.error(exception.getMessage());
            throw new ApiException("No se pudo cambiar la autenticacion multifactor.");
        }
    }

    @Override
    public void updateImage(UserDTO userDTO, MultipartFile image) {
        String userImageUrl = setUserImageUrl(userDTO.getEmail());
        userDTO.setImageUrl(userImageUrl);
        saveImage(userDTO.getEmail(), image);
        jdbc.update(UPDATE_USER_IMAGE_QUERY, of("imageUrl", userImageUrl,"id", userDTO.getId()));
    }

    private String setUserImageUrl(String email) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/image/" + email + ".png").toUriString();
    }

    private void saveImage(String email, MultipartFile image) {
        Path fileStorageLocation = Paths.get(System.getProperty("user.home") + "/Downloads/images")
                .toAbsolutePath().normalize();
        if(!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(fileStorageLocation);
            } catch (Exception exception) {
                log.error(exception.getMessage());
                throw new ApiException("No se pudo crear el directorio para guardar la imagen.");
            }
            log.info("Directorio creado con exito: " + fileStorageLocation);
        }
        try {
//            Files.deleteIfExists(fileStorageLocation.resolve(email + ".png"));
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(email + ".png"), REPLACE_EXISTING);
        } catch (Exception exception) {
            log.error(exception.getMessage());
//            throw new ApiException("No se pudo guardar la imagen.");
            throw new ApiException(exception.getMessage());
        }
        log.info("Imagen guardada con exito: " + email + ".png en " + fileStorageLocation);
    }

    private SqlParameterSource getUserDetailsSqlParameterSource(UpdateForm user) {
        return new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("phone", user.getPhone())
                .addValue("address", user.getAddress())
                .addValue("title", user.getTitle())
                .addValue("bio", user.getBio());
    }

}


