package it.loreluc.sagraservice.error;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SagraExceptionHandler {

    @ExceptionHandler(SagraNotFoundException.class)
    public ResponseEntity<ErrorResource> handler(SagraNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResource.createError(e.getMessage())
        );
    }


    @ExceptionHandler(SagraConflictException.class)
    public ResponseEntity<ErrorResource> handler(SagraConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorResource.createError(e.getMessage(), e.getInvalidValues())
        );
    }

//    @ExceptionHandler(SagraUnexpectedErrorException.class)
//    public ResponseEntity<ErrorResource> handler(SagraUnexpectedErrorException e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                createErrorResource(e.getErrorCode(), e.getArgs())
//        );
//    }

    @ExceptionHandler(SagraBadRequestException.class)
    public ResponseEntity<ErrorResource> handler(SagraBadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResource.createError(e.getMessage(), e.getInvalidValues())
        );
    }

    @ExceptionHandler(SagraQuantitaNonSufficiente.class)
    public ResponseEntity<ErrorResourceNotEnoughQuantity> handler(SagraQuantitaNonSufficiente e) {
        return ResponseEntity.status(450)
                .body(ErrorResourceNotEnoughQuantity.createError(
                "Alcuni prodotti non sono vendibili o non hanno quantità sufficiente",
                        e.getInvalidProducts()
                ));
    }



//    @ExceptionHandler(SagraAuthorizationException.class)
//    public ResponseEntity<ErrorResource> handler(SagraAuthorizationException e) {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
//                createErrorResource(e.getErrorCode(), e.getArgs())
//        );
//    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResource> handler(MethodArgumentNotValidException ex) {
        final Set<InvalidValue> invalidValues = createWrongValue(ex.getBindingResult());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResource.createError("Richiesta non valida", invalidValues)
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResource> handler(MethodArgumentTypeMismatchException ex) {
        final Set<InvalidValue> invalidValues = Set.of(InvalidValue.builder().field(ex.getName()).value(ex.getValue()).message(ex.getMessage()).build());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResource.createError("Richiesta non valida", invalidValues)
        );
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResource> handler(BindException ex) {
        final Set<InvalidValue> invalidValues = createWrongValue(ex.getBindingResult());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResource.createError("Richiesta non valida", invalidValues)
        );
    }


    private Set<InvalidValue> createWrongValue(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .filter(FieldError.class::isInstance)
                .map(FieldError.class::cast)
                .map(e -> InvalidValue.builder().field(e.getField()).message(e.getDefaultMessage()).value(e.getRejectedValue()).build())
                .collect(Collectors.toSet());
    }
}
