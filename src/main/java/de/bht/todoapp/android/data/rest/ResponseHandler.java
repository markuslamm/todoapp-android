/**
 * 
 */
package de.bht.todoapp.android.data.rest;




/**
 * @author markus
 *
 */
public interface ResponseHandler<T>
{
	void handleResponse(T response);
	
}
