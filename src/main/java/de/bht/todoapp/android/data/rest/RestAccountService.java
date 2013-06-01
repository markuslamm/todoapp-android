/**
 * 
 */
package de.bht.todoapp.android.data.rest;

import de.bht.todoapp.android.AuthenticationException;
import de.bht.todoapp.android.data.AccountService;
import de.bht.todoapp.android.model.Account;

/**
 * @author markus
 *
 */
public class RestAccountService implements AccountService
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.todoapp.android.data.AccountService#authenticateAccount(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public Account authenticateAccount(final String email, final String password) throws AuthenticationException {
		final RestClient client = new RestClient(email, password);
		final Account account = client.authenticateUser();
		return account;
	}

}
