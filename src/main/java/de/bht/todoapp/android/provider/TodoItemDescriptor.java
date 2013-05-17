/**
 * 
 */
package de.bht.todoapp.android.provider;

import android.net.Uri;

/**
 * @author markus
 *
 */
public class TodoItemDescriptor
{
	 /* Used for the UriMacher */
 	public static final int ITEMS_CODE = 10;
 	public static final int ITEM_CODE = 20;
 	
	public static final String DB_NAME = "todo.db";
	public static final int DB_VERSION = 1;
	public static final String TABLE_NAME = "tbl_todoitems";
 	
 	public static final String ID_COLUMN = "_id";
 	public static final String SERVERID_COLUMN = "serverId";
 	public static final String TITLE_COLUMN = "title"; 
 	public static final String DESCRIPTION_COLUMN = "description";
 	public static final String STATUS_COLUMN = "status";
 	public static final String ISFAVOURITE_COLUMN = "is_favourite";
 	public static final String DUEDATE_COLUMN = "dueDate";
 	public static final String LATITUDE_COLUMN = "latitude";
 	public static final String LONGITUDE_COLUMN = "longitude";
 	
 	public static final String DEFAULT_SORT_ORDER = "dueDate ASC";
 	
 	public static final String AUTHORITY = "de.bht.todoapp.provider.items";
 	public static final String MIME_DIR_PREFIX = "vnd.android.cursor.dir";
	public static final String MIME_ITEM_PREFIX = "vnd.android.cursor.item";
	public static final String MIME_ITEM = "vnd.todoapp.items";
    public static final String PATH_SINGLE = "items/#";
    public static final String PATH_MULTIPLE = "items";
	public static final String MIME_TYPE_SINGLE = MIME_ITEM_PREFIX + "/" + MIME_ITEM;
	public static final String MIME_TYPE_MULTIPLE =	MIME_DIR_PREFIX + "/" + MIME_ITEM;
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + TodoItemDescriptor.AUTHORITY + "/" + TodoItemDescriptor.PATH_MULTIPLE);
	
	
	/* No instantiation allowed */
	private TodoItemDescriptor() { }
}
