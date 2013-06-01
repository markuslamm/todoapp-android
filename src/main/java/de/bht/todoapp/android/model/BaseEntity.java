/**
 * 
 */
package de.bht.todoapp.android.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author markus
 *
 */
public abstract class BaseEntity implements Serializable
{
	@JsonIgnore
	private Long internalId;
	private Long entityId;

	/**
	 * @return
	 */
	public Long getInternalId() {
		return internalId;
	}

	/**
	 * @param internalId
	 */
	public void setInternalId(Long internalId) {
		this.internalId = internalId;
	}

	/**
	 * @return the remoteId
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * @param remoteId the remoteId to set
	 */
	public void setEntityId(Long remoteId) {
		this.entityId = remoteId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((internalId == null) ? 0 : internalId.hashCode());
		result = prime * result + ((entityId == null) ? 0 : entityId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseEntity other = (BaseEntity) obj;
		if (internalId == null) {
			if (other.internalId != null)
				return false;
		}
		else if (!internalId.equals(other.internalId))
			return false;
		if (entityId == null) {
			if (other.entityId != null)
				return false;
		}
		else if (!entityId.equals(other.entityId))
			return false;
		return true;
	}
	
	
}
