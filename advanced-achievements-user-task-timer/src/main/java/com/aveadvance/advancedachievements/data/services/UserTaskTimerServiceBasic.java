package com.aveadvance.advancedachievements.data.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aveadvance.advancedachievements.data.dao.UserTaskTimerDao;
import com.aveadvance.advancedachievements.data.dto.ResponseDto;
import com.aveadvance.advancedachievements.data.dto.UserTaskDto;
import com.aveadvance.advancedachievements.data.dto.UserTaskTimerDto;
import com.aveadvance.advancedachievements.data.entities.UserTask;
import com.aveadvance.advancedachievements.data.entities.UserTaskTimer;

@Service
public class UserTaskTimerServiceBasic implements UserTaskTimerService {
	
	@Autowired
	private UserTaskTimerDao userTaskTimerDao;

	@Override
	@Transactional(readOnly=true)
	public List<UserTaskTimer> retrieve(String userName, long workspaceId) {
		return userTaskTimerDao.retrieve(userName, workspaceId);
	}

	@Override
	public void create(UserTask userTask, LocalDateTime intervalStart, LocalDateTime intervalEnd) {
		userTaskTimerDao.create(new UserTaskTimer(userTask, intervalStart, intervalEnd));
	}

	@Override
	public ResponseDto<UserTaskTimerDto> engage(long id) {
		ResponseDto<UserTaskTimerDto> response = new ResponseDto<>();
		Optional<UserTaskTimer> taskTimerOpt = userTaskTimerDao.retrieve(id);
		if (taskTimerOpt.isPresent()) {
			UserTaskTimer taskTimer = taskTimerOpt.get();
			if (taskTimer.getStartDate() == null) {
				UserTaskTimer newTaskTimer = new UserTaskTimer(taskTimer.getId(), taskTimer.getUserTask()
						, taskTimer.getIntervalStart(), taskTimer.getIntervalEnd(), LocalDateTime.now());
				userTaskTimerDao.update(newTaskTimer);
				UserTaskDto userTaskDto = new UserTaskDto();
				userTaskDto.setId(newTaskTimer.getUserTask().getId());
				response.setStatus("started");
				response.setEntity(new UserTaskTimerDto(userTaskDto, newTaskTimer.getIntervalStart()
						, newTaskTimer.getIntervalEnd(), newTaskTimer.getStartDate()));
			} else {
				Duration duration = Duration.between(taskTimer.getStartDate(), LocalDateTime.now());
				LocalDateTime newIntervalEnd = taskTimer.getIntervalEnd().plus(duration);
				UserTaskTimer newTaskTimer = new UserTaskTimer(taskTimer.getId(), taskTimer.getUserTask()
						, taskTimer.getIntervalStart(), newIntervalEnd, null);
				userTaskTimerDao.update(newTaskTimer);
				UserTaskDto userTaskDto = new UserTaskDto();
				userTaskDto.setId(newTaskTimer.getUserTask().getId());
				response.setStatus("stopped");
				response.setEntity(new UserTaskTimerDto(userTaskDto, newTaskTimer.getIntervalStart()
						, newTaskTimer.getIntervalEnd(), newTaskTimer.getStartDate()));
			}
		} else {
			response.setStatus("not found");
		}
		return response;
	}

	@Override
	public void delete(long taskId) {
		userTaskTimerDao.delete(taskId);
	}

}
