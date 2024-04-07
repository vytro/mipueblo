package bytron.mipueblo.utils;

import jakarta.servlet.http.HttpServletRequest;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

public class RequestUtils {
//    public static String getIpAddress(HttpServletRequest request) {
//
//        String ipAddress = "Unknown IP";
//
//        if(request != null) {
//            ipAddress = request.getHeader("X-Forwarded-For");
//            if(ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress))
//                ipAddress = request.getRemoteAddr();
//        }
//        return ipAddress;
//    }
//
//    public static String getDevice(HttpServletRequest request) {
//        UserAgentAnalyzer userAgentAnalyzer = UserAgentAnalyzer.newBuilder()
//                .hideMatcherLoadStats()
//                .withCache(1000)
//                .build();
//        UserAgent agent = userAgentAnalyzer.parse(request.getHeader("User-Agent"));
////        System.out.println("From RequestUtils: " + agent);
//        return agent.getValue(UserAgent.OPERATING_SYSTEM_NAME) + " - " + agent.getValue(UserAgent.AGENT_NAME) + " - " + agent.getValue(UserAgent.DEVICE_NAME);
//    }


    private static final UserAgentAnalyzer userAgentAnalyzer;

    static {
        userAgentAnalyzer = UserAgentAnalyzer.newBuilder()
                .hideMatcherLoadStats()
                .withCache(1000)
                .build();
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = "Unknown IP";
        if(request != null) {
            ipAddress = request.getHeader("X-Forwarded-For");
            if(ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress))
                ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    public static String getDevice(HttpServletRequest request) {
        UserAgent agent = userAgentAnalyzer.parse(request.getHeader("User-Agent"));
//        return agent.getValue(UserAgent.OPERATING_SYSTEM_NAME) + " - " + agent.getValue(UserAgent.AGENT_NAME) + " - " + agent.getValue(UserAgent.DEVICE_NAME);
        return agent.getValue(UserAgent.AGENT_NAME) + " - " + agent.getValue(UserAgent.DEVICE_NAME);
    }
}

