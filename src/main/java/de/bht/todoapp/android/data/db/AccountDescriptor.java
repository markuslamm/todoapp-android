/**
 * 
 */
package de.bht.todoapp.android.data.db;

import android.net.Uri;

/**
 * @author markus
 *
 */
public class AccountDescriptor
{
	 /* Used for the UriMacher */
 	public static final int ACCOUNTS_CODE = 10;
 	public static final int ACCOUNT_CODE = 20;
 	
 	public static final String DB_NAME = "todo.db";
	public static final int DB_VERSION = 1;
	public static final String TABLE_NAME = "tbl_accounts";
	
	public static final String ID_COLUMN = "_id";
 	public static final String SERVERID_COLUMN = "serverId";
 	public static final String EMAIL_COLUMN = "email"; 
 	public static final String PASSWORD_COLUMN = "password";
 	
 	public static final String DEFAULT_SORT_ORDER = "email ASC";
 	
 	public static final String AUTHORITY = "de.bht.todoapp.provider.accounts";
 	public static final String MIME_DIR_PREFIX = "vnd.android.cursor.dir";
	public static final String MIME_ITEM_PREFIX = "vnd.android.cursor.item";
	public static final String MIME_ITEM = "vnd.todoapp.accounts";
    public static final String PATH_SINGLE = "accounts/#";
    public static final String PATH_MULTIPLE = "accounts";
	public static final String MIME_TYPE_SINGLE = MIME_ITEM_PREFIX + "/" + MIME_ITEM;
	public static final String MIME_TYPE_MULTIPLE =	MIME_DIR_PREFIX + "/" + MIME_ITEM;
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AccountDescriptor.AUTHORITY + "/" + AccountDescriptor.PATH_MULTIPLE);
	
	/* No instantiation allowed */
	private AccountDescriptor()
	{
	}
}
