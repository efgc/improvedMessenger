package com.asche.mensajeria.messengerImproved.exception;

public class GenericException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 297938253040157381L;
	private int errorCode;
	public GenericException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	
	public int getErrorCode() {
		return this.errorCode;
	}

}
