/**
 * 
 */
package de.bht.todoapp.android.data.rest.handler;

/**
 * @author markus
 *
 */
public interface ResponseHandler<T>
{
	T handleResponse(T response);
}
