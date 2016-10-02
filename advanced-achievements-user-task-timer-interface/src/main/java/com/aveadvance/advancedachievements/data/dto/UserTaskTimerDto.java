package com.aveadvance.advancedachievements.data.dto;

import java.time.LocalDateTime;

public class UserTaskTimerDto {
	
	private UserTaskDto userTask;
	
	private LocalDateTime intervalStart;
	
	private LocalDateTime intervalEnd;
	
	private LocalDateTime startDate;

	public UserTaskTimerDto() {}

	public UserTaskTimerDto(UserTaskDto userTask, LocalDateTime intervalStart, LocalDateTime intervalEnd,
			LocalDateTime startDate) {
		super();
		this.userTask = userTask;
		this.intervalStart = intervalStart;
		this.intervalEnd = intervalEnd;
		this.startDate = startDate;
	}

	public UserTaskDto getUserTask() {
		return userTask;
	}

	public void setUserTask(UserTaskDto userTask) {
		this.userTask = userTask;
	}

	public LocalDateTime getIntervalStart() {
		return intervalStart;
	}

	public void setIntervalStart(LocalDateTime intervalStart) {
		this.intervalStart = intervalStart;
	}

	public LocalDateTime getIntervalEnd() {
		return intervalEnd;
	}

	public void setIntervalEnd(LocalDateTime intervalEnd) {
		this.intervalEnd = intervalEnd;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

}
