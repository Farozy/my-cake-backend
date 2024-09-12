package org.farozy.exception;

import javax.naming.AuthenticationException;

public class OAuth2AuthenticationProcessingException extends AuthenticationException {

    public OAuth2AuthenticationProcessingException(String msg) {
        super(msg);
    }

}
