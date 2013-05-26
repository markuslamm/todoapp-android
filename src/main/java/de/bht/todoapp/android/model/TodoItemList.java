/**
 * Project: 	todoapp-server
 * Package:		de.bht.todoapp.server.domain
 * Filename:	TodoItemList.java
 * Timestamp:	25.05.2013 | 14:42:16
 */
package de.bht.todoapp.android.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author markus
 * 
 */
public class TodoItemList
{
    private List<TodoItem> items = new ArrayList<TodoItem>();

    public TodoItemList(final List<TodoItem> items)
    {
        this.items = items;
    }
    
    public TodoItemList() { }
    
    
    public List<TodoItem> getItems() {
        return items;
    }
}
