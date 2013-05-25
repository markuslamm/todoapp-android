package de.bht.todoapp.android.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Patterns;

/**
 * Project: android-client Package: net.findme.android.util Filename:
 * 
 * import android.util.Patterns;
 * 
 * /**
 * 
 * @author Markus Lamm
 * 
 */
public class Validator
{
	
	private Validator() { }
    /**
     * @param password
     * @return
     */
    public static final boolean isValidPassword(final String password) {
    	final Pattern pattern = Pattern.compile("\\d{6}");
        final Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * @param password
     * @param confirmation
     * @return
     */
    public static final boolean isPasswordConfirmed(final String password, final String passwordConfirm) {
        return password.equals(passwordConfirm);
    }

    /**
     * @param emailString
     * @return
     */
    public static final boolean isValidEmail(final String email) {
        return (email.trim() == null) ? false : Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
