package com.antonio.wallettransfer.integration.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthorizerResponse {

    private String status;
    private Data data;

    public static class Data {
        private Boolean authorization;

        public Boolean getAuthorization() {
            return authorization;
        }

        public void setAuthorization(Boolean authorization) {
            this.authorization = authorization;
        }
    }

    public Boolean isAuthorized() {
        return data != null && Boolean.TRUE.equals(data.authorization);
    }

}
