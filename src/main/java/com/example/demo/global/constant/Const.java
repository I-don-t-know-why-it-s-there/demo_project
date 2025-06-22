package com.example.demo.global.constant;

public class Const {
    private Const() {}

    public static final String USER_BASE_URL = "/user-service/user";
    public static final String USER_CREATE_URL = USER_BASE_URL + "/create";
    public static final String USER_LOGIN_URL = USER_BASE_URL + "/login";
    public static final String USER_UPDATE_URL = USER_BASE_URL + "/update";
    public static final String USER_FIND_URL = USER_BASE_URL + "/info";
    public static final String USER_DELETE_URL = USER_BASE_URL + "/delete";

    public static long TOKEN_VALIDITY_IN_MS = 86400000L;
    public static String Token_Prefix = "Bearer ";
    public static String AUTHORIZATION_HEADER = "Authorization";
}
