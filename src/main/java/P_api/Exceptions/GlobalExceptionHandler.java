package P_api.Exceptions;

import P_api.Exceptions.Erros.CampoVazio;
import P_api.Exceptions.Erros.FalhaRelacionamento;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {//usado sempre que algo quebrar uma regra de negocio



    @ExceptionHandler(FalhaRelacionamento.class)
    public ResponseEntity<String> handleFalhaRelacionamento(
            FalhaRelacionamento ex) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ex.getMessage());
    }
    @ExceptionHandler(CampoVazio.class)
    public ResponseEntity<String> handleCampoVazio(
            CampoVazio ex) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ex.getMessage());
    }
}