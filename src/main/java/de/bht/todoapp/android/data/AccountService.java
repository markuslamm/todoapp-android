/**
 * 
 */
package de.bht.todoapp.android.data;

import de.bht.todoapp.android.AuthenticationException;
import de.bht.todoapp.android.model.Account;

/**
 * @author markus
 *
 */
public interface AccountService
{
	/**
	 * @param email
	 * @param password
	 * @return
	 */
	Account authenticateAccount(String email, String password) throws AuthenticationException;
}
