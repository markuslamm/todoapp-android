/**
 * 
 */
package de.bht.todoapp.android.model;

/**
 * @author markus
 *
 */
public class TodoItem extends BaseEntity
{
	public enum Status { OPEN, CLOSED }
	
	private String title;
	private String description;

	private String status;
	private boolean isFavourite;

	private Long dueDate;
	private double longitude;
	private double latitude;
	
	public TodoItem() { }

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(final String status) {
		this.status = status;
	}

	/**
	 * @return the isFavourite
	 */
	public boolean isFavourite() {
		return isFavourite;
	}

	/**
	 * @param isFavourite the isFavourite to set
	 */
	public void setFavourite(final boolean isFavourite) {
		this.isFavourite = isFavourite;
	}

	/**
	 * @return the dueDate
	 */
	public Long getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(final Long dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(final double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(final double latitude) {
		this.latitude = latitude;
	}
}
