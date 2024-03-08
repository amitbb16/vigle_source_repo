package com.retro.dev.dtos;

import java.time.LocalDateTime;

public class ReviewDTO {
    
	//Review Details
	private Long id;
    private Long userid;
    private Long serviceid;
    private Long chooseOrderId;
    private Long serviceRequestId;
    private Long discountId;
    private String comment;
    private float rating;
	private LocalDateTime createdDate;
	private LocalDateTime lastUpdatedDate;
    
    //User Details
    private String userName;
	private String name;
	private String profileImage;
    

    
    public ReviewDTO() {
		super();
	}


	public ReviewDTO( Long userid, Long serviceid,  String comment, float rating) {
        this.userid = userid;
        this.serviceid = serviceid;
        this.comment = comment;
        this.rating = rating;
    }


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


	public Long getServiceid() {
		return serviceid;
	}


	public void setServiceid(Long serviceid) {
		this.serviceid = serviceid;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public float getRating() {
		return rating;
	}


	public void setRating(float rating) {
		this.rating = rating;
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


	public Long getChooseOrderId() {
		return chooseOrderId;
	}


	public void setChooseOrderId(Long chooseOrderId) {
		this.chooseOrderId = chooseOrderId;
	}


	public Long getServiceRequestId() {
		return serviceRequestId;
	}


	public void setServiceRequestId(Long serviceRequestId) {
		this.serviceRequestId = serviceRequestId;
	}


	public Long getDiscountId() {
		return discountId;
	}


	public void setDiscountId(Long discountId) {
		this.discountId = discountId;
	}
    
    
}
