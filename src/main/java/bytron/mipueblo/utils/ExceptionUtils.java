package bytron.mipueblo.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import bytron.mipueblo.exception.ApiException;

import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import bytron.mipueblo.domain.HttpResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;

import static java.time.LocalTime.now;
//import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
public class ExceptionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionUtils.class);


//    public static void processError(HttpServletRequest request, HttpServletResponse response, Exception exception) {
//
//        if(exception instanceof ApiException
//                || exception instanceof DisabledException
//                || exception instanceof LockedException
//                || exception instanceof InvalidClaimException
//                || exception instanceof TokenExpiredException
//                || exception instanceof BadCredentialsException){
//            HttpResponse httpResponse = getHttpResponse(response, exception.getMessage(), HttpStatus.BAD_REQUEST);
//            writeResponse(response, httpResponse);
//        } else {
//            HttpResponse httpResponse = getHttpResponse(response, "Error, intente nuevamente.", HttpStatus.INTERNAL_SERVER_ERROR);
//            writeResponse(response, httpResponse);
//        }
//        log.error(exception.getMessage());
//    }

    public static void processError(HttpServletRequest request, HttpServletResponse response, Exception exception) {

        HttpStatus status;
        if(exception instanceof ApiException
                || exception instanceof DisabledException
                || exception instanceof LockedException
                || exception instanceof InvalidClaimException
                || exception instanceof BadCredentialsException){
//            System.out.println(exception.getMessage());
            status = HttpStatus.BAD_REQUEST;
        } else if (exception instanceof TokenExpiredException) {
            status = HttpStatus.UNAUTHORIZED;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        HttpResponse httpResponse = getHttpResponse(response, exception.getMessage(), status);
        writeResponse(response, httpResponse);
        //maybe giving the long error message, working with it
//        LOGGER.error(exception.getMessage(), exception);
    }


    private static void writeResponse(HttpServletResponse response, HttpResponse httpResponse) {
        OutputStream out;
        try{
            out = response.getOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, httpResponse);
            out.flush();
        }catch(Exception exception){
            log.error(exception.getMessage());
            exception.printStackTrace();
        }
    }


    private static HttpResponse getHttpResponse(HttpServletResponse response, String message, HttpStatus httpStatus) {
        HttpResponse httpResponse = HttpResponse.builder()
                .timestamp(now().toString())
                .reason(message)
                .status(httpStatus)
                .statusCode(httpStatus.value())
                .build();
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        return httpResponse;
    }
}
