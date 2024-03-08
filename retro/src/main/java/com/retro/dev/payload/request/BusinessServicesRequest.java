package com.retro.dev.payload.request;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;


public class BusinessServicesRequest extends PaginationGenericRequest{

	
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
	
	private Date createdDate;
	
	private Long createdBy;
	
	
	private Date lastUpdatedDate;
	
	
	private Long lastUpdatedBy;

	private List<MultipartFile> servicesMedias;
	
	private float servicePrice;
	private float serviceRating;
	
    private String serviceCondition;
    
    private String isProvider;
	private float priceTo;
	private Long orderId;
	private Long maxLinks;
	private String type;
	
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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public List<MultipartFile> getServicesMedias() {
		return servicesMedias;
	}

	public void setServicesMedias(List<MultipartFile> servicesMedias) {
		this.servicesMedias = servicesMedias;
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

	public Long getMaxLinks() {
		return maxLinks;
	}

	public void setMaxLinks(Long maxLinks) {
		this.maxLinks = maxLinks;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
