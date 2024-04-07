package bytron.mipueblo.domain;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@SuperBuilder
@JsonInclude(NON_DEFAULT)
public class HttpResponse {
    protected String timestamp;
    protected int statusCode; //200, 201, 400, 500
    protected HttpStatus status; //OK, CREATED, BAD_REQUEST, INTERNAL_SERVER_ERROR
    protected String reason; //message for the user reason
    protected String message; //message for the user message
    protected String developerMessage; //message for the developer
    protected Map<?, ?> data; //data to be returned
}
