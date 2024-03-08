package com.retro.dev.models;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long userid;
    
    @NotBlank
    private String notificationType;
    
    @NotBlank
    private String module;
    
	private Long moduleId;
    
    @NotBlank
    private String message;
    
    @NotBlank
    private String readStatus;
    
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    
    private boolean reminderStatus;
    
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reminderDate;
    
    //default constructor
    /////////////////////
    public Notification() {
		super();
	}

    //all-args constructor
    /////////////////////
	public Notification(Long id, Long userid, String notificatioType, String module,
			Long moduleId, String message, String readStatus,LocalDateTime startDate, 
			LocalDateTime endDate, boolean reminderStatus,LocalDateTime reminderDate) {
		super();
		this.id = id;
		this.userid = userid;
		this.notificationType = notificatioType;
		this.module = module;
		this.moduleId = moduleId;
		this.message = message;
		this.readStatus = readStatus;
		this.startDate = startDate;
		this.endDate = endDate;
		this.reminderStatus = reminderStatus;
		this.reminderDate = reminderDate;
	}

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

    
}
