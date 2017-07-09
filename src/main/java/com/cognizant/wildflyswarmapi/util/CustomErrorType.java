package com.cognizant.wildflyswarmapi.util;

/**
 * Created by Koneru on 7/6/17.
 */
public class CustomErrorType {

    private String errorMessage;

    public CustomErrorType(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
