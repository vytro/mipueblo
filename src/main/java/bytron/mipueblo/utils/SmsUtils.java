package bytron.mipueblo.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import static com.twilio.rest.api.v2010.account.Message.creator;


public class SmsUtils {

    //public static final String FROM_NUMBER = "";
    //public static final String SID_KEY = "";
    //public static final String TOKEN_KEY = "";

    public static void sendSMS(String to, String messageBody) {
        //Twilio.init(SID_KEY, TOKEN_KEY);
        //Message message = creator(new PhoneNumber("+" + to), new PhoneNumber(FROM_NUMBER), messageBody).create();
    }

}
