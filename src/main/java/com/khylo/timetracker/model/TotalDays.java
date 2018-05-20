package com.khylo.timetracker.model;

import java.math.BigDecimal;

import com.khylo.timetracker.model.WorkedDay.WorkedDayBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true) @NoArgsConstructor @AllArgsConstructor  
public class TotalDays{
	private BigDecimal days;
	private WorkType type;
}