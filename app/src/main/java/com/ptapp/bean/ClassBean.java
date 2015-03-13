package com.ptapp.bean;

/**
 * This class is our model and contains the data we will save in the database
 * and show in the user interface.
 */
public class ClassBean {
	// private variables
	private String id; // PK
	private String className;
	private String section;
	private String schoolId;
	private String clsInchargeEducatorId;
	private String attendantId;
	private String transportId;
	private String feeId;
	private String studentId;
	private String sessionYear;

	// Empty constructor
	public ClassBean() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getClsInchargeEducatorId() {
		return clsInchargeEducatorId;
	}

	public void setClsInchargeEducatorId(String clsInchargeEducatorId) {
		this.clsInchargeEducatorId = clsInchargeEducatorId;
	}

	public String getAttendantId() {
		return attendantId;
	}

	public void setAttendantId(String attendantId) {
		this.attendantId = attendantId;
	}

	public String getTransportId() {
		return transportId;
	}

	public void setTransportId(String transportId) {
		this.transportId = transportId;
	}

	public String getFeeId() {
		return feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getSessionYear() {
		return sessionYear;
	}

	public void setSessionYear(String sessionYear) {
		this.sessionYear = sessionYear;
	}
}
