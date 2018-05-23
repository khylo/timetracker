package com.khylo.timetracker.model;

import java.util.ArrayList;
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
	@Builder.Default List<WorkType> supportedWorkTypes=List.of();
	@Builder.Default List<WorkType> blockedTypes=List.of();
	UserStaticData child;
	UserStaticData parent;
	boolean allowChildToOverride;

	public void createData() {
		
	}

	private List<WorkType> getParentWorkTypes(){
		List<WorkType> ret = new ArrayList<>();
		if(parent!=null) {
			ret.addAll(parent.getSupportedWorkTypes());
			if (parent.allowChildToOverride)
				ret.removeAll(blockedTypes);
		}
		ret.addAll(supportedWorkTypes);
		return ret;
	}

	public List<WorkType> getSupportedWorkTypes0() {
		if(allowChildToOverride && child!=null) {
			List<WorkType> childResp = new ArrayList<>(supportedWorkTypes);
			childResp.addAll(child.getSupportedWorkTypes());
			if(childResp!=null && !childResp.isEmpty() )
				return childResp;
		}
		log.info("At level "+levelName);
		return supportedWorkTypes;
	}
	
	/**
	 * Walk dependency tree to see if child node overrides this value
	 * 
	 */
	public List<WorkType> getSupportedWorkTypes() {
		return getParentWorkTypes();
	}

	// Old version that went down through children. New version goes up through parents.
	/*public List<WorkType> getSupportedWorkTypes() {
		log.debug("At level "+levelName);
		if(allowChildToOverride && child!=null) {
			return child.getSupportedWorkTypes(getParentWorkTypes());
		}
		return getParentWorkTypes();
	}

	private List<WorkType> getSupportedWorkTypes(List<WorkType> parentTypes) {
		log.debug("At level "+levelName);
		List<WorkType> ret = new ArrayList<>(parentTypes);
		ret.removeAll(blockedTypes);
		boolean res =ret.addAll(supportedWorkTypes);
		if(!res){
			log.info("ret.addAll(supportedWorkTypes) returned false. This means at "+levelName+" there are no supportedTypes");
		}
		if(allowChildToOverride && child!=null) {
			return child.getSupportedWorkTypes(ret);
		}
		return ret;
	}*/
}
