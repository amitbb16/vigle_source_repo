package com.retro.dev.payload.response;

import java.util.List;

import com.retro.dev.dtos.NotificationDTO;

public class NotificationResponse extends PaginationGenericResponse{

    

	//private attributes
    ////////////////////
    private List<NotificationDTO> userNotificationsList;
    private NotificationDTO userNotificationDetails;
    
    //public attributes
    ////////////////////
	public List<NotificationDTO> getUserNotificationsList() {
		return userNotificationsList;
	}
	public void setUserNotificationsList(List<NotificationDTO> userNotificationsList) {
		this.userNotificationsList = userNotificationsList;
	}
	public NotificationDTO getUserNotificationDetails() {
		return userNotificationDetails;
	}
	public void setUserNotificationDetails(NotificationDTO userNotificationDetails) {
		this.userNotificationDetails = userNotificationDetails;
	}
    
}
