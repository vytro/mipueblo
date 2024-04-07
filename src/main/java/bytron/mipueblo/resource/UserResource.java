package bytron.mipueblo.resource;

import bytron.mipueblo.domain.HttpResponse;
import bytron.mipueblo.domain.User;
import bytron.mipueblo.domain.UserPrincipal;
import bytron.mipueblo.dto.UserDTO;
import bytron.mipueblo.dtomapper.UserDTOMapper;
import bytron.mipueblo.enumeration.EventType;
import bytron.mipueblo.event.NewUserEvent;
import bytron.mipueblo.exception.ApiException;
import bytron.mipueblo.form.*;
import bytron.mipueblo.provider.TokenProvider;
import bytron.mipueblo.service.EventService;
import bytron.mipueblo.service.RoleService;
import bytron.mipueblo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

//used to be javax
import jakarta.validation.Valid;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static bytron.mipueblo.constant.Constants.TOKEN_PREFIX;
import static bytron.mipueblo.utils.ExceptionUtils.processError;

import static bytron.mipueblo.utils.UserUtils.getAuthenticatedUser;
import static bytron.mipueblo.utils.UserUtils.getLoggedInUser;

import static java.time.LocalTime.now;
import static java.util.Map.of;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;
//    @Autowired
    private final RoleService roleService;
    private final EventService eventService;

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    private final ApplicationEventPublisher publisher;

    private static final Logger logger = LoggerFactory.getLogger(UserResource.class);

    @PostMapping("/login")
//    public ResponseEntity<HttpResponse> login(String email, String password) {
    public ResponseEntity<HttpResponse> login(@RequestBody @Valid LoginForm loginForm) {
//        System.out.println("login method called");
//        logger.info("someEndpoint method called");
        logger.info("login method called");
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword()));
//        authenticationManager.authenticate(unauthenticated(loginForm.getEmail(), loginForm.getPassword()));
        //Authentication authentication = authenticate(loginForm.getEmail(), loginForm.getPassword());
//        UserDTO userDTO = getAuthenticatedUser(authentication);
        //UserDTO userDTO = getLoggedInUser(authentication);
        UserDTO userDTO = authenticate(loginForm.getEmail(), loginForm.getPassword());
        //System.out.println(authentication);
        System.out.println(userDTO);
        //System.out.println(((UserPrincipal) authentication.getPrincipal()).getUser());
//        UserDTO userDTO = userService.getUserByEmail(loginForm.getEmail());
        System.out.println("Inicio de sesion exitoso");
        return userDTO.isUsingMfa() ? sendVerificationCode(userDTO) : sendResponse(userDTO);

//        return ResponseEntity.ok().body(
//                HttpResponse.builder()
//                        .timestamp(now().toString())
//                        .data(of("user", userDTO))
//                        .message("Login successful")
//                        .status(HttpStatus.OK)
//                        .statusCode(HttpStatus.OK.value())
//                        .build()
//        );
    }

//    private UserDTO getAuthenticatedUser(Authentication authentication){
//        return ((UserPrincipal) authentication.getPrincipal()).getUser();
//    }




    //og works v1
//    private UserDTO authenticate(String email, String password) {
//        String test = "login";
//        try{
//            if(null != userService.getUserByEmail(email)){
//                publisher.publishEvent(new NewUserEvent(email, EventType.LOGIN_ATTEMPT));
//                System.out.println("login attempt");
//                Thread.sleep(500); //0.5 second delay, to save login_attempt event before the success event
//                // not recommeded, should use transactional
//                // as in the method commented below
//                // org.springframework.transaction.interceptor.TransactionAspectSupport
//                // for the flush method.
//            }
//            Authentication authentication = authenticationManager.authenticate(unauthenticated(email, password));
//            UserDTO loggedInUser = getLoggedInUser(authentication);
//            if(!loggedInUser.isUsingMfa()){
//                publisher.publishEvent(new NewUserEvent(email, EventType.LOGIN_ATTEMPT_SUCCESS));
//                System.out.println("login attempt success");
//            }
//            return loggedInUser;
//        }catch(Exception exception){
//            publisher.publishEvent(new NewUserEvent(email, EventType.LOGIN_ATTEMPT_FAILURE));
//            processError(request, response,exception);
//            throw new ApiException(exception.getMessage());
////            return null; //to comment out ApiException
//        }
//    }

    //v2
