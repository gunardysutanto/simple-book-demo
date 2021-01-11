package book.exception.reporter;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorReporter {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String errorCode;
    private final String errorMessage;
}
