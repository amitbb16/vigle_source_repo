package com.retro.dev.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service")
/*uniqueConstraints = { 
		@UniqueConstraint(columnNames = "serviceid") 
	})*/
public class BusinessServices {

	
	//private Attributes
	////////////////////
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long serviceId;
	
	//@NotBlank
	/*@OneToOne(fetch = FetchType.LAZY)
	@JoinTable(	name = "users", 
				joinColumns = @JoinColumn(name = "service_user_id"), 
				inverseJoinColumns = @JoinColumn(name = "user_id"))*/
	private Long userId;
	
	@NotBlank
	private String companyName;
	
	@NotBlank
	private String serviceName;
	
	@NotBlank
    @Size(max = 600)
	private String serviceDesc;
	
	
	//@NotBlank
	/*@OneToOne(fetch = FetchType.LAZY)
	@JoinTable(	name = "category", 
				joinColumns = @JoinColumn(name = "category_id"), 
				inverseJoinColumns = @JoinColumn(name = "id"))*/
	private Long categoryId;
	
	private String serviceLoc;
	
	private double longitude;
	
	private double latitude;
	
	private char isPrivate;
	
    @Size(max = 600)
	private String serviceMediaLinks;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createdDate;
	
	private Long createdBy;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastUpdatedDate;
	
	
	@JsonIgnore
	private Long lastUpdatedBy;
	
	//@NotBlank
	private float servicePrice;
	
	@Column(columnDefinition = "float default 0.0")
	private float serviceRating = 0.0f;

    @Size(max = 500)
    private String serviceCondition;
    
    private String isProvider;
	private float priceTo;
	
	
	//default (no-arg) constructor
	/////////////////////
	public BusinessServices() {
		super();
	}

	//arg constructor
	/////////////////////
	/*public BusinessServices(Long serviceId, Long user, @NotBlank String companyName,
			@NotBlank String serviceName, @NotBlank String serviceDesc, Long category,
			double longitude, double latitude) {
		super();
		this.serviceId = serviceId;
		this.userId = user;
		this.companyName = companyName;
		this.serviceName = serviceName;
		this.serviceDesc = serviceDesc;
		this.categoryId = category;
		this.longitude = longitude;
		this.latitude = latitude;
	}*/
	
	
	//all-arg constructor
	/////////////////////
	/*public BusinessServices(Long serviceId, Long user, @NotBlank String companyName,
			@NotBlank String serviceName, @NotBlank String serviceDesc, Long category, String serviceLoc,
			double longitude, double latitude, char isPrivate, String serviceMediaLinks,
			Date createdDate, Long createdBy, Date updatedDate,
			Long lastUpdatedBy) {
		super();
		this.serviceId = serviceId;
		this.userId = user;
		this.companyName = companyName;
		this.serviceName = serviceName;
		this.serviceDesc = serviceDesc;
		this.categoryId = category;
		this.serviceLoc = serviceLoc;
		this.longitude = longitude;
		this.latitude = latitude;
		this.isPrivate = isPrivate;
		this.serviceMediaLinks = serviceMediaLinks;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.lastUpdatedDate = updatedDate;
		this.lastUpdatedBy = lastUpdatedBy;
	}*/

	
	//public Attributes
	////////////////////
	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	/*public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}*/

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

/*	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}*/
	

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


}
