package com.huasit.pm.system.security.model;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class UserWebAuthenticationDetails extends WebAuthenticationDetails {

    public UserWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
    }
}
