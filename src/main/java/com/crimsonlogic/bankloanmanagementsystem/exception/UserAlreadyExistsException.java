package com.crimsonlogic.bankloanmanagementsystem.exception;
public class UserAlreadyExistsException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2155145688312342942L;

	public UserAlreadyExistsException(String message) {
        super(message);
    }
}
