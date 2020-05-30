package com.medestin.ftp.client.connection;

import java.util.Objects;
import java.util.Optional;

public class ServerResponse {

    private final int code;
    private final String message;
    private final String body;

    private ServerResponse(int code, String message, String body){
        this.code = code;
        this.message = message;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public Optional<String> getMessage() {
        return Optional.ofNullable(message);
    }

    public Optional<String> getBody() {
        return Optional.ofNullable(body);
    }

    public static class ServerResponseBuilder{
        private Integer code;
        private String message;
        private String body;

        public ServerResponseBuilder() {
        }

        public ServerResponseBuilder code(Integer code) {
            this.code = code;
            return this;
        }

        public ServerResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ServerResponseBuilder body(String body) {
            this.body = body;
            return this;
        }

        public ServerResponse build() {
            Objects.requireNonNull(code, "Code must be provided");
            return new ServerResponse(code, message, body);
        }
    }
}
