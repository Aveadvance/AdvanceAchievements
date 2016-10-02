package com.aveadvance.advancedachievements.data.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="user_task_timers")
public class UserTaskTimer {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_task_timer_seq")
	@SequenceGenerator(name="user_task_timer_seq", sequenceName="user_task_timer_seq", initialValue=1, allocationSize=1)
	private long id;
	
	@NotNull
	@OneToOne
	@JoinColumn(name="user_task_id", referencedColumnName="id")
	private UserTask userTask;
	
	@NotNull
	@Column(name="interval_start")
	private LocalDateTime intervalStart;
	
	@NotNull
	@Column(name="interval_end")
	private LocalDateTime intervalEnd;
	
	@Column(name="start_date")
	private LocalDateTime startDate;
	
	public UserTaskTimer() {
		
	}
	
	public UserTaskTimer(UserTask userTask, LocalDateTime intervalStart, LocalDateTime intervalEnd) {
		this(0, userTask, intervalStart, intervalEnd, null);
	}
	
	public UserTaskTimer(long id, UserTask userTask, LocalDateTime intervalStart
			, LocalDateTime intervalEnd, LocalDateTime startDate) {
		this.id = id;
		this.userTask = userTask;
		this.intervalStart = intervalStart;
		this.intervalEnd = intervalEnd;
		this.startDate = startDate;
	}

	public long getId() {
		return id;
	}

	public UserTask getUserTask() {
		return userTask;
	}

	public LocalDateTime getIntervalStart() {
		return intervalStart;
	}

	public LocalDateTime getIntervalEnd() {
		return intervalEnd;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}
}
