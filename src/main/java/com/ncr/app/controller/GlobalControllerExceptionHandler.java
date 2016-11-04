package com.ncr.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.ncr.app.enumeration.ErrorMessage;
import com.ncr.app.exception.GeoCodingException;
import com.ncr.app.models.ApiErrorResponse;

@ControllerAdvice
@Controller
public class GlobalControllerExceptionHandler {

	@ExceptionHandler(value = { GeoCodingException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ApiErrorResponse handleGeoCodingExcpetion(Exception ex, WebRequest req) {
		ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
		apiErrorResponse.setDescription(ex.getMessage());
		apiErrorResponse.setErrorMessage(ErrorMessage.ProcessingError);
		return apiErrorResponse;

	}

	@ExceptionHandler(value = { MissingServletRequestParameterException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ApiErrorResponse handleMissingParamsException(Exception ex, WebRequest req) {
		ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
		apiErrorResponse.setDescription("Missing required params");
		apiErrorResponse.setErrorMessage(ErrorMessage.MissingParams);
		return apiErrorResponse;

	}


	@ExceptionHandler(value = { Exception.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ApiErrorResponse unknownException(Exception ex, WebRequest req) {
		ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
		if (ex instanceof MethodArgumentTypeMismatchException) {
			apiErrorResponse.setErrorMessage(ErrorMessage.InValidParamsError);			
		}
		else {
			apiErrorResponse.setErrorMessage(ErrorMessage.UnKnownError);
		}
		apiErrorResponse.setDescription(ex.getMessage());
		return apiErrorResponse;

	}	
	
}