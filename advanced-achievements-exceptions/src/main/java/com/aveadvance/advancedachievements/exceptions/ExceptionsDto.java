package com.aveadvance.advancedachievements.exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExceptionsDto {
	
	private Map<String,List<String>> exceptions = new HashMap<>();
	
	public void addException(String field, String message) {
		if (exceptions.containsKey(field)) {
			exceptions.get(field).add(message);
		} else {
			exceptions.put(field, new ArrayList<>(Arrays.asList(message)));
		}
	}
	
	public void addException(String field, Set<String> message) {
		if (exceptions.containsKey(field)) {
			exceptions.get(field).addAll(message);
		} else {
			exceptions.put(field, new ArrayList<>(message));
		}
	}
	
	public Map<String,List<String>> getExceptions() {
		return exceptions;
	}
}
