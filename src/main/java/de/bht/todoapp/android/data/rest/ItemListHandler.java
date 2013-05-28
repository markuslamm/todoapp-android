/**
 * 
 */
package de.bht.todoapp.android.data.rest;

import android.util.Log;
import de.bht.todoapp.android.model.TodoItemList;
import de.bht.todoapp.android.ui.base.BaseActivity;

/**
 * @author markus
 *
 */
public class ItemListHandler implements ResponseHandler<TodoItemList>
{
	private static final String TAG = ItemListHandler.class.getSimpleName();
	private BaseActivity context;

	public ItemListHandler(final BaseActivity context)
	{
		this.context = context;
	}
	/* (non-Javadoc)
	 * @see de.bht.todoapp.android.data.rest.ResponseHandler#handleResponse(org.springframework.http.ResponseEntity)
	 */
	@Override
	public void handleResponse(final TodoItemList responseEntity) {
		Log.d(TAG, "handle TodoItemList response.");
	}


}
