package org.farozy.utility;

import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.math.BigDecimal;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "twilio")
public class TwilioProperties {

    private String account_sid;
    private String auth_token;
    private String phone_number;

    public String getAccountSid() {
        return account_sid;
    }

    public void setAccountSid(String account_sid) {
        this.account_sid = account_sid;
    }

    public String getAuthToken() {
        return auth_token;
    }

    public void setAuthToken(String auth_token) {
        this.auth_token = auth_token;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }
}