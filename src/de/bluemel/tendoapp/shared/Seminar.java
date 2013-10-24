package de.bluemel.tendoapp.shared;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Seminar implements Serializable {

	private static final long serialVersionUID = 1L;

	// does not compile on the client
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	// private com.google.appengine.api.datastore.Key key;
	@Id
	private String key;

	@Basic
	private Date firstDay;

	@Basic
	private Date lastDay;

	@Basic
	private String title;

	@Basic
	private String instructor;

	@Basic
	private String organizer;

	@Basic
	private String location;

	@Basic
	private String announcement;

	public Seminar() {
	}

	public Seminar(final Seminar orig) {
		this.key = orig.getKey();
		this.firstDay = orig.getFirstDay();
		this.lastDay = orig.getLastDay();
		this.title = orig.getTitle();
		this.instructor = orig.getInstructor();
		this.organizer = orig.getOrganizer();
		this.location = orig.getLocation();
		this.announcement = orig.getAnnouncement();
	}

	public Seminar(Date firstDay, Date lastDay, String title, String instructor, String organizer, String location,
			String announcement) {
		this.firstDay = new Date(firstDay.getTime());
		this.lastDay = new Date(lastDay.getTime());
		this.title = title;
		this.instructor = instructor;
		this.organizer = organizer;
		this.location = location;
		this.announcement = announcement;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Date getFirstDay() {
		return new Date(firstDay.getTime());
	}

	public void setFirstDay(Date firstDay) {
		this.firstDay = new Date(firstDay.getTime());
	}

	public Date getLastDay() {
		return new Date(lastDay.getTime());
	}

	public void setLastDay(Date lastDay) {
		this.lastDay = new Date(lastDay.getTime());
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInstructor() {
		return instructor;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}
}
