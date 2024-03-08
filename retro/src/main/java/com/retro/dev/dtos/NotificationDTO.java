package com.retro.dev.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.retro.dev.models.User;


public class NotificationDTO {
    
	//private attribs
	//////////////////
	private Long id;
    private Long userid;
    private String notificationType;
    private String module;
	private Long moduleId;
    private String message;
    private String readStatus;
    
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
	
    private boolean reminderStatus;
    
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reminderDate;
	
    private User userdata;
    

	//public attribs
	//////////////////
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getUserid() {
		return userid;
	}


	public void setUserid(Long userid) {
		this.userid = userid;
	}


	public String getNotificationType() {
		return notificationType;
	}


	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}


	public String getModule() {
		return module;
	}


	public void setModule(String module) {
		this.module = module;
	}


	public Long getModuleId() {
		return moduleId;
	}


	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getReadStatus() {
		return readStatus;
	}


	public void setReadStatus(String readStatus) {
		this.readStatus = readStatus;
	}


	public LocalDateTime getStartDate() {
		return startDate;
	}


	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}


	public LocalDateTime getEndDate() {
		return endDate;
	}


	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}


	public boolean isReminderStatus() {
		return reminderStatus;
	}


	public void setReminderStatus(boolean reminderStatus) {
		this.reminderStatus = reminderStatus;
	}


	public LocalDateTime getReminderDate() {
		return reminderDate;
	}


	public void setReminderDate(LocalDateTime reminderDate) {
		this.reminderDate = reminderDate;
	}


	public User getUserdata() {
		return userdata;
	}


	public void setUserdata(User userdata) {
		this.userdata = userdata;
	}

    
}
