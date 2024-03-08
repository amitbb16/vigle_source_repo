package com.retro.dev.payload.request;

import com.retro.dev.dtos.NotificationDTO;


public class NotificationRequest extends PaginationGenericRequest{
    
	//private attribs
	//////////////////
	private NotificationDTO notification;
    

	//public attribs
	//////////////////

	public NotificationDTO getNotification() {
		return notification;
	}


	public void setNotification(NotificationDTO notification) {
		this.notification = notification;
	}

    
}
