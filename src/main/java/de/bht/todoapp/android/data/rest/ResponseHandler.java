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
	T handleResponse(T response);
	
}
