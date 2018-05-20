package com.khylo.timetracker.model;

import java.util.List;

import com.khylo.common.StaticData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Data
@Builder(toBuilder = true) @NoArgsConstructor @AllArgsConstructor @Log4j2
public class UserStaticData implements StaticData{
	String userId;
	String levelName; // e.g. in terms of hierarchy Agent (optional), Client, Manager, User 
	List<WorkType> supportedWorkTypes;
	UserStaticData child;
	boolean allowChildToOverride;

	public void createData() {
		
	}
	
	/**
	 * Walk dependency tree to see if child node overrides this value
	 * 
	 */
	public List<WorkType> getSupportedWorkTypes() {
		if(allowChildToOverride && child!=null) {
			List<WorkType> childResp  = child.getSupportedWorkTypes();
			if(childResp!=null && !childResp.isEmpty() )
				return childResp;
		}
		log.info("At level "+levelName);
		return supportedWorkTypes;
	}
}
