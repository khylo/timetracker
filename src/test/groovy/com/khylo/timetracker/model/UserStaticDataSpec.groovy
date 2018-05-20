package com.khylo.timetracker.model

import org.bouncycastle.math.ec.WTauNafMultiplier

import com.khylo.timetracker.model.UserStaticData

import spock.lang.Unroll

class UserStaticDataSpec extends spock.lang.Specification{

	@Unroll
	def testUserChild(){
		when:
		WorkType wt1 = new WorkType();
		wt1.setName(level1);
		WorkType wt2 = new WorkType();
		wt2.setName(level2);
		WorkType wt3 = new WorkType();
		wt3.setName(level3);
		WorkType wt4 = new WorkType();
		wt4.setName(level4);
		UserStaticData u1 = UserStaticData.builder().levelName(level1).supportedWorkTypes(List.of(wt1)).build();
		UserStaticData u2 = UserStaticData.builder().levelName(level2).supportedWorkTypes(List.of(wt2)).build();
		UserStaticData u3 = UserStaticData.builder().levelName(level3).supportedWorkTypes(List.of(wt3)).build();
		UserStaticData u4 = UserStaticData.builder().levelName(level4).supportedWorkTypes(List.of(wt4)).build();
		
		u3.setChild(u4);
		u2.setChild(u3);
		u1.setChild(u2);	
						
		then:
			u1.getSupportedWorkTypes() == workTypes
			
		where:
			level1 |level2 | level3 | level4 | l1  | l2  | l3  | l4  || lvl    |workTypes
			"One"  | null  | null   | null   |true |true |true |true || "One"  |List.of("One")
			"One"  | "Two" | null   | null   |true |true |true |true || "Two"  |List.of("One","Two")
			"One"  | "Two" | "Three"| null   |true |true |true |true || "Three"|List.of("One","Two","Three") 
			"One"  | "Two" | "Three"| "Four" |false|true |true |true || "One"  |List.of("One") 
			"One"  | "Two" | "Three"| "Four" |true |false|true |true || "Two"  |List.of("One","Two") 
			"One"  | "Two" | "Three"| "Four" |true |true |false|true || "Three"|List.of("One","Two","Three") 
			"One"  | "Two" | "Three"| "Four" |true |true |true |false|| "Four" |List.of("One","Two","Three","Four") 
			"One"  | "Two" | "Three"| "Four" |true |true |true |true || "Four" |List.of("One","Two","Three","Four") 
			
	}

}
