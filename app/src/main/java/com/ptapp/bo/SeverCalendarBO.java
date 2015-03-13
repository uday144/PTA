package com.ptapp.bo;

//Names of JSON fields and this class variable names must match.
public class SeverCalendarBO {
	// private ServerEventsBO VCALENDAR;
	private EventsBO VCALENDAR;

	public EventsBO getVCALENDAR() {
		return VCALENDAR;
	}

	public void setVCALENDAR(EventsBO vCALENDAR) {
		VCALENDAR = vCALENDAR;
	}

}