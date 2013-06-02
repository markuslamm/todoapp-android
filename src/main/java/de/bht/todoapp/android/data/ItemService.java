/**
 * 
 */
package de.bht.todoapp.android.data;

import de.bht.todoapp.android.model.TodoItem;
import de.bht.todoapp.android.model.TodoItemList;

/**
 * @author markus
 * 
 */
public interface ItemService
{
	TodoItemList findAllItems();

	TodoItem createItem(TodoItem item);

	TodoItemList createItemList(TodoItemList list);

	TodoItem updateItem(TodoItem item);

	int deleteItem(TodoItem item);
}
