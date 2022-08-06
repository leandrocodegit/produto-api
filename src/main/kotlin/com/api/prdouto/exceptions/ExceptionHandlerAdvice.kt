package com.api.prdouto.exceptions

import com.api.prdouto.enuns.CodeError
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.validation.UnexpectedTypeException

@ControllerAdvice
class ExceptionHandlerAdvice: ResponseEntityExceptionHandler() {

    @ExceptionHandler(EntityResponseException::class)
    fun handlerEntityNotFoundException(ex: EntityResponseException): ResponseEntity<ResponseError> {
        return ResponseEntity<ResponseError>(
                ResponseError(ex.message, ex.codeError.name, ex.codeError.value,listOf(ex.localizedMessage)),
                HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UnexpectedTypeException::class)
    fun handlerValidadeException(ex: UnexpectedTypeException): ResponseEntity<ResponseError> {
        return ResponseEntity<ResponseError>(
                ResponseError(
                        "Formato invalido",
                        CodeError.FORMAT_INVALID.name,
                        CodeError.FORMAT_INVALID.value,
                        listOf(ex.localizedMessage)
                ),
                HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    override fun handleMethodArgumentNotValid(
            ex: MethodArgumentNotValidException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        return ResponseEntity.badRequest().body(ResponseError(
                "Formato invalido",
                CodeError.FORMAT_INVALID.name,
                CodeError.FORMAT_INVALID.value,
                listOf(ex.localizedMessage)
        ))
    }
}