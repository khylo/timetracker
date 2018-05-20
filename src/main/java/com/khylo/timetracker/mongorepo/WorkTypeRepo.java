package com.khylo.timetracker.mongorepo;

import org.springframework.data.repository.CrudRepository;

import com.khylo.timetracker.model.WorkType;

public interface WorkTypeRepo  extends CrudRepository<WorkType, String>{

}
