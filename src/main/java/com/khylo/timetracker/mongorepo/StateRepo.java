package com.khylo.timetracker.mongorepo;

import org.springframework.data.repository.CrudRepository;

import com.khylo.timetracker.model.State;

public interface StateRepo  extends CrudRepository<State, String>{

}
