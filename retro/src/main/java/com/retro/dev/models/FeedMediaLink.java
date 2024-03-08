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
@Table(name = "feed_media_link")
public class FeedMediaLink {
	
	//private Attributes
	/////////////////////
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feed_media_link_id;
	
    private Long feedId;

    @NotBlank
    @Size(max = 255)
    private String mediaLink;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdDate;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastUpdatedDate;

	public Long getFeed_media_link_id() {
		return feed_media_link_id;
	}

	public Long getFeedId() {
		return feedId;
	}

	public String getMediaLink() {
		return mediaLink;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public LocalDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setFeed_media_link_id(Long feed_media_link_id) {
		this.feed_media_link_id = feed_media_link_id;
	}

	public void setFeedId(Long feedId) {
		this.feedId = feedId;
	}

	public void setMediaLink(String mediaLink) {
		this.mediaLink = mediaLink;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	
}
