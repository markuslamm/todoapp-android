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
}
