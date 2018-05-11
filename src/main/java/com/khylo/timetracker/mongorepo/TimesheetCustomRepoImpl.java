package com.khylo.timetracker.mongorepo;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.khylo.timetracker.model.State;
import com.khylo.timetracker.model.Timesheet;

public class TimesheetCustomRepoImpl implements TimesheetCustomRepo {
	MongoTemplate mongoTemplate;
	
	@Override
	public List<Timesheet> getUnapprovedDueTimesheets() {
		LocalDateTime now = LocalDateTime.now();
		return getUnapprovedDueTimesheets(now.getMonthValue(), now.getYear());
	}

	@Override
	public List<Timesheet> getUnapprovedDueTimesheets(int year, int month) {
		Query unapp = new Query();
		unapp.addCriteria(Criteria.where("year").is(year).and("month").is(month).and("state").in(State.unapprovedStates));
		
		return mongoTemplate.find(unapp, Timesheet.class);
	}

}
