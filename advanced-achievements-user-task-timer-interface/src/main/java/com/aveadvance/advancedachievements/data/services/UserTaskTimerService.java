package com.aveadvance.advancedachievements.data.services;

import java.time.LocalDateTime;
import java.util.List;

import com.aveadvance.advancedachievements.data.dto.ResponseDto;
import com.aveadvance.advancedachievements.data.dto.UserTaskTimerDto;
import com.aveadvance.advancedachievements.data.entities.UserTask;
import com.aveadvance.advancedachievements.data.entities.UserTaskTimer;

public interface UserTaskTimerService {

	List<UserTaskTimer> retrieve(String userName, long workspaceId);

	void create(UserTask userTask, LocalDateTime intervalStart, LocalDateTime intervalEnd);

	ResponseDto<UserTaskTimerDto> engage(long id);

	void delete(long taskId);

}
