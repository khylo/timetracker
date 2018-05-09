package com.khylo.timetracker.mongorepo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.khylo.timetracker.model.Timesheet;

public interface TimesheetRepo  extends CrudRepository<Timesheet, String>{

	public Optional<Timesheet> findByName(@Param("name")String name);
	public Optional<Timesheet> findByClient(@Param("name")String name);
	public Optional<Timesheet> findByYearAndMonth(@Param("y")Integer year, @Param("m") Integer month);;
}