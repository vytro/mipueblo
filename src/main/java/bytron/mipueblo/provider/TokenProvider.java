package bytron.mipueblo.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import bytron.mipueblo.domain.UserPrincipal;
import bytron.mipueblo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static bytron.mipueblo.constant.Constants.*;
import static java.util.Arrays.stream;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    private final UserService userService;

    public String createAccessToken(UserPrincipal userPrincipal) {
        //for testing in docker
        System.out.println("####################");
        System.out.println("Secret Value Isz: " + secret);
        System.out.println("####################");
        String[] claims = getClaimsFromUser(userPrincipal);
        return JWT.create().withIssuer(BYTRON).withAudience(CUSTOMER_MANAGEMENT_SERVICE)
//                .withIssuedAt(new Date()).withSubject(userPrincipal.getUsername()).withArrayClaim(AUTHORITIES, claims)
                .withIssuedAt(new Date()).withSubject(String.valueOf(userPrincipal.getUser().getId())).withArrayClaim(AUTHORITIES, claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    public String createRefreshToken(UserPrincipal userPrincipal) {
        return JWT.create().withIssuer(BYTRON).withAudience(CUSTOMER_MANAGEMENT_SERVICE)
//                .withIssuedAt(new Date()).withSubject(userPrincipal.getUsername())
                .withIssuedAt(new Date()).withSubject(String.valueOf(userPrincipal.getUser().getId()))
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

//    public String getSubject(String token, HttpServletRequest request) {
    public Long getSubject(String token, HttpServletRequest request) {
        try {
//            return getJWTVerifier().verify(token).getSubject();
            return Long.valueOf(getJWTVerifier().verify(token).getSubject());
        } catch (TokenExpiredException exception) {
            request.setAttribute("expiredMessage", exception.getMessage());
            throw exception; //might not use it later
        } catch (InvalidClaimException exception) {
            request.setAttribute("invalidClaim", exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            throw exception;
        }
//        return verifier.verify(token).getSubject();
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        String[] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority :: new).collect(Collectors.toList());
    }

//    public Authentication getAuthentication(String email, List<GrantedAuthority> authorities, HttpServletRequest request) {
    public Authentication getAuthentication(Long userId, List<GrantedAuthority> authorities, HttpServletRequest request) {
//        UsernamePasswordAuthenticationToken userPasswordAuthToken = new UsernamePasswordAuthenticationToken(userService.getUserByEmail(email), null, authorities);
        UsernamePasswordAuthenticationToken userPasswordAuthToken = new UsernamePasswordAuthenticationToken(userService.getUserById(userId), null, authorities);
//        userPasswordAuthToken.setDetails(request.getRemoteAddr());
        userPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return userPasswordAuthToken;
    }

//    public boolean isTokenValid(String email, String token) {
    public boolean isTokenValid(Long userId, String token) {
        JWTVerifier verifier = getJWTVerifier();
        System.out.println("isTokenValid method called for userId: " + userId);
//        return email != null && !email.isEmpty() && !isTokenExpired(verifier, token);
//        return !Objects.isNull(userId) && !isTokenExpired(verifier, token);
        boolean isValid = !Objects.isNull(userId) && !isTokenExpired(verifier, token);
        System.out.println("Token is valid in TokenProvider: " + isValid);
        return isValid;
    }

    public boolean isTokenExpired(JWTVerifier verifier, String token) {
//        Date expiration = verifier.verify(token).getExpiresAt();
//        System.out.println("isTokenExpired method called");
//        return expiration.before(new Date());
        Date expiration = verifier.verify(token).getExpiresAt();
        Date now = new Date();
        boolean expired = expiration.before(now);

        // Logging the details
        System.out.println("Token expiration time: " + expiration);
        System.out.println("Current time: " + now);
        System.out.println("TP Is token expired? " + expired);

        return expired;
    }

    private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
        return userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
    }

    private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            verifier = JWT.require(algorithm).withIssuer(BYTRON).build();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("No se puede confiar el token.");
        }
        return verifier;
    }
}















