/**
 * 
 */
package de.bht.todoapp.android.model;

/**
 * @author markus
 *
 */
public abstract class BaseEntity
{
	private Long entityId;

	/**
	 * @return the entityId
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
}
