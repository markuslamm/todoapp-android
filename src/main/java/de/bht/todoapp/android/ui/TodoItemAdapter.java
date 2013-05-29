/**
 * 
 */
package de.bht.todoapp.android.ui;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.bht.todoapp.android.R;
import de.bht.todoapp.android.model.TodoItem;
import de.bht.todoapp.android.util.DateHelper;

/**
 * @author markus
 *
 */
public class TodoItemAdapter extends ArrayAdapter<TodoItem>
{
	private Activity context;
	private TodoItem[] items;

	/**
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public TodoItemAdapter(Activity context, List<TodoItem> objects)
	{
		super(context, R.layout.item_row, objects);
		this.context = context;
		this.items = objects.toArray(new TodoItem[objects.size()]);
	}

	/* Helper class to hold references to view objects */
	static class ViewHolder
	{
		TextView txtTitle;
		TextView txtDueDate;
		TextView txtStatus;
		TextView txtPriority;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			final LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.item_row, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.txtTitle = (TextView) rowView.findViewById(R.id.title);
			viewHolder.txtDueDate = (TextView) rowView.findViewById(R.id.dueDateTime);
			viewHolder.txtStatus = (TextView) rowView.findViewById(R.id.status);
			viewHolder.txtPriority = (TextView) rowView.findViewById(R.id.txtPriority);
			rowView.setTag(viewHolder);
		}
		final TodoItem item = items[position];
		final ViewHolder viewHolder = (ViewHolder) rowView.getTag();
		viewHolder.txtTitle.setText(item.getTitle());
		final Long dateInMillis = item.getDueDate();
		viewHolder.txtDueDate.setText(DateHelper.generateDateTimeString(dateInMillis));
		viewHolder.txtStatus.setText(item.getStatus().toString());
		viewHolder.txtPriority.setText(item.getPriority().toString());
		return rowView;
	}

}
