package com.khylo.timetracker.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.khylo.timetracker.StaticData;
import com.khylo.timetracker.mongorepo.StateRepo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString

@Builder(toBuilder = true) @NoArgsConstructor @AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class State implements StaticData{	
	public static final String inProgress = "inProgress";
	public static final String submittedForApproval = "submittedForApproval";
	public static final String approved = "approved";
	public static final String rejected = "rejected";
	public static final String invoiceGenerated = "invoiceGenerated";
	public static final String closed = "closed";
	
	public static final List<String> unapprovedStates = List.of(inProgress, submittedForApproval, rejected); 
	
	
	@Autowired StateRepo stateRepo;
	
	@Id
	String name;
	String user; //user specific.. mostly null
	String agent; // agent specific mostly null
	String client;
	String category;
	
	@Override
	public void createData() {
		stateRepo.save(State.builder().name(inProgress).build());
		stateRepo.save(State.builder().name(submittedForApproval).build());
		stateRepo.save(State.builder().name(approved).build());
		stateRepo.save(State.builder().name(rejected).build());
		stateRepo.save(State.builder().name(invoiceGenerated).build());
		stateRepo.save(State.builder().name(closed).build());
		
	}
	
	

}
