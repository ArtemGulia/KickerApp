package com.g_art.kickerapp.utils;

import retrofit.RequestInterceptor;

/**
 * Kicker App
 * Created by G_Art on 19/2/2016.
 */
public class ApiHeaders implements RequestInterceptor {

    private String sessionId;

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void clearSessionId() {
        sessionId = null;
    }

    @Override
    public void intercept(RequestFacade request) {
        if (sessionId != null) {
            request.addHeader("", "");
        }

    }
}
