package com.retro.dev.dtos;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.retro.dev.models.EStatus;

public class ServiceRequestDTO {
    
	private Long id;
    private Long userid;
    private String name;
    private String description;
    private float price;

    
	//dafault constructor
	////////////////////
    public ServiceRequestDTO() {

    }

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completiondate;

    private String message;

    @Enumerated(EnumType.STRING)
    private EStatus status;
    
    
    private Long serviceId;
    private String mediaPaths;
    
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdDate;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastUpdatedDate;
    
	private List<MultipartFile> servicesRequestMedias;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public LocalDateTime getCompletiondate() {
		return completiondate;
	}

	public void setCompletiondate(LocalDateTime completiondate) {
		this.completiondate = completiondate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public EStatus getStatus() {
		return status;
	}

	public void setStatus(EStatus status) {
		this.status = status;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public String getMediaPaths() {
		return mediaPaths;
	}

	public void setMediaPaths(String mediaPaths) {
		this.mediaPaths = mediaPaths;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public List<MultipartFile> getServicesRequestMedias() {
		return servicesRequestMedias;
	}

	public void setServicesRequestMedias(List<MultipartFile> servicesRequestMedias) {
		this.servicesRequestMedias = servicesRequestMedias;
	}
    
    
}
