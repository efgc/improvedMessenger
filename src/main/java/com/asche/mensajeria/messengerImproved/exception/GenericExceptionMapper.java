package com.asche.mensajeria.messengerImproved.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.asche.mensajeria.messengerImproved.model.ErrorMessage;
@Provider
public class GenericExceptionMapper implements ExceptionMapper<GenericException>{

	@Override
	public Response toResponse(GenericException exception) {
		ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(),exception.getErrorCode(),"http://www.google.com");
		
		return Response.status(exception.getErrorCode()).entity(errorMessage).build();
	}

}
