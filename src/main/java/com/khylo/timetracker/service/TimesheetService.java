package com.khylo.timetracker.service;

import com.khylo.timetracker.model.Timesheet;

public class TimesheetService {
	Timesheet calculate(Timesheet timesheet) {
		return timesheet.calculate();
	}

}
