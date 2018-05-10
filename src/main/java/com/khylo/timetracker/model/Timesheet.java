package com.khylo.timetracker.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data @ToString(exclude="id")

@Builder(toBuilder = true) @NoArgsConstructor @AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Timesheet {
	@Id
    private String id;

	private String name;
	private String client;
	private String agent;
	private String manager;
	private BigDecimal days;
	private BigDecimal onCall;
	private BigDecimal overtime;
	private int year;
	private int month;
	@CreatedDate
	private Date createdDate;
	private State state;

	private List<WorkedDay> workedDays;
	private List<WorkedDay> onCallDays;
	private List<WorkedDay> overtimeDays;
	
	class WorkedDay{
		private int dayOfMonth;
		private BigDecimal worked;
	}
	
	enum State{
		inProgress,
		submittedForApproval,
		approved,
		archived
	}
	

}
