package com.example.documentstorage.shared.base.problemdetail;

import com.example.documentstorage.shared.base.apimessages.LocalizationService;
import com.example.documentstorage.shared.base.exceptions.ApiException;
import com.example.documentstorage.shared.base.exceptions.ApiExceptionError;
import com.example.documentstorage.shared.base.exceptions.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProblemDetailMapperImpl implements ProblemDetailMapper {
    private static final int STATUS_CODE = 400;
    private static final Logger logger = LoggerFactory.getLogger(ProblemDetailMapperImpl.class);
    private final LocalizationService localizationService;

    public ProblemDetailMapperImpl(LocalizationService localizationService) {
        this.localizationService = localizationService;
    }

    @Override
    public ProblemDetail mapToProblemDetail(BindingResult result) {
        String typeName = Optional.ofNullable(result.getTarget())
                .map(Object::getClass)
                .map(Class::getName)
                .orElse("");

        logger.warn("Validation failed for type '{}'", typeName);

        ErrorMessage errorMessage = localizationService.getErrorMessage("validation.error.message");
        List<ApiExceptionError> errorMessages = result.getAllErrors().stream()
                //ToDo fix arguments for example map {max} to 20
                .map(e -> localizationService.getErrorMessage(e.getDefaultMessage()))
                .map(message -> {
                    ApiExceptionError mock = new Mock(message);
                    return mock;
                })
                .toList();

        ProblemDetail problemDetail = ProblemDetail.forStatus(STATUS_CODE);
        problemDetail = fillProblemDetails(problemDetail, errorMessage, errorMessages);
        return problemDetail;
    }

    @Override
    public ProblemDetail fillProblemDetails(ProblemDetail problemDetail, ErrorMessage errorMessage, List<ApiExceptionError> errors)  {
        problemDetail.setProperty("code", errorMessage.code());
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("errors", "ex.getErrors()");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }
}

//ToDo this is very bad
class Mock implements ApiExceptionError {

    private final ErrorMessage message;

    public Mock(ErrorMessage message) {
        this.message = message;
    }
    @Override
    public String getCode() {
        return message.code();
    }

    /**
     * @return gets the title of the error
     */
    @Override
    public String getTitle() {
        return message.title();
    }

    /**
     * @return gets the detail of the error
     */
    @Override
    public String getDetail() {
        return message.detail();
    }
}
