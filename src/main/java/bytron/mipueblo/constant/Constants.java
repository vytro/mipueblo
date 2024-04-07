package bytron.mipueblo.constant;

public class Constants {

    // for SecurityConfig.java
    public static final String[] PUBLIC_URLS = { "/user/login/**",
            "/user/register/**",
            "/user/verify/code/**",
            "/user/resetpassword/**",
            "/user/verify/password/**",
            "/user/verify/account/**",
            "/user/refresh/token/**",
            "/user/image/**",
            "/user/new/password/**"};
    //use for testing 404
    //public static final String[] PUBLIC_URLS = { "/**" };



    // for CustomAuthorizationFilter.java and UserResource.java
    public static final String TOKEN_PREFIX = "Bearer ";


    // for CustomAuthorizationFilter.java
    public static final String[] PUBLIC_ROUTES = {"/user/login", "/user/register", "/user/verify/code", "/user/refresh/token", "/user/image", "/user/new/password"};
    public static final String HTTP_OPTIONS_METHOD = "OPTIONS";



    // for TokenProvider.java
    public static final String AUTHORITIES = "authorities";
    public static final String BYTRON = "BYTRON";
    public static final String CUSTOMER_MANAGEMENT_SERVICE = "MANAGEMENT_SERVICE";
    //    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 5 * 60 * 1000;
    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 1_800_000; // 30 minutes

//    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 30 * 1000; // 30 seconds for testing

    //to test BAD_REQUEST in postman
//    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 100;
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 432_000_000;  // 5 days
//    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 30 * 1000; // 30 seconds for testing


    //for UserRepositoryImpl.java
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

}
