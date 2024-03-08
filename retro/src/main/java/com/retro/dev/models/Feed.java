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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feed")
public class Feed {
	
	//private Attributes
	/////////////////////
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedId;

    private Long userId;

    @NotBlank
    @Size(max = 255)
    private String visibility;

    @NotBlank
    @Size(max = 255)
    private String type;

    @NotBlank
    @Size(max = 8000)
    private String feedData;
    
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdDate;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastUpdatedDate;

	private double longitude;
	
	private double latitude;

	public Long getFeedId() {
		return feedId;
	}

	public Long getUserId() {
		return userId;
	}

	public String getVisibility() {
		return visibility;
	}

	public String getType() {
		return type;
	}

	public String getFeedData() {
		return feedData;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public LocalDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setFeedId(Long feedId) {
		this.feedId = feedId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setFeedData(String feedData) {
		this.feedData = feedData;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
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
}
