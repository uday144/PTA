package com.ptapp.bean;

/**
 * This class is our model and contains the data we will save in the database
 * and show in the user interface.
 */
public class TransportBean {
	// private variables
	private String id;
	private String RouteName;
	private String StopName;
	private String ZoneName;
	private String way;
	private String SPOCPhoneNumber;
	private String VehicleNumber;
	private String DriverFName;
	private String DriverLName;
	private String DriverPhoneNumber;
	private String ConductorFName;
	private String ConductorLName;
	private String ConductorPhoneNumber;

	// Empty constructor
	public TransportBean() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRouteName() {
		return RouteName;
	}

	public void setRouteName(String routeName) {
		RouteName = routeName;
	}

	public String getStopName() {
		return StopName;
	}

	public void setStopName(String stopName) {
		StopName = stopName;
	}

	public String getZoneName() {
		return ZoneName;
	}

	public void setZoneName(String zoneName) {
		ZoneName = zoneName;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	public String getSPOCPhoneNumber() {
		return SPOCPhoneNumber;
	}

	public void setSPOCPhoneNumber(String sPOCPhoneNumber) {
		SPOCPhoneNumber = sPOCPhoneNumber;
	}

	public String getVehicleNumber() {
		return VehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		VehicleNumber = vehicleNumber;
	}

	public String getDriverFName() {
		return DriverFName;
	}

	public void setDriverFName(String driverFName) {
		DriverFName = driverFName;
	}

	public String getDriverLName() {
		return DriverLName;
	}

	public void setDriverLName(String driverLName) {
		DriverLName = driverLName;
	}

	public String getDriverPhoneNumber() {
		return DriverPhoneNumber;
	}

	public void setDriverPhoneNumber(String driverPhoneNumber) {
		DriverPhoneNumber = driverPhoneNumber;
	}

	public String getConductorFName() {
		return ConductorFName;
	}

	public void setConductorFName(String conductorFName) {
		ConductorFName = conductorFName;
	}

	public String getConductorLName() {
		return ConductorLName;
	}

	public void setConductorLName(String conductorLName) {
		ConductorLName = conductorLName;
	}

	public String getConductorPhoneNumber() {
		return ConductorPhoneNumber;
	}

	public void setConductorPhoneNumber(String conductorPhoneNumber) {
		ConductorPhoneNumber = conductorPhoneNumber;
	}

}
