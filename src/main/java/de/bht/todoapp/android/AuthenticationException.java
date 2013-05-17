/**
 * 
 */
package de.bht.todoapp.android;

/**
 * @author markus
 *
 */
public class AuthenticationException extends Exception
{
	/**
	 * 
	 */
	public AuthenticationException() { }
	
	/**
	 * @param msg
	 */
	public AuthenticationException(final String msg)
	{
		super(msg);
	}
}
