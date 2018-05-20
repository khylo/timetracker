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
import java.util.function.Predicate;

@Data @ToString(exclude="id")

@Builder(toBuilder = true) @NoArgsConstructor @AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Timesheet {
	@Id
    private String id;
	
	private UserStaticData user; // should user take over from name client agent manager

	private String name;
	private String client;
	private String agent;
	private String manager;

	private int year;
	private int month;
	private List<WorkedDay> days;
	private List<TotalDays> totalDays;
	@CreatedDate
	private Date createdDate;
	private State state;
	//Optional Subset of Global WorkType, specifying the workTypes for this timesheet
	private List<WorkType> types;
	
	private List<TotalDays> td(BigDecimal days, BigDecimal onCall, BigDecimal overtime){
    	return List.of(	new TotalDays(days, WorkType.Day), 
						new TotalDays(onCall, WorkType.OnCall),
						new TotalDays(overtime, WorkType.Overtime));
    }
	
	public Timesheet calculate() {
		
		Predicate<WorkedDay> day = wd -> wd.getType()==WorkType.Day; 
		BigDecimal numDays = days.stream().filter(day).map(WorkedDay::getWorked).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal onCall = days.stream().filter(day).map(WorkedDay::getWorked).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal overtime = days.stream().filter(day).map(WorkedDay::getWorked).reduce(BigDecimal.ZERO, BigDecimal::add);
		
		this.setTotalDays(td(numDays, onCall, overtime));
		return this;
	}
	

}
