package com.ptapp.bo;

import com.ptapp.io.model.Event;

import java.util.ArrayList;

/** Stores list of Events. */
public class EventsBO {

	// private ArrayList<EventBO> lstEvents;
	// 'VEVENT' name should be same as in JSON response.
	private ArrayList<Event> VEVENT;
	private String VERSION;
	private String PRODID;

	public EventsBO() {

	}

	public ArrayList<Event> getLstEvents() {
		return VEVENT;
	}

	public void setLstEvents(ArrayList<Event> vEVENT) {
		VEVENT = vEVENT;
	}

	public String getVersion() {
		return VERSION;
	}

	public void setVersion(String version) {
		this.VERSION = version;
	}

	public String getPRODID() {
		return PRODID;
	}

	public void setPRODID(String pRODID) {
		PRODID = pRODID;
	}

}
