package br.com.alura.forum.config.validacao;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErroDeValidacaoHandler {

	//@Autowired
	//private MessageSource messageSource; 
	
	@ResponseStatus(code =  HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<ErroDeFormularioDTO> validation(MethodArgumentNotValidException ex) {
		
		List<ErroDeFormularioDTO> erroDTO = new ArrayList<ErroDeFormularioDTO>(); 
		
		List<FieldError> fieldErrors =  ex.getBindingResult().getFieldErrors(); 
		fieldErrors.forEach(e ->{
			//String message = messageSource.getMessage(e, LocaleContextHolder.getLocale()); // getDefaultMessage Retornou a mesma mesagem do obj messageSource. 
			ErroDeFormularioDTO erro = new ErroDeFormularioDTO(e.getField(), e.getDefaultMessage());  
			erroDTO.add(erro); 
		});
		
		return erroDTO; 
	}
}
