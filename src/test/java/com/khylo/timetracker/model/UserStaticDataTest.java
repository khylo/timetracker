package com.khylo.timetracker.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.khylo.timetracker.model.UserStaticData;


public class UserStaticDataTest {

	private List<Map<String, Object>> makeTestData(String[] params, Object[][] data){
		List<Map<String, Object>> ret = new ArrayList<>();
		for(Object[] row :data) {
			Map<String,Object> dataRow = new HashMap<>();
			int i=0;
			for(Object item:row) {
				dataRow.put(params[i++].trim(), item);
			}
			ret.add(dataRow);
		}
		return ret;
	}
	
	@Test
	public void testUserChild(){
		//setup test data
		/*
			level1 |level2 | level3 | level4 | l1  | l2  | l3  | l4  || lvl    |workTypes
			"One"  | null  | null   | null   |true |true |true |true || "One"  |List.of("One")
			"One"  | "Two" | null   | null   |true |true |true |true || "Two"  |List.of("One","Two")
			"One"  | "Two" | "Three"| null   |true |true |true |true || "Three"|List.of("One","Two","Three") 
			"One"  | "Two" | "Three"| "Four" |false|true |true |true || "One"  |List.of("One") 
			"One"  | "Two" | "Three"| "Four" |true |false|true |true || "Two"  |List.of("One","Two") 
			"One"  | "Two" | "Three"| "Four" |true |true |false|true || "Three"|List.of("One","Two","Three") 
			"One"  | "Two" | "Three"| "Four" |true |true |true |false|| "Four" |List.of("One","Two","Three","Four") 
			"One"  | "Two" | "Three"| "Four" |true |true |true |true || "Four" |List.of("One","Two","Three","Four") 
		 */
		String[] params = "level1 |level2 | level3 | level4 | l1  | l2  | l3  | l4  || lvl    |workTypes".split("|");
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
			WorkType wt1 = new WorkType(test.get("level1").toString());
			WorkType wt2 = new WorkType(test.get("level2").toString());
			WorkType wt3 = new WorkType(test.get("level3").toString());
			WorkType wt4 = new WorkType(test.get("level4").toString());
			UserStaticData u1 = UserStaticData.builder().levelName(test.get("level1").toString()).supportedWorkTypes(List.of(wt1)).build();
			UserStaticData u2 = UserStaticData.builder().levelName(test.get("level2").toString()).supportedWorkTypes(List.of(wt2)).build();
			UserStaticData u3 = UserStaticData.builder().levelName(test.get("level3").toString()).supportedWorkTypes(List.of(wt3)).build();
			UserStaticData u4 = UserStaticData.builder().levelName(test.get("level4").toString()).supportedWorkTypes(List.of(wt4)).build();
			
			u3.setChild(u4);
			u2.setChild(u3);
			u1.setChild(u2);	
			
			//Test
			
			assertEquals(u1.getSupportedWorkTypes() , ((List)test.get("workTypes"))) ; 
		}
			
			
	}

}
