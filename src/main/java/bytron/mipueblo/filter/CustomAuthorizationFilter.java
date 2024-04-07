package bytron.mipueblo.filter;

import bytron.mipueblo.provider.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

//import static java.util.stream.Stream.ofNullable;
//import static org.springframework.data.mapping.Alias.ofNullable;
import static bytron.mipueblo.constant.Constants.*;
import static bytron.mipueblo.utils.ExceptionUtils.processError;
import static java.util.Arrays.asList;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
//    protected static final String TOKEN_KEY = "token";
//    protected static final String EMAIL_KEY = "email";

//    //OG code
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter) throws ServletException, IOException {
//        try{
////            Map<String, String> values = getRequestValues(request);
//            String token = getToken(request);
//            Long userId = getUserId(request);
////            if(tokenProvider.isTokenValid(values.get(EMAIL_KEY), token)){
//            if(tokenProvider.isTokenValid(userId, token)){
////                List<GrantedAuthority> authorities = tokenProvider.getAuthorities(values.get(TOKEN_KEY));
//                List<GrantedAuthority> authorities = tokenProvider.getAuthorities(token);
////                Authentication authentication = tokenProvider.getAuthentication(values.get(EMAIL_KEY), authorities, request);
//                Authentication authentication = tokenProvider.getAuthentication(userId, authorities, request);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//            else{
//                SecurityContextHolder.clearContext();
//            }
//            //allows the request to continue
//            filter.doFilter(request, response);
//        }
//        catch (Exception e){
//            log.error(e.getMessage());
//            System.out.println("MYerror");
////            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            processError(request, response, e);
//        }
//    }

    //log code replace later
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter) throws ServletException, IOException {
        try{
            String token = getToken(request);
            Long userId = getUserId(request);
            System.out.println("CAF Token: " + token);
            boolean isValid = tokenProvider.isTokenValid(userId, token);
            System.out.println("Token is valid in CustomAuthorizationFilter: " + isValid);
            if(isValid){
                List<GrantedAuthority> authorities = tokenProvider.getAuthorities(token);
                Authentication authentication = tokenProvider.getAuthentication(userId, authorities, request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else{
                SecurityContextHolder.clearContext();
            }
            filter.doFilter(request, response);
        }
        catch (Exception e){
            log.error(e.getMessage());
            System.out.println("MYerror");
            processError(request, response, e);
        }
    }

    private String getToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .filter(header -> header.startsWith(TOKEN_PREFIX))
                .map(token -> token.replace(TOKEN_PREFIX, StringUtils.EMPTY)).get();
//                .findFirst()
//                .orElse(null);
    }

//    private Map<String, String> getRequestValues(HttpServletRequest request) {
    private Long getUserId(HttpServletRequest request) {
//        return Map.of(EMAIL_KEY, tokenProvider.getSubject(getToken(request), request), TOKEN_KEY, getToken(request));
        return tokenProvider.getSubject(getToken(request), request);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getHeader(AUTHORIZATION) == null
                || !request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX)
                || request.getMethod().equalsIgnoreCase(HTTP_OPTIONS_METHOD)
                || asList(PUBLIC_ROUTES).contains(request.getRequestURI());
//                || request.getRequestURI().equals("/login") || request.getRequestURI().equals("/register");
    }
}

//
//package bytron.mipueblo.filter;
//
//import bytron.mipueblo.provider.TokenProvider;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.List;
//
//import static bytron.mipueblo.utils.ExceptionUtils.processError;
//import static java.util.Arrays.asList;
//import static java.util.Optional.ofNullable;
//import static org.apache.commons.lang3.StringUtils.EMPTY;
//import static org.springframework.http.HttpHeaders.AUTHORIZATION;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class CustomAuthorizationFilter extends OncePerRequestFilter {
//    private static final String TOKEN_PREFIX = "Bearer ";
//    private static final String[] PUBLIC_ROUTES = { "/user/login", "/user/verify/code", "/user/register", "/user/refresh/token" };
//    private static final String HTTP_OPTIONS_METHOD = "OPTIONS";
//    private final TokenProvider tokenProvider;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter) throws ServletException, IOException {
//        try {
//            String token = getToken(request);
//            Long userId = Long.valueOf(getUserId(request));
//            if(tokenProvider.isTokenValid(String.valueOf(userId), token)) {
//                List<GrantedAuthority> authorities = tokenProvider.getAuthorities(token);
//                Authentication authentication = tokenProvider.getAuthentication(String.valueOf(userId), authorities, request);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            } else { SecurityContextHolder.clearContext(); }
//            filter.doFilter(request, response);
//        } catch (Exception exception) {
//            log.error(exception.getMessage());
//            processError(request, response, exception);
//        }
//
//    }
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        return request.getHeader(AUTHORIZATION) == null || !request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX) ||
//                request.getMethod().equalsIgnoreCase(HTTP_OPTIONS_METHOD) || asList(PUBLIC_ROUTES).contains(request.getRequestURI());
//    }
//
//    private String getUserId(HttpServletRequest request) {
//        return tokenProvider.getSubject(getToken(request), request);
//    }
//
//    private String getToken(HttpServletRequest request) {
//        return ofNullable(request.getHeader(AUTHORIZATION))
//                .filter(header -> header.startsWith(TOKEN_PREFIX))
//                .map(token -> token.replace(TOKEN_PREFIX, EMPTY)).get();
//    }
//}






