/**
 * 
 */
package de.bht.todoapp.android.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import de.bht.todoapp.android.R;
import de.bht.todoapp.android.data.db.TodoItemDescriptor;
import de.bht.todoapp.android.util.DateHelper;

/**
 * @author markus
 * 
 */
public class ItemAdapter extends CursorAdapter
{
	private final LayoutInflater inflater;

	/**
	 * @param context
	 * @param c
	 */
	public ItemAdapter(Context context, Cursor c)
	{
		super(context, c);
		inflater = LayoutInflater.from(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.CursorAdapter#bindView(android.view.View,
	 * android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		final TextView txtTitle = (TextView) view.findViewById(R.id.title);
		final TextView txtDueDate = (TextView) view.findViewById(R.id.dueDateTime);
		final TextView txtStatus = (TextView) view.findViewById(R.id.status);
		final TextView txtPriority = (TextView) view.findViewById(R.id.txtPriority);
		
		txtTitle.setText(cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.TITLE_COLUMN)));
		final Long dateInMillis = cursor.getLong(cursor.getColumnIndex(TodoItemDescriptor.DUEDATE_COLUMN));
		txtDueDate.setText(DateHelper.generateDateTimeString(dateInMillis));
		final String status = cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.STATUS_COLUMN));
		txtStatus.setText(status);
		final String priority = cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.PRIORITY_COLUMN));
		txtPriority.setText(priority);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.CursorAdapter#newView(android.content.Context,
	 * android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View view = inflater.inflate(R.layout.item_row, parent, false);
		return view;
	}
	
	
}
