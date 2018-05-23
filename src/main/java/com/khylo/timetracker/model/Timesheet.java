package com.khylo.timetracker.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Data @ToString(exclude="id")

@Builder(toBuilder = true) @NoArgsConstructor @AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Timesheet {
	@Id
    private String id;

	private String name;
	private UserStaticData userData;
	private List<User> users;

	@lombok.NonNull
	private Integer year; // Integer so we can use noNull
	@lombok.NonNull 
	private Integer month;
	@Builder.Default private List<WorkedDay> days=new ArrayList<>();
	private List<TotalDays> totalDays;
	@CreatedDate
	private Date createdDate;
	private State state;
	@lombok.NonNull
	private List<WorkType> allowedWorkTypes;
	
	public Timesheet calculate() {
		List<TotalDays> ret = new ArrayList<>();
		for(WorkType type: allowedWorkTypes){
			BigDecimal num = days.stream().filter(wd->wd.getType()==type).map(WorkedDay::getWorked).reduce(BigDecimal.ZERO, BigDecimal::add);
			ret.add(new TotalDays(num,type));
		}
		this.setTotalDays(ret);
		return this;
	}

	public boolean validWorkType(WorkType wt){
		return allowedWorkTypes.contains(wt);
	}

	public Timesheet addDay(WorkedDay d){
		if(!validWorkType(d.getType()))
			throw new IllegalArgumentException("Invalid work day type "+d.getType()+". Supported values "+ allowedWorkTypes);
		days.add(d);
		return this;
	}

	private List<WorkType> getUsedWorkTypes(){
		if(days==null || days.isEmpty())
			return Collections.EMPTY_LIST;
		return days.stream().map(WorkedDay::getType).distinct().collect(Collectors.toList());
	}

	public Timesheet setAllowedWorkTypes(List<WorkType> newAllowedWorkTypes){
		if(newAllowedWorkTypes==null)
			throw new IllegalArgumentException("Invalid WorkType list "+newAllowedWorkTypes);
		List<WorkType> usedWorkTypes = getUsedWorkTypes();
		boolean bad=!usedWorkTypes.stream().allMatch(wt->newAllowedWorkTypes.contains(wt));
		if(bad)
			throw new IllegalArgumentException("Invalid WorkType list "+newAllowedWorkTypes+" It does not contain some of the existing workTypes "+usedWorkTypes);
		this.allowedWorkTypes=newAllowedWorkTypes;
		return this;
	}
	

}
