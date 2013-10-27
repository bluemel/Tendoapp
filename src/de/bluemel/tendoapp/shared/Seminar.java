package de.bluemel.tendoapp.shared;

import java.util.Date;

/**
 * Abstract class that keeps the persistent object (PO) and the data transfer object (DTO) together.
 * Basically the GWT app would also work with the PO but with the separation we gain more flexibility
 * - we could use the gwt-syncproxy library in order to access the app's server by any Java application (e. g. Android)
 * - we could use special classes / annotations (e. g. @GeneratedValue(strategy = GenerationType.IDENTITY)) that do not compile on a GWT client
 * 
 * @author Bluemel1.Martin
 */
public abstract class Seminar {

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getClass().getName()).append('[');
		add(sb, "key", getKey(), true);
		add(sb, "firstDay", TimeLogic.format(getFirstDay()), false);
		add(sb, "lastDay", TimeLogic.format(getLastDay()), false);
		add(sb, "title", getTitle(), false);
		add(sb, "instructor", getInstructor(), false);
		add(sb, "organizer", getOrganizer(), false);
		add(sb, "location", getLocation(), false);
		add(sb, "announcement", getAnnouncement(), false);
		sb.append(']');
		return sb.toString();
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Seminar)) {
			return false;
		}
		return getKey().equals(((Seminar) other).getKey());
	}

	@Override
	public int hashCode() {
		return getKey().hashCode();
	}

	private void add(final StringBuilder sb, final String name, final String value, final boolean first) {
		if (!first) {
			sb.append(", ");
		}
		if (value == null || value.trim().length() == 0) {
			sb.append(name).append(" = <no ").append(name).append('>');
		} else {
			sb.append(name).append(" = \"").append(value).append('\"');
		}
	}

	protected void init(Seminar seminar) {
		setKey(seminar.getKey());
		setFirstDay(new Date(seminar.getFirstDay().getTime()));
		setLastDay(new Date(seminar.getLastDay().getTime()));
		setTitle(seminar.getTitle());
		setInstructor(seminar.getInstructor());
		setOrganizer(seminar.getOrganizer());
		setLocation(seminar.getLocation());
		setAnnouncement(seminar.getAnnouncement());
	}

	protected void init(String key, Date firstDay, Date lastDay, String title, String instructor, String organizer,
			String location, String announcement) {
		setKey(key);
		setFirstDay(new Date(firstDay.getTime()));
		setLastDay(new Date(lastDay.getTime()));
		setTitle(title);
		setInstructor(instructor);
		setOrganizer(organizer);
		setLocation(location);
		setAnnouncement(announcement);
	}

	public abstract String getKey();

	public abstract void setKey(String key);

	public abstract Date getFirstDay();

	public abstract void setFirstDay(Date firstDay);

	public abstract Date getLastDay();

	public abstract void setLastDay(Date lastDay);

	public abstract String getTitle();

	public abstract void setTitle(String title);

	public abstract String getInstructor();

	public abstract void setInstructor(String instructor);

	public abstract String getOrganizer();

	public abstract void setOrganizer(String organizer);

	public abstract String getLocation();

	public abstract void setLocation(String location);

	public abstract String getAnnouncement();

	public abstract void setAnnouncement(String announcement);
}
