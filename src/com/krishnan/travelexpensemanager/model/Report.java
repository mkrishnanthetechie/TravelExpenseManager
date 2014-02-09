package com.krishnan.travelexpensemanager.model;

import java.io.Serializable;

/**
 * @author krishm90
 * POJO to store report/trip details
 *
 */
public class Report implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int report_Id;

	private String trip_Name;
	private String start_Date;
	private String end_Date;
	private int person_Count;

	public Report(String trip_Name, String start_Date,
			String end_Date, int person_Count) {
		super();
		this.trip_Name = trip_Name;
		this.start_Date = start_Date;
		this.end_Date = end_Date;
		this.person_Count = person_Count;
	}

	public int getReport_Id() {
		return report_Id;
	}

	public void setReport_Id(int report_Id) {
		this.report_Id = report_Id;
	}

	public String getTrip_Name() {
		return trip_Name;
	}

	public String getStart_Date() {
		return start_Date;
	}

	public String getEnd_Date() {
		return end_Date;
	}

	public int getPerson_Count() {
		return person_Count;
	}

	public String toString() {
		return trip_Name;
	}

}
