package com.khylo.timetracker.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.khylo.common.StaticData;
import com.khylo.timetracker.mongorepo.WorkTypeRepo;

import lombok.Data;
import lombok.ToString;

@Data @ToString()
@JsonInclude(Include.NON_NULL)
public class WorkType{
	
	public static final String days = "days";
	public static final String onCall = "onCall";
	public static final String overtime = "overtime";
	public static final String overtime15 = "overtime15";
	public static final String overtime2 = "overtime2";

	public static final WorkType Day = new WorkType(days);
	public static final WorkType OnCall =     new WorkType(onCall);
	public static final WorkType Overtime =   new WorkType(overtime);
	public static final WorkType Overtime15 = new WorkType(overtime15);
	public static final WorkType Overtime2 =  new WorkType(overtime2);

	
	@Id
	String name;
	
	public WorkType(String name) {
		this.name=name;	
	}

}
