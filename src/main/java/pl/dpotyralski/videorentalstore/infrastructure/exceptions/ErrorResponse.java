package pl.dpotyralski.videorentalstore.infrastructure.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class ErrorResponse {

    private int code;
    private String message;

}
