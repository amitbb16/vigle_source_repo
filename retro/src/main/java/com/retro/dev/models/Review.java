package com.retro.dev.models;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userid;
    private Long serviceid;
    private Long chooseOrderId;
    private Long serviceRequestId;
    private Long discountId;

    @NotBlank
    @Size(max = 200)
    private String comment;

    private float rating;
    
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdDate;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastUpdatedDate;

    
    public Review() {
		super();
	}


	public Review(Long userid, Long serviceid, Long chooseOrderId, Long serviceRequestId, Long discountId,
			@NotBlank @Size(max = 200) String comment, float rating, LocalDateTime createdDate,
			LocalDateTime lastUpdatedDate) {
		super();
		this.userid = userid;
		this.serviceid = serviceid;
		this.chooseOrderId = chooseOrderId;
		this.serviceRequestId = serviceRequestId;
		this.discountId = discountId;
		this.comment = comment;
		this.rating = rating;
		this.createdDate = createdDate;
		this.lastUpdatedDate = lastUpdatedDate;
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
