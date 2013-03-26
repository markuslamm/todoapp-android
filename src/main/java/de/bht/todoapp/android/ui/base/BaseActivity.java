/**
 * 
 */
package de.bht.todoapp.android.ui.base;

import de.bht.todoapp.android.MainApplication;

/**
 * @author markus
 *
 */
public interface BaseActivity
{
    /**
     * @return
     */
    MainApplication getMainApplication();

    /**
     * 
     */
    void displayNetworkError();

    /**
     * 
     */
    void displayAuthorizationError();

    /**
     * @param message
     */
    void showProgressDialog(String message);

    /**
     * 
     */
    void dismissProgressDialog();
}