//    private UserDTO authenticate(String email, String password) {
//        try {
//            UserDTO userDTO = userService.getUserByEmail(email);
//            if (userDTO != null) {
//                publisher.publishEvent(new NewUserEvent(email, EventType.LOGIN_ATTEMPT));
//                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
//                UserDTO loggedInUser = getLoggedInUser(authentication);
//                if (!loggedInUser.isUsingMfa()) {
//                    publisher.publishEvent(new NewUserEvent(email, EventType.LOGIN_ATTEMPT_SUCCESS));
//                }
//                return loggedInUser;
//            } else {
//                throw new BadCredentialsException("Incorrect Email or Password.");
//            }
//        } catch (Exception exception) {
//            publisher.publishEvent(new NewUserEvent(email, EventType.LOGIN_ATTEMPT_FAILURE));
//            processError(request, response, exception);
//            throw new ApiException(exception.getMessage());
//        }
//    }

    //v3
    private UserDTO authenticate(String email, String password) {
        UserDTO userDTOByEmail = userService.getUserByEmail(email);
        try {
//            UserDTO userDTOByEmail = userService.getUserByEmail(email);
            if (userDTOByEmail != null) {
                publisher.publishEvent(new NewUserEvent(email, EventType.LOGIN_ATTEMPT));
                try {
                    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
                    UserDTO loggedInUser = getLoggedInUser(authentication);
                    if (!loggedInUser.isUsingMfa()) {
                        publisher.publishEvent(new NewUserEvent(email, EventType.LOGIN_ATTEMPT_SUCCESS));
                    }
                    return loggedInUser;
                } catch (BadCredentialsException e) {
                    throw new BadCredentialsException("Correo o contrasena incorrecto");
                }
            } else {
                throw new BadCredentialsException("Correo o contrasena incorrecto");
            }
        } catch (Exception exception) {
            if(null != userDTOByEmail){
                publisher.publishEvent(new NewUserEvent(email, EventType.LOGIN_ATTEMPT_FAILURE));
                }
            processError(request, response, exception);
            throw new ApiException(exception.getMessage());
        }
    }



    //better way, for production in the future
