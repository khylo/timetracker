package com.khylo.timetracker.mongorepo;

import java.util.List;

import com.khylo.timetracker.model.Timesheet;

public interface TimesheetCustomRepo {
	
	public List<Timesheet> getUnapprovedDueTimesheets();
	public List<Timesheet> getUnapprovedDueTimesheets(int year, int month);

}
