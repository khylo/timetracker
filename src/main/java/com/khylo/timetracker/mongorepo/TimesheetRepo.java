package com.khylo.timetracker.mongorepo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.khylo.timetracker.model.Timesheet;

@RepositoryRestResource
public interface TimesheetRepo  extends CrudRepository<Timesheet, String>, TimesheetCustomRepo{

	public List<Timesheet> findByName(@Param("n")String name);
	public List<Timesheet> findByClient(@Param("c")String name);
	public List<Timesheet> findByYearAndMonth(@Param("y")Integer year, @Param("m") Integer month);
	public List<Timesheet> findByNameAndYearAndMonth(@Param("n")String name, @Param("y")Integer year, @Param("m") Integer month);
	public List<Timesheet> findByYearAndMonthAndState(@Param("n")String name, @Param("y")Integer year, @Param("m") Integer month, @Param("s") String state);
}
