package com.khylo.timetracker.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class TimeTrackerUnitTest {
	
	private List<WorkedDay> wd(List<Integer> days, List<Integer> onCall, List<Integer> overtime){
		List<WorkedDay> ret = new ArrayList<>();;
		if(ret!=null)
			ret.addAll(days.stream().map(i->new WorkedDay(i,BigDecimal.ONE,WorkType.Day)).collect(Collectors.toList()));
		if(onCall!=null)
			ret.addAll(onCall.stream().map(i->new WorkedDay(i,BigDecimal.ONE,WorkType.OnCall)).collect(Collectors.toList()));
		if(overtime!=null)
			ret.addAll(overtime.stream().map(i->new WorkedDay(i,BigDecimal.ONE,WorkType.Overtime)).collect(Collectors.toList()));
		return ret;		
    }
	
	private Timesheet buildTimesheet(int year, int month, List<Integer> days, List<Integer> onCall, List<Integer> overtime) {		
		return Timesheet.builder().days(wd(days, onCall, overtime)).build();
	}

	@Test
	public void testTimesheet() {
		Timesheet t = buildTimesheet(2018,5,List.of(8,9,10,11,14,15,16,17,18,21,22,23,24,25,28,29,30,31), List.of(1,2), null);
		t.calculate();
	}

}
