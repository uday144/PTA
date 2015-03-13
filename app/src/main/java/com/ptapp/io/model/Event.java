package com.ptapp.io.model;

/**
 * This class is our model and contains the data we will save in the database
 * and show in the user interface.
 */
public class Event {
	// private variables
	public String evtType;
    public String id; // PK - ServerEventId
    public String forClasses; // JSON returned classes from server
    public String eventTitle;
    public String eventDescription;
    public long calId;
    public String startDatetime; //expected format: yyyy-MM-dd HH:MM:SS
    public String endDatetime;   //expected format: yyyy-MM-dd HH:MM:SS
    public long androidEventId; // not in json response

	// Empty constructor
	public Event() {

	}

	public String getEvtType() {
		return evtType;
	}

	public void setEvtType(String evtType) {
		this.evtType = evtType;
	}

	public String getServerEventId() {
		return id;
	}

	public void setId(String serverEventId) {
		this.id = serverEventId;
	}

	public String getForClasses() {
		return forClasses;
	}

	public void setForClasses(String forClasses) {
		this.forClasses = forClasses;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public long getCalId() {
		return calId;
	}

	public void setCalId(long calId) {
		this.calId = calId;
	}

	public String getStartDatetime() {
		return startDatetime;
	}

	public void setStartDatetime(String startDatetime) {
		this.startDatetime = startDatetime;
	}

	public String getEndDatetime() {
		return endDatetime;
	}

	public void setEndDatetime(String endDatetime) {
		this.endDatetime = endDatetime;
	}

	public long getAndroidEventId() {
		return androidEventId;
	}

	public void setAndroidEventId(long androidEventId) {
		this.androidEventId = androidEventId;
	}

}
