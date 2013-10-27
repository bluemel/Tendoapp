package de.bluemel.tendoapp.shared;

import java.io.Serializable;
import java.util.Date;

public class SeminarDTO extends Seminar implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key;

	private Date firstDay;

	private Date lastDay;

	private String title;

	private String instructor;

	private String organizer;

	private String location;

	private String announcement;

	public SeminarDTO() {
	}

	public SeminarDTO(final Seminar orig) {
		init(orig);
	}

	public SeminarDTO(String key, Date firstDay, Date lastDay, String title, String instructor, String organizer,
			String location, String announcement) {
		init(key, firstDay, lastDay, title, instructor, organizer, location, announcement);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public Date getFirstDay() {
		return new Date(firstDay.getTime());
	}

	@Override
	public void setFirstDay(Date firstDay) {
		this.firstDay = new Date(firstDay.getTime());
	}

	@Override
	public Date getLastDay() {
		return new Date(lastDay.getTime());
	}

	@Override
	public void setLastDay(Date lastDay) {
		this.lastDay = new Date(lastDay.getTime());
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getInstructor() {
		return instructor;
	}

	@Override
	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	@Override
	public String getOrganizer() {
		return organizer;
	}

	@Override
	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

	@Override
	public String getLocation() {
		return location;
	}

	@Override
	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String getAnnouncement() {
		return announcement;
	}

	@Override
	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}
}
