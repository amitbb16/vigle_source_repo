package com.retro.dev.models;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author KumarSourav
 *
 *This Entity class is actually 
 *used to store Business(Offers)
 *data i.e when a service provider 
 *accepts a User's Order
 *
 */


@Data
@AllArgsConstructor
@Entity
@Table(name = "chooseOrder")
public class ChooseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userid;
    
    @Size(max = 200)
    private String name;

    @NotBlank
    @Size(max = 500)
    private String description;


    private float price;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completiondate;

    @NotBlank
    @Size(max = 600)
    private String message;

    @Enumerated(EnumType.STRING)
    private EStatus status;
    
    private int discountPercentage;
    private Long serviceId;
    private String mediaPaths;
    
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdDate;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastUpdatedDate;
	
    //private String isChooseOrderFlag;
    

	public ChooseOrder() {
		super();
	}

	public ChooseOrder(Long id, String name, String description,
                          float price, LocalDateTime completiondate,
                          String message, EStatus status, Long serviceId, 
                          String mediaPaths, int discountPercentage) {
        this.userid = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.completiondate = completiondate;
        this.message = message;
        this.status = status;
        this.serviceId = serviceId;
        this.mediaPaths = mediaPaths;
        this.discountPercentage = discountPercentage;
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

	/*public String getIsChooseOrderFlag() {
		return isChooseOrderFlag;
	}

	public void setIsChooseOrderFlag(String isChooseOrderFlag) {
		this.isChooseOrderFlag = isChooseOrderFlag;
	}
    */
    
}
