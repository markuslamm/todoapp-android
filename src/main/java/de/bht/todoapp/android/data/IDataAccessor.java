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
public interface IDataAccessor
{
	TodoItemList findAllItems();

	TodoItem createItem(TodoItem item);

	TodoItem updateItem(TodoItem item);

	int deleteItem();
}
