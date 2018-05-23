package com.khylo.timetracker.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class TimesheetUnitTest {
	private List<WorkedDay> wd(List<Integer> days, List<Integer> onCall){
		return wd(days,onCall,Collections.emptyList() );
	}
	private List<WorkedDay> wd(List<Integer> days, List<Integer> onCall, List<Integer> overtime){
		List<WorkedDay> ret = new ArrayList<>(days.size()+onCall.size()+overtime.size());
		if(days!=null)
			ret.addAll(days.stream().map(i->new WorkedDay(i,BigDecimal.ONE,WorkType.Day)).collect(Collectors.toList()));
		if(onCall!=null)
			ret.addAll(onCall.stream().map(i->new WorkedDay(i,BigDecimal.ONE,WorkType.OnCall)).collect(Collectors.toList()));
		if(overtime!=null)
			ret.addAll(overtime.stream().map(i->new WorkedDay(i,BigDecimal.ONE,WorkType.Overtime)).collect(Collectors.toList()));
		return ret;		
    }

	private Timesheet buildTimesheet(int year, int month, List<Integer> days, List<Integer> onCall) {
		if(onCall==null)
			onCall=Collections.EMPTY_LIST;
		return Timesheet.builder().days(wd(days, onCall)).year(year).month(month).allowedWorkTypes(List.of(WorkType.Day,WorkType.OnCall)).build();
	}
	private Timesheet buildTimesheet(int year, int month, List<Integer> days, List<Integer> onCall, List<Integer> overtime) {
		if(onCall==null)
			onCall=Collections.EMPTY_LIST;
		if(overtime==null)
			overtime=Collections.EMPTY_LIST;
		return Timesheet.builder().days(wd(days, onCall, overtime)).year(year).month(month).allowedWorkTypes(List.of(WorkType.Day,WorkType.OnCall,WorkType.Overtime)).build();
	}

	@Test
	public void testTimesheet() {
		Timesheet t = buildTimesheet(2018,5,List.of(1,2,3,4,8,9,10,11,14,15,16,17,18,21,22,23,24,25,28,29,30,31), List.of(1,2));
		List<TotalDays> res = t.calculate().getTotalDays();
		assertEquals(res.get(0).getType(),WorkType.Day);
		assertEquals(res.get(0).getDays().intValue(),22);
		assertEquals(res.get(1).getType(),WorkType.OnCall);
		assertEquals(res.get(1).getDays().intValue(),2);

		t = buildTimesheet(2017,12,List.of(1,4,5,6,7,8,11,12,13,14,15,18,19,20,21,22,27,28,29), null, List.of(1,2,3,4));
		res = t.calculate().getTotalDays();
		assertEquals(3,res.size());
		assertEquals(res.get(0).getType(),WorkType.Day);
		assertEquals(res.get(0).getDays().intValue(),19);
		assertEquals(res.get(1).getType(),WorkType.OnCall);
		assertEquals(res.get(1).getDays().intValue(),0);
		assertEquals(res.get(2).getType(),WorkType.Overtime);
		assertEquals(res.get(2).getDays().intValue(),4);
	}

	@Test
	public void testAddDaysWithInvalidWorkType(){
		//First create null
		Timesheet t;
		try {
			t = Timesheet.builder().year(2018).month(4).allowedWorkTypes(null).build();
			fail("Expect to fail. CAn't set allowedWrokTypes to null");
		}catch (NullPointerException e){}

		//Try empty list
		t = Timesheet.builder().year(2018).month(4).allowedWorkTypes(Collections.EMPTY_LIST).build();
		try {
			t.addDay(new WorkedDay(3, BigDecimal.ONE, WorkType.Day));
			fail("Shoudl fail. Adding unsupported workedtype");
		}catch(IllegalArgumentException e){}

		//test calculate
		t.calculate();
		assertEquals(0,t.getTotalDays().size());

		//Test not null
		t.setAllowedWorkTypes(List.of(WorkType.Overtime));
		try {
			t.addDay(new WorkedDay(3, BigDecimal.ONE, WorkType.Day));
			fail("Should fail. Adding unsupported workedtype");
		}catch(IllegalArgumentException e){}

		//Test works
		t.setAllowedWorkTypes(List.of(WorkType.Day));
		t.addDay(new WorkedDay(3, BigDecimal.ONE, WorkType.Day));

	}

	@Test
	public void testAllowedWorkTypes(){
		List<Integer> days = List.of(2,3,4,5,6,9,10,11,12,13,16,17,18,19,20,23,24,25,26,27,30);
		List<Integer> onCall = List.of(1,2);
		List<Integer> overtime = List.of(3,4);

		Timesheet t = Timesheet.builder().days(wd(days, onCall)).year(2018).month(4).allowedWorkTypes(List.of(WorkType.Day,WorkType.OnCall)).build();
		//Set new allowedWorkTypes
		t.setAllowedWorkTypes(List.of(WorkType.Day,WorkType.OnCall,WorkType.Overtime));
		//Set allowedWorkType to list that doesn't cover days list
		try{
			t.setAllowedWorkTypes(List.of(WorkType.Day,WorkType.Overtime));
			fail("Expected exception not thrown. Invalid allowedWorkType list added");
		}catch(IllegalArgumentException e){

		}
		//Set allowedWorkType to list that doesn't cover days list
		try{
			t.setAllowedWorkTypes(null);
			fail("Expected exception not thrown. Invalid allowedWorkType list added");
		}catch(IllegalArgumentException e){

		}

		//Final positive test
		t.setAllowedWorkTypes(List.of(WorkType.Day,WorkType.Overtime,WorkType.OnCall));
		
	}

}
