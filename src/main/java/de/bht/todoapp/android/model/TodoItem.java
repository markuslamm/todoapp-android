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
	public enum Status {
		OPEN, CLOSED;
	}

	public enum Priority {
		HIGH, MEDIUM, LOW;
	}

	private String title;
	private String description;
	private Long dueDate;
	private double longitude;
	private double latitude;
	private Priority priority;
	private Status status;

	public TodoItem()
	{
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
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
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the dueDate
	 */
	public Long getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate
	 *            the dueDate to set
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
	 * @param longitude
	 *            the longitude to set
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
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(final double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the priority
	 */
	public Priority getPriority() {
		return priority;
	}

	/**
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority(final Priority priority) {
		this.priority = priority;
	}

	public static Status getStatusFromString(final String status) {
		Status result = null;
		if (status.equals(Status.OPEN.toString())) {
			result = Status.OPEN;
		}
		else if (status.equals(Status.CLOSED.toString())) {
			result = Status.CLOSED;
		}
		else {
			throw new RuntimeException("Unknown status string. Unable to create Status");
		}
		return result;
	}

	public static Priority getPriorityFromString(final String priority) {
		Priority result = null;
		if (priority.equals(Priority.HIGH.toString())) {
			result = Priority.HIGH;
		}
		else if (priority.equals(Priority.MEDIUM.toString())) {
			result = Priority.MEDIUM;
		}
		else if (priority.equals(Priority.LOW.toString())) {
			result = Priority.LOW;
		}
		else {
			throw new RuntimeException("Unknown priority string. Unable to create Priority");
		}
		return result;
	}

	public static int getSpinnerPositionFromPriority(final Priority priority) {
		int result;
		switch (priority) {
			case HIGH:
				result = 0;
				break;
			case MEDIUM:
				result = 1;
				break;
			case LOW:
				result = 2;
				break;
			default:
				throw new RuntimeException("Unknown Priority. Unable to create spinner position");
		}
		return result;
	}
}
