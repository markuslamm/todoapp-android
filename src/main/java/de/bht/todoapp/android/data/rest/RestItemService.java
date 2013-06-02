/**
 * 
 */
package de.bht.todoapp.android.data.rest;

import android.app.Activity;
import de.bht.todoapp.android.data.ItemService;
import de.bht.todoapp.android.data.rest.handler.ItemListHandler;
import de.bht.todoapp.android.data.rest.handler.ResponseHandler;
import de.bht.todoapp.android.model.TodoItem;
import de.bht.todoapp.android.model.TodoItemList;
import de.bht.todoapp.android.ui.base.BaseActivity;

/**
 * @author markus
 *
 */
public class RestItemService implements ItemService
{
	private static final String TAG = RestItemService.class.getSimpleName();

	private static final String APPLICATION_PATH = "/todoapp-server";
	private static final String AUTH_PATH = "/authenticate";
	private static final String ITEMS_URI = "/items";

	// protected String SERVER_ROOT = "http://10.0.2.2:8080";
	private static final String SERVER_ROOT = "http://192.168.2.100:8080";

	private BaseActivity context;

	public RestItemService(final BaseActivity context)
	{
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.bht.todoapp.android.data.ItemService#findAllItems()
	 */
	@Override
	public TodoItemList findAllItems() {
		final RestClient client = new RestClient(getEmail(), getPasswort());
		final TodoItemList response = client.findAllItems();
		final ResponseHandler<TodoItemList> handler = new ItemListHandler(((Activity) context).getContentResolver());
		final TodoItemList result = handler.handleResponse(response);
		return result;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.todoapp.android.data.ItemService#createItem(de.bht.todoapp.android
	 * .model.TodoItem)
	 */
	@Override
	public TodoItem createItem(final TodoItem item) {
		final RestClient client = new RestClient(getEmail(), getPasswort());
		final TodoItem newItem = client.createItem(item);
		return newItem;
	}

	public TodoItemList createItemList(final TodoItemList itemList) {
		final RestClient client = new RestClient(getEmail(), getPasswort());
		final TodoItemList list = client.createItemList(itemList);
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.todoapp.android.data.ItemService#updateItem(de.bht.todoapp.android
	 * .model.TodoItem)
	 */
	@Override
	public TodoItem updateItem(final TodoItem item) {
		final RestClient client = new RestClient(getEmail(), getPasswort());
		final TodoItem updatedItem = client.updateItem(item);
		updatedItem.setInternalId(item.getInternalId());
		return updatedItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.todoapp.android.data.ItemService#deleteItem(de.bht.todoapp.android
	 * .model.TodoItem)
	 */
	@Override
	public int deleteItem(final TodoItem item) {
		final RestClient client = new RestClient(getEmail(), getPasswort());
		final Integer deleteCount = client.deleteItem(item);
		return deleteCount;
	}


	private String getEmail() {
		final String email = context.getMainApplication().getPreferences().getString("email", "");
		return email;
	}

	private String getPasswort() {
		final String password = context.getMainApplication().getPreferences().getString("password", "");
		return password;
	}
}
