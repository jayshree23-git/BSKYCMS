/**
 * 
 */
package com.project.bsky.exception;

/**
 * @author ipsita.shaw
 *
 */
public class CPDClaimProcessingException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CPDClaimProcessingException(String message,  Throwable cause)
	{
		super(message,cause);
	}

}
