package com.retro.dev.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SearchDTO {

	
	//private Attributes
	////////////////////
	
	private Long serviceId;
	private Long userId;
	private String companyName;
	private String serviceName;
	private String serviceDesc;
	private Long categoryId;
	private String serviceLoc;
	private double longitude;
	private double latitude;
	private char isPrivate;
	private String serviceMediaLinks;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createdDate;
	
	private Long createdBy;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastUpdatedDate;
	
	private Long lastUpdatedBy;
	private String isUserCategoryFlag;
	private float servicePrice;
	private float serviceRating;
	
	private String serviceIconImg;
    private String serviceCondition;
    private String isProvider;
	private float priceTo;
	private Long orderId;
	
	//User Details
    private String userName;
	private String name;
	private String profileImage;
	
	
	//Category Details
	 private String category;
	 private String subcategory;
	 
	//Discount Details
	 private Double discount;
	 
	 
	//public Attributes
	////////////////////
	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	public String getServiceLoc() {
		return serviceLoc;
	}

	public void setServiceLoc(String serviceLoc) {
		this.serviceLoc = serviceLoc;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public char getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(char isPrivate) {
		this.isPrivate = isPrivate;
	}

	public String getServiceMediaLinks() {
		return serviceMediaLinks;
	}

	public void setServiceMediaLinks(String serviceMediaLinks) {
		this.serviceMediaLinks = serviceMediaLinks;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.lastUpdatedDate = updatedDate;
	}

	public Long getlastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setlastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getIsUserCategoryFlag() {
		return isUserCategoryFlag;
	}

	public void setIsUserCategoryFlag(String isUserCategoryFlag) {
		this.isUserCategoryFlag = isUserCategoryFlag;
	}

	public float getServicePrice() {
		return servicePrice;
	}

	public void setServicePrice(float servicePrice) {
		this.servicePrice = servicePrice;
	}

	public float getServiceRating() {
		return serviceRating;
	}

	public void setServiceRating(float serviceRating) {
		this.serviceRating = serviceRating;
	}

	public String getServiceIconImg() {
		return serviceIconImg;
	}

	public void setServiceIconImg(String serviceIconImg) {
		this.serviceIconImg = serviceIconImg;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public String getServiceCondition() {
		return serviceCondition;
	}

	public void setServiceCondition(String serviceCondition) {
		this.serviceCondition = serviceCondition;
	}

	public String getIsProvider() {
		return isProvider;
	}

	public void setIsProvider(String isProvider) {
		this.isProvider = isProvider;
	}

	public float getPriceTo() {
		return priceTo;
	}

	public void setPriceTo(float priceTo) {
		this.priceTo = priceTo;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}


}
