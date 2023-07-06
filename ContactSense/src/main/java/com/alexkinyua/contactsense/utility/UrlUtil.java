package com.alexkinyua.contactsense.utility;

import jakarta.servlet.http.HttpServletRequest;
/**
  * @Author: Alex Kinyua
  */
public class UrlUtil {
    public static String getApplicationUrl(HttpServletRequest request){
        String appUrl = request.getRequestURL().toString();
        return  appUrl.replace(request.getServletPath(), "");
    }
}
