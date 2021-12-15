package be.zwoop.web.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;


@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class RequestParamException extends Exception {
    private String message;
}
