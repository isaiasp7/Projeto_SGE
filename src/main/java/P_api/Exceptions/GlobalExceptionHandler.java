package P_api.Exceptions;

import P_api.Exceptions.Erros.AlunoNaoEncontrado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {//usado sempre que algo quebrar uma regra de negocio

    @ExceptionHandler(AlunoNaoEncontrado.class)
    public ResponseEntity<String> handleProdutoNaoEncontrado(
            AlunoNaoEncontrado ex) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ex.getMessage());
    }
}