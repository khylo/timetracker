package com.khylo.timetracker.model;

import static org.junit.Assert.assertEquals;

import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import com.khylo.timetracker.model.UserStaticData;

@Log4j2
public class UserStaticDataTest {

	private List<Map<String, Object>> makeTestData(String[] params, Object[][] data){
		List<Map<String, Object>> ret = new ArrayList<>();
		for(Object[] row :data) {
			Map<String,Object> dataRow = new LinkedHashMap<>(); // LinkedHashmap to keep order
			int i=0;
			for(Object item:row) {
				dataRow.put(params[i++].trim(), item);
			}
			ret.add(dataRow);
		}
		return ret;
	}

	@Test
	public void testMtWorkTypes(){
		log.info("Testing testWorkTypes");
		UserStaticData u0 = UserStaticData.builder().levelName("mtParent").allowChildToOverride(true).build();
		assertEquals(List.of(), u0.getSupportedWorkTypes()) ;
		// Child to mt parent
		WorkType wt1 = new WorkType("work");
		UserStaticData u1 = UserStaticData.builder().levelName("child1").allowChildToOverride(true).supportedWorkTypes(List.of(wt1)).build();
		u1.setParent(u0);
		assertEquals(List.of("work"), u1.getSupportedWorkTypes().stream().map(WorkType::getName).collect(Collectors.toList())) ;
		//Add mt child
		WorkType wtmt = new WorkType("mtChild");
		UserStaticData u2 = UserStaticData.builder().levelName("mtChild").allowChildToOverride(true).build();
		u2.setParent(u1);
		assertEquals(List.of("work"), u2.getSupportedWorkTypes().stream().map(WorkType::getName).collect(Collectors.toList())) ;

	}

	@Test
	public void testWorkTypes(){
		log.info("Testing testWorkTypes");
		//client
		UserStaticData clientData = UserStaticData.builder().levelName("Client").allowChildToOverride(true).supportedWorkTypes(List.of(WorkType.Day, WorkType.Overtime2, WorkType.Overtime15)).build();
		assertEquals(List.of(WorkType.Day, WorkType.Overtime2, WorkType.Overtime15), clientData.getSupportedWorkTypes()) ;

		//Agent
		UserStaticData agentData = UserStaticData.builder().levelName("Agent").allowChildToOverride(true).supportedWorkTypes(List.of(WorkType.Overtime)).blockedTypes(List.of(WorkType.Overtime2, WorkType.Overtime15)).build();
		assertEquals(List.of(WorkType.Overtime), agentData.getSupportedWorkTypes()) ;
		agentData.setParent(clientData);
		assertEquals(List.of(WorkType.Day, WorkType.Overtime), agentData.getSupportedWorkTypes()) ;

		//User
		UserStaticData userData = UserStaticData.builder().levelName("User").allowChildToOverride(true).supportedWorkTypes(List.of(WorkType.OnCall)).blockedTypes(List.of(WorkType.Overtime)).build();
		assertEquals(List.of(WorkType.OnCall), userData.getSupportedWorkTypes()) ;
		userData.setParent(agentData);
		assertEquals(List.of(WorkType.Day, WorkType.OnCall), userData.getSupportedWorkTypes()) ;

	}
	
	/*@Test
	public void testUserChild(){
		//setup test data
		String[] params = "level1 |level2 | level3 | level4 | l1  | l2  | l3  | l4  || lvl    |workTypes".replaceAll("\\|\\|","|").split("\\|");
		Object[][] data = {
							{"One", null  , null   , null   ,true ,true ,true ,true , "One"  ,List.of("One")                      },
							{"One", "Two" , null   , null   ,true ,true ,true ,true , "Two"  ,List.of("One","Two")                },
							{"One", "Two" , "Three", null   ,true ,true ,true ,true , "Three",List.of("One","Two","Three")        },
							{"One", "Two" , "Three", "Four" ,false,true ,true ,true , "One"  ,List.of("One")                      },
							{"One", "Two" , "Three", "Four" ,true ,false,true ,true , "Two"  ,List.of("One","Two")                },
							{"One", "Two" , "Three", "Four" ,true ,true ,false,true , "Three",List.of("One","Two","Three")        },
							{"One", "Two" , "Three", "Four" ,true ,true ,true ,false, "Four" ,List.of("One","Two","Three","Four") },
							{"One", "Two" , "Three", "Four" ,true ,true ,true ,true , "Four" ,List.of("One","Two","Three","Four") },
		};
		List<Map<String, Object>> testData = makeTestData(params,data);
		for(Map<String, Object> test:testData) {
			log.info("Testing "+test);
			WorkType wt1 = new WorkType((String)test.get("level1"));
			WorkType wt2 = new WorkType((String)test.get("level2"));
			WorkType wt3 = new WorkType((String)test.get("level3"));
			WorkType wt4 = new WorkType((String)test.get("level4"));
			UserStaticData u1 = UserStaticData.builder().levelName((String)test.get("level1")).allowChildToOverride((Boolean)test.get("l1")).supportedWorkTypes(List.of(wt1)).build();
			UserStaticData u2 = UserStaticData.builder().levelName((String)test.get("level2")).allowChildToOverride((Boolean)test.get("l2")).supportedWorkTypes(List.of(wt2)).build();
			UserStaticData u3 = UserStaticData.builder().levelName((String)test.get("level3")).allowChildToOverride((Boolean)test.get("l3")).supportedWorkTypes(List.of(wt3)).build();
			UserStaticData u4 = UserStaticData.builder().levelName((String)test.get("level4")).allowChildToOverride((Boolean)test.get("l4")).supportedWorkTypes(List.of(wt4)).build();
			if(test.get("level4")!=null)
				u3.setChild(u4);
			if(test.get("level3")!=null)
				u2.setChild(u3);
			if(test.get("level2")!=null)
				u1.setChild(u2);
			
			//Test
			
			assertEquals(((List)test.get("workTypes")), u1.getSupportedWorkTypes().stream().map(WorkType::getName).collect(Collectors.toList())) ;
		}
			


	}
*/
}