//   import org.springframework.transaction.annotation.Transactional;
//    @Transactional
//    private UserDTO authenticate(String email, String password) {
//        String test = "login";
//        try{
//            if(null != userService.getUserByEmail(email)){
//                publisher.publishEvent(new NewUserEvent(email, EventType.LOGIN_ATTEMPT));
//                System.out.println("login attempt");
//            }
//            // Force the current transaction to commit before continuing
//            TransactionAspectSupport.currentTransactionStatus().flush();
//            Authentication authentication = authenticationManager.authenticate(unauthenticated(email, password));
//            UserDTO loggedInUser = getLoggedInUser(authentication);
//            if(!loggedInUser.isUsingMfa()){
//                publisher.publishEvent(new NewUserEvent(email, EventType.LOGIN_ATTEMPT_SUCCESS));
//                System.out.println("login attempt success");
//            }
//            return loggedInUser;
//        }catch(Exception exception){
//            publisher.publishEvent(new NewUserEvent(email, EventType.LOGIN_ATTEMPT_FAILURE));
//            processError(request, response,exception);
//            throw new ApiException(exception.getMessage());
//        }
//    }



    @PostMapping("/register")
    public ResponseEntity<HttpResponse> saveUser(@RequestBody @Valid User user) {
        UserDTO userDTO = userService.createUser(user);
        return ResponseEntity.created(getUri()).body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("user", userDTO))
                        .message("Cuenta creada para el usuario: " + userDTO.getFirstName() + " " + userDTO.getLastName())
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<HttpResponse> profile(Authentication authentication) {
        UserDTO userDTO = userService.getUserByEmail(getAuthenticatedUser(authentication).getEmail());
//        UserDTO userDTO = userService.getUserByEmail(authentication.getName());
//        System.out.println(authentication.getPrincipal()); //returns email
        System.out.println("UserResource Authenticated user: " + userDTO);
        //comment back later
        //System.out.println(authentication); //returns everything
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("user", userDTO,
                                "events", eventService.getEventByUserId(userDTO.getId()),
                                "roles", roleService.getRoles()))
                        .message("Perfil Obtenido")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/update")
    public ResponseEntity<HttpResponse> updateUser(@RequestBody @Valid UpdateForm user) throws InterruptedException {
//        TimeUnit.SECONDS.sleep(1); //don't need this anymore because we are using the event listener
        UserDTO updatedUserDTO = userService.updateUserDetails(user);
        publisher.publishEvent(new NewUserEvent(updatedUserDTO.getEmail(), EventType.PROFILE_UPDATE));
//        System.out.println(authentication); //returns everything
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
//                        .data(of("user", updatedUserDTO))
                        .data(of("user", updatedUserDTO,
                                "events", eventService.getEventByUserId(updatedUserDTO.getId()),
                                "roles", roleService.getRoles()))
                        .message("Usuario Actualizado")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    //reset the password when the user is not logged in  v

    @GetMapping("/verify/code/{email}/{code}")
    public ResponseEntity<HttpResponse> verifyCode(@PathVariable("email") String email, @PathVariable("code") String code) {
        UserDTO userDTO = userService.verifyCode(email, code);
        publisher.publishEvent(new NewUserEvent(userDTO.getEmail(), EventType.LOGIN_ATTEMPT_SUCCESS));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("user", userDTO, "access_token", tokenProvider.createAccessToken(getUserPrincipal(userDTO))
                                , "refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(userDTO))))
                        .message("Inicio de sesion exitosa")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/resetpassword/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) {
        userService.resetPassword(email);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .message("Correo electronico enviado, verifique su correo electrónico para restablecer su contraseña")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/verify/password/{key}")
    public ResponseEntity<HttpResponse> verifyPasswordUrl(@PathVariable("key") String key) throws InterruptedException {
//        System.out.println("should be fired");
//        TimeUnit.SECONDS.sleep(3);
//        throw new ApiException("Purposefully thrown exception");
        UserDTO userDTO = userService.verifyPasswordKey(key);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("user", userDTO))
                        .message("Por favor ingrese una nueva contrasena")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    //not recommended to pass in the password in the url
//    @PostMapping("/resetpassword/{key}/{password}/{confirmPassword}")
//    public ResponseEntity<HttpResponse> verifyPasswordWithKey(@PathVariable("key") String key,
//                                                          @PathVariable("password") String password,
//                                                          @PathVariable("confirmPassword") String confirmPassword) {
//        userService.renewPassword(key, password, confirmPassword);
//        return ResponseEntity.ok().body(
//                HttpResponse.builder()
//                        .timestamp(now().toString())
//                        .message("Password reset successfully.")
//                        .status(HttpStatus.OK)
//                        .statusCode(HttpStatus.OK.value())
//                        .build()
//        );
//    }
    @PutMapping("/new/password")
    public ResponseEntity<HttpResponse> verifyPasswordWithKey(@RequestBody @Valid NewPasswordForm form) {
        userService.updatePassword(form.getUserId(), form.getPassword(), form.getConfirmPassword());
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .message("Restablecimiento de contraseña exitoso")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    //reset the password when the user is not logged in  ^

    @GetMapping( "/verify/account/{key}")
    public ResponseEntity<HttpResponse> verifyAccount(@PathVariable("key") String key) throws InterruptedException {
//        TimeUnit.SECONDS.sleep(3);
//        throw new ApiException("Purposefully thrown exception");
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .message(userService.verifyAccountKey(key).isEnabled() ? "Cuenta ya se verifico." : "Cuenta Verificado.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/update/password")
    public ResponseEntity<HttpResponse> updatePassword(Authentication authentication, @RequestBody @Valid UpdatePasswordForm form) {
        UserDTO userDTO = getAuthenticatedUser(authentication);
        userService.updatePassword(userDTO.getId(), form.getCurrentPassword(), form.getNewPassword(), form.getConfirmNewPassword());
        publisher.publishEvent(new NewUserEvent(userDTO.getEmail(), EventType.PASSWORD_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .data(of("user", userService.getUserById(userDTO.getId()),
                                "events", eventService.getEventByUserId(userDTO.getId()),
                                "roles", roleService.getRoles()))
                        .timestamp(now().toString())
                        .message("Constrasena se actualizo correctamente")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/update/role/{roleName}")
    public ResponseEntity<HttpResponse> updateUserRole(Authentication authentication, @PathVariable("roleName") String roleName) {
        UserDTO userDTO = getAuthenticatedUser(authentication);
        userService.updateUserRole(userDTO.getId(), roleName);
        publisher.publishEvent(new NewUserEvent(userDTO.getEmail(), EventType.ROLE_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .data(of("user", userService.getUserById(userDTO.getId()),
                                "events", eventService.getEventByUserId(userDTO.getId()),
                                "roles", roleService.getRoles()))
                        .timestamp(now().toString())
                        .message("Role se actualizo correctamente")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/update/settings")
    public ResponseEntity<HttpResponse> updateAccountSettings(Authentication authentication,  @RequestBody @Valid SettingsForm form) {
        UserDTO userDTO = getAuthenticatedUser(authentication);
        userService.updateAccountSettings(userDTO.getId(), form.getEnabled(), form.getNotLocked());
        publisher.publishEvent(new NewUserEvent(userDTO.getEmail(), EventType.ACCOUNT_SETTINGS_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        //we recall the user again in case something changed when we make the update call
                        .data(of("user", userService.getUserById(userDTO.getId()),
                                "events", eventService.getEventByUserId(userDTO.getId()),
                                "roles", roleService.getRoles()))
                        .timestamp(now().toString())
                        .message("Configuracion de la cuenta se actualizo correctamente")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/togglemfa")
    public ResponseEntity<HttpResponse> toggleMfa(Authentication authentication) throws InterruptedException {
//        TimeUnit.SECONDS.sleep(1);
        UserDTO userDTO = userService.toggleMfa(getAuthenticatedUser(authentication).getEmail());
        publisher.publishEvent(new NewUserEvent(userDTO.getEmail(), EventType.MFA_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        //we recall the user again in case something changed when we make the update call
                        .data(of("user", userDTO,
                                "events", eventService.getEventByUserId(userDTO.getId()),
                                "roles", roleService.getRoles()))
                        .timestamp(now().toString())
                        .message("Autenticacion multifactor se actualizo correctamente")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/update/image")
    public ResponseEntity<HttpResponse> updateProfileImage(Authentication authentication, @RequestParam("image")MultipartFile image) throws InterruptedException {
        UserDTO userDTO = getAuthenticatedUser(authentication);
        userService.updateImage(userDTO, image);
        publisher.publishEvent(new NewUserEvent(userDTO.getEmail(), EventType.PROFILE_PICTURE_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        //we recall the user again in case something changed when we make the update call
                        .data(of("user", userService.getUserById(userDTO.getId()),
                                "events", eventService.getEventByUserId(userDTO.getId()),
                                "roles", roleService.getRoles()))
                        .timestamp(now().toString())
                        .message("Image de perfil se actualizo correctamente")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping(value ="/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public byte[] getProfileImage(@PathVariable("fileName") String fileName) throws Exception {
        return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/Downloads/images/" + fileName));
    }


    @GetMapping("/refresh/token")
    public ResponseEntity<HttpResponse> refreshToken(HttpServletRequest request) {
        if(isHeaderAndTokenValid(request)){
            //get the token from the header without the Bearer
            String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(TOKEN_PREFIX.length());
//            UserDTO userDTO = userService.getUserByEmail(tokenProvider.getSubject(token, request));
            UserDTO userDTO = userService.getUserById(tokenProvider.getSubject(token, request));
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(of("user", userDTO, "access_token", tokenProvider.createAccessToken(getUserPrincipal(userDTO))
                                    , "refresh_token", token))
                            .message("Token Actualizado")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        }
        return ResponseEntity.badRequest().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .reason("Refresh token missing or invalid")
                        .developerMessage("El token de actualizacion falta o no es válido")
                        .status(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build()
        );
    }

    private boolean isHeaderAndTokenValid(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION) != null
                && request.getHeader(HttpHeaders.AUTHORIZATION).startsWith(TOKEN_PREFIX) //it starts with Bearer
                && tokenProvider.isTokenValid(
                        tokenProvider.getSubject(
                                request.getHeader(HttpHeaders.AUTHORIZATION).substring(TOKEN_PREFIX.length()), request),
                        request.getHeader(HttpHeaders.AUTHORIZATION).substring(TOKEN_PREFIX.length()));
    }

//    @RequestMapping("/error")
//    public ResponseEntity<HttpResponse> handleError0(HttpServletRequest request) {
//
//        return ResponseEntity.badRequest().body(
//                HttpResponse.builder()
//                        .timestamp(now().toString())
//                        .reason("There is no mapping for a  " + request.getMethod() + " request for this path on the server")
//                        .status(HttpStatus.BAD_REQUEST)
//                        .statusCode(HttpStatus.BAD_REQUEST.value())
//                        .build()
//        );
//    }

    //better
    @RequestMapping("/error")
    public ResponseEntity<HttpResponse> handleError(HttpServletRequest request) {
        return new ResponseEntity<>(HttpResponse.builder()
                .timestamp(now().toString())
                .reason("No hay ninguna asignacion para una solicitud  " + request.getMethod() + " para esta ruta en el servidor")
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .build(), HttpStatus.NOT_FOUND);
    }

    private URI getUri() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }

    private ResponseEntity<HttpResponse> sendResponse(UserDTO userDTO) {
        return ResponseEntity.ok().body(
        HttpResponse.builder()
                .timestamp(now().toString())
                .data(of("user", userDTO, "access_token", tokenProvider.createAccessToken(getUserPrincipal(userDTO))
                , "refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(userDTO))))
                .message("Inicio de sesion exitosa")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

    private UserPrincipal getUserPrincipal(UserDTO userDTO) {
        return new UserPrincipal(UserDTOMapper.toUser(userService.getUserByEmail(userDTO.getEmail())), roleService.getRoleByUserId(userDTO.getId()));
    }

    private ResponseEntity<HttpResponse> sendVerificationCode(UserDTO userDTO) {
        userService.sendVerificationCode(userDTO);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("user", userDTO))
                        .message("Codigo de verificacion enviado")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}













