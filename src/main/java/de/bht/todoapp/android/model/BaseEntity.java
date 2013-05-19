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
	private Long internalId;
	private Long remoteId;


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
	public Long getRemoteId() {
		return remoteId;
	}

	/**
	 * @param remoteId the remoteId to set
	 */
	public void setRemoteId(Long remoteId) {
		this.remoteId = remoteId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((internalId == null) ? 0 : internalId.hashCode());
		result = prime * result + ((remoteId == null) ? 0 : remoteId.hashCode());
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
		if (remoteId == null) {
			if (other.remoteId != null)
				return false;
		}
		else if (!remoteId.equals(other.remoteId))
			return false;
		return true;
	}
	
	
}
