package com.ptapp.service;

/**
 * Service Proxy to call the server to fetch data. - for now it's taking data
 * from Stub class.
 */

public class ServiceProxy {

	public ServiceProxy() {

	}

	// Get the FTLoad feed - JSON String.
	public String getFTFeed() {

		// Retrofit
		// StudentInfo studentInfo = ApiClient.getStudentInfo();
		// StudentDemoBO str = studentInfo.getStu("1");
		// Log.v("ttrtag", "stu bloodgrp: " + str.getBlood_group());
		// Log.v("ttrtag", "stu firtname: " + str.getFirst_name());
		// Log.v("ttrtag", "stu dob: " + str.getDob());
		// Log.v("ttrtag", "stu allergies: " + str.getAllergies());
		// return str.toString();

		/*Stub objStub = new Stub();
		String js = objStub.JSONFTLoadFeed();
		return js;*/
        return null;
	}

	// Get the Events JSON String
	public String getJSONEvents() {
		/*Stub objStub = new Stub();
		String js = objStub.getEventsJSONString();
		return js;*/
        return null;
	}
}
