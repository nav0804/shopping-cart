package com.nav.shoppingcart.response;

import lombok.*;

@Getter
@Setter

public class ApiResponse {
    private String message;
    private Object data;

    public ApiResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}
