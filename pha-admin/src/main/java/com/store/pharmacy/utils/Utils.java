package com.store.pharmacy.utils;

import javax.servlet.http.HttpServletRequest;

public class Utils {
    public static String getBaseURL(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        StringBuffer url =  new StringBuffer();
        url.append(scheme).append("://").append(serverName);
        if ((serverPort != 80) && (serverPort != 443)) {
            url.append(":").append(serverPort);
        }
        url.append(contextPath);
        if(url.toString().endsWith("/")){
            url.append("/");
        }
        return url.toString();
    }

    public static final String URL = "http://localhost:8080/";

	public static String getTimeZone() {
		return "Asia/Ho_Chi_Minh";
	}
}
