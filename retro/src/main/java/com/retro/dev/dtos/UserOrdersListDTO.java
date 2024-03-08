package com.retro.dev.dtos;

import java.time.LocalDateTime;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.retro.dev.models.EStatus;


public class UserOrdersListDTO {

	private Long id;
    private Long userid;
    private String name;
    private String description;
    private float price;
    private Double discountprice;
    private String message;
    private int discountPercentage;
    private Long serviceId;
    private String mediaPaths;
    private String isPendingStatusFlag;
    private String isCurrentStatusFlag;
    private String isCompletedStatusFlag;
    private float rating;
    private String isChooseOrderFlag;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completiondate;


    @Enumerated(EnumType.STRING)
    private EStatus status;
    
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdDate;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastUpdatedDate;
    
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fromdate;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime todate;


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

	public int getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(int discountPercentage) {
		this.discountPercentage = discountPercentage;
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

	public String getIsPendingStatusFlag() {
		return isPendingStatusFlag;
	}

	public void setIsPendingStatusFlag(String isPendingStatusFlag) {
		this.isPendingStatusFlag = isPendingStatusFlag;
	}

	public String getIsCompletedStatusFlag() {
		return isCompletedStatusFlag;
	}

	public void setIsCompletedStatusFlag(String isCompletedStatusFlag) {
		this.isCompletedStatusFlag = isCompletedStatusFlag;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getIsChooseOrderFlag() {
		return isChooseOrderFlag;
	}

	public void setIsChooseOrderFlag(String isChooseOrderFlag) {
		this.isChooseOrderFlag = isChooseOrderFlag;
	}

	public LocalDateTime getFromdate() {
		return fromdate;
	}

	public void setFromdate(LocalDateTime fromdate) {
		this.fromdate = fromdate;
	}

	public LocalDateTime getTodate() {
		return todate;
	}

	public void setTodate(LocalDateTime todate) {
		this.todate = todate;
	}

	public Double getDiscountprice() {
		return discountprice;
	}

	public void setDiscountprice(Double discountprice) {
		this.discountprice = discountprice;
	}

	public String getIsCurrentStatusFlag() {
		return isCurrentStatusFlag;
	}

	public void setIsCurrentStatusFlag(String isCurrentStatusFlag) {
		this.isCurrentStatusFlag = isCurrentStatusFlag;
	}
    
    
}
