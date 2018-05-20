package com.khylo.timetracker.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true) @NoArgsConstructor @AllArgsConstructor 
public class WorkedDay{
	private int dayOfMonth;
	private BigDecimal worked;
	private WorkType type;
}