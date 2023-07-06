package br.com.erudio.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequiredObjectsIsNullException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public RequiredObjectsIsNullException() {
		super("Não é permnitido persistir um objeto nulo!");
	}
	
	public RequiredObjectsIsNullException(String ex) {
		super(ex);
	}
}
