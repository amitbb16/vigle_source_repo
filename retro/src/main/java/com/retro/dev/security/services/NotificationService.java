package com.retro.dev.security.services;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.retro.dev.dtos.NotificationDTO;
import com.retro.dev.models.ChooseOrder;
import com.retro.dev.models.EStatus;
import com.retro.dev.models.Notification;
import com.retro.dev.models.ServiceRequest;
import com.retro.dev.models.User;
import com.retro.dev.payload.request.NotificationRequest;
import com.retro.dev.payload.response.NotificationResponse;
import com.retro.dev.repository.ChooseOrderRepository;
import com.retro.dev.repository.NotificationRepository;
import com.retro.dev.repository.ServiceRequestRepository;
import com.retro.dev.repository.UserRepository;
import com.retro.dev.util.DevUtils;
import com.retro.dev.util.PublicConstants;



@Service
public class NotificationService {
    
    
	@Autowired
    UserRepository userRepository;
    
	@Autowired
	NotificationRepository notificationDao;
    
    @Autowired
    DevUtils utils;
    
    @Autowired
    ServiceRequestRepository serviceRequestRepository;
    
    @Autowired
    ChooseOrderRepository chooseOrderRepository;
    
    

	@Value("${notification.daysForCompletetion}")
	private String daysForCompletetion;

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private static Map<Long, LinkedHashSet<Notification>> notificationMap = new LinkedHashMap<Long, LinkedHashSet<Notification>>();
    															/*use this map to store userId (key) and notificationSet (value)*/
    private static Set<Notification> notificationSet = new LinkedHashSet<Notification>();	
    															/* use this set to store and retrieve notification 
    															instead of hitting DB every time (maybe later stage of code development). 
    															while creating a notification, store at both places- DB and this set.
    															Since this instance will be applicable for all Users, need to iterate
    															this set and fetch based on User*/


	public NotificationResponse getUserNotificationsList(Principal principal) {
		NotificationResponse response = new NotificationResponse();
		Optional<User> userDTO = userRepository.findByUsername(principal.getName());
    	if(userDTO==null||!userDTO.isPresent()) {
			return generateNotificationFailResponse("User not found.");
		}
    	List<Notification> userNotifications = notificationDao.findAllByUserid(userDTO.get().getId());
    	List<NotificationDTO> notificationDTOs = new ArrayList<NotificationDTO>();
    	if(null!=userNotifications) {
	    	for(Notification eachNotification : userNotifications) {
	    		NotificationDTO eachNotificationDTO = new NotificationDTO();
	    		BeanUtils.copyProperties(eachNotification, eachNotificationDTO);
	    		eachNotificationDTO.setUserdata(userDTO.get());
	    		notificationDTOs.add(eachNotificationDTO);
	    	}
	    	response = generateNotificationSuccessResponse("Successfully fetched User Notifications List", response);
    	} else {
    		response = generateNotificationSuccessResponse("No new notifications for this User", response);
    	}
    	response.setUserNotificationsList(notificationDTOs);
		return response;
	}


	public NotificationResponse getNotificationDetails(Principal principal, Long notificationId) {
		NotificationResponse response = new NotificationResponse();
		Optional<User> userDTO = userRepository.findByUsername(principal.getName());
    	if(userDTO==null||!userDTO.isPresent()) {
			return generateNotificationFailResponse("User not found.");
		}
    	if(notificationId==null) {
			return generateNotificationFailResponse("Please provide a valid Notification Id.");
		}
    	Notification userNotification = notificationDao.findById(notificationId);
    	NotificationDTO userNotificationDTO = new NotificationDTO();
    	if(null!=userNotification) {
    		BeanUtils.copyProperties(userNotification, userNotificationDTO);
    		userNotificationDTO.setUserdata(userDTO.get());
    		response = generateNotificationSuccessResponse("Successfully fetched Notifications details", response);
    	} else {
    		response = generateNotificationSuccessResponse("Notification details not found", response);
    	}
    	response.setUserNotificationDetails(userNotificationDTO);
		return response;
	}


	public NotificationResponse createUserNotification(String userName, NotificationRequest notificationRequest) {
		NotificationResponse response = new NotificationResponse();
		Optional<User> userDTO = userRepository.findByUsername(userName);
    	if(userDTO==null||!userDTO.isPresent()) {
			return generateNotificationFailResponse("User not found.");
		}
    	if(null!=notificationRequest && null!=notificationRequest.getNotification()) {
    		Notification userNotification = new Notification();
    		BeanUtils.copyProperties(notificationRequest.getNotification(), userNotification);
    		userNotification.setUserid(null==userNotification.getUserid()?userDTO.get().getId():userNotification.getUserid());
    		userNotification = notificationDao.save(userNotification);
    		response = generateNotificationSuccessResponse("Notification created successfully", response);
    		response.setUserNotificationDetails(notificationRequest.getNotification());
    	} else {
    		response = generateNotificationSuccessResponse("No Notification data found to insert", response);
    	}
		return response;
	}


	public NotificationResponse updateUserNotification(Principal principal, NotificationRequest notificationRequest) {
		NotificationResponse response = new NotificationResponse();
		Optional<User> userDTO = userRepository.findByUsername(principal.getName());
    	if(userDTO==null||!userDTO.isPresent()) {
			return generateNotificationFailResponse("User not found.");
		}
    	if(null!=notificationRequest && null!=notificationRequest.getNotification() 
    			&& null != notificationRequest.getNotification().getId()) {
    		Long notificationId = notificationRequest.getNotification().getId();
    		Notification userNotification = notificationDao.findById(notificationRequest.getNotification().getId());
    		BeanUtils.copyProperties(notificationRequest.getNotification(), userNotification, utils.getNullPropertyNames(notificationRequest.getNotification()));
    		userNotification.setId(notificationId);
    		userNotification = notificationDao.save(userNotification);
    		response = generateNotificationSuccessResponse("Notification updated successfully", response);
    		response.setUserNotificationDetails(notificationRequest.getNotification());
    	} else {
    		response = generateNotificationSuccessResponse("Nothing to Update", response);
    	}
		return response;
	}

	public NotificationResponse deleteNotification(Principal principal, Long notificationId) {
		NotificationResponse response = new NotificationResponse();
		Optional<User> userDTO = userRepository.findByUsername(principal.getName());
    	if(userDTO==null||!userDTO.isPresent()) {
			return generateNotificationFailResponse("User not found.");
		}
    	if(notificationId==null) {
			return generateNotificationFailResponse("Please provide a valid Notification Id.");
		}
    	notificationDao.deleteById(notificationId.intValue());
    	response = generateNotificationSuccessResponse("Notification deleted successfully", response);
    	NotificationDTO userNotificationDTO = new NotificationDTO();
    	userNotificationDTO.setId(notificationId);
    	response.setUserNotificationDetails(userNotificationDTO);
		return response;
	}
	
	public String createUserNotificationForOtherModules(NotificationRequest notificationRequest) {
    	String response = null;
		if(null!=notificationRequest && null!=notificationRequest.getNotification()) {
    		Notification userNotification = new Notification();
    		BeanUtils.copyProperties(notificationRequest.getNotification(), userNotification);
    		userNotification.setUserid(userNotification.getUserid());
    		userNotification = notificationDao.save(userNotification);
    		response = PublicConstants.SUCCESS;
    	} else {
    		response = "No Notification data found to insert";
    	}
		return response;
	}

/*	//*******************************************************************************************************************************
	 * * * * * * * * * * * * * *
	 * Scheduler Functions Start *
	 * * * * * * * * * * * * * * 
	//*******************************************************************************************************************************
	
	@Scheduled(fixedDelay = 3000, initialDelay = 50000)
	public void scheduleNotificationScanning() {
		logger.info("Scheduler Process started at: "+ utils.getCurrentDateAndTime());
		try {
			//first fetch all notifications from notification table
			Iterable<Notification> allNotifications = notificationDao.findAll();
			List<Notification> allNotificationsList = StreamSupport.stream(allNotifications.spliterator(), false).collect(Collectors.toList());
			if(null!=allNotifications) {
				//second, scan serviceRequest (based on completion date and Status Pending) table to get User's orders data and insert into notification
				generateNotificationFromServiceRequest(allNotificationsList);
				
				//third, scan choose_order table (based on completion date and Status Pending) to get Provider's offers data and insert into notification
				generateNotificationFromChooseOrder(allNotificationsList);
			}
			logger.info("Scheduler Process completed successfully at: "+ utils.getCurrentDateAndTime()+" \n");
		} catch (Exception e) {
			logger.error("Error running scheduler instance at: "+ utils.getCurrentDateAndTime()+" \n error being: "+ e.getMessage());
		}
	}


	private void generateNotificationFromServiceRequest(List<Notification> allNotifications) {
		//adding daysForCompletion to today's date and then fetching the near future orders
		List<ServiceRequest> serviceRequests = serviceRequestRepository.findAllByCompletiondateAndStatus(
				utils.addDaysToProvidedDateTime(utils.getLocalDateTime(),getDaysForCompletetion()), EStatus.ACCEPTED);
		if(null!=serviceRequests) {
			for(ServiceRequest eachServiceReq : serviceRequests) {
				boolean isfoundFlag = false;
				
				//setting flag to check if the required serviceRequest entry is already present in notification table
				for(Notification eachUserNotification : allNotifications) {
					if(null!=eachUserNotification && eachServiceReq.getCompletiondate()!=null && null!=eachUserNotification.getEndDate()
							&& (null!=eachServiceReq.getUserid()&& eachUserNotification.getUserid().toString().equals(eachServiceReq.getUserid().toString())
							&& eachUserNotification.getModuleId().toString().equals(eachServiceReq.getId().toString())
							&& eachUserNotification.getModule().equals(PublicConstants.NOTIFICATION_MODULE_ORDER)
							&& eachUserNotification.getStartDate().toLocalDate().isAfter(utils.subtractDaysToProvidedDateTime(eachServiceReq.getCompletiondate(),
									getDaysForCompletetion()).toLocalDate()))) {
						isfoundFlag = true;
					}
				}
				
				//if the serviceRequest entry is not present in notification table, insert new entry
				if(!isfoundFlag ) {
					if(eachServiceReq.getCompletiondate().toLocalDate().isBefore(utils.addDaysToProvidedDateTime(eachServiceReq.getCompletiondate(),
							getDaysForCompletetion()).toLocalDate())) {
						createGenericNotificationForCompletionDate(eachServiceReq.getUserid(), PublicConstants.NOTIFICATION_MODULE_ORDER, 
								eachServiceReq.getId(), eachServiceReq.getCompletiondate());
					}
				}
				
			}
		}
	}

   
	private void generateNotificationFromChooseOrder(List<Notification> allNotifications) {
		// chooseOrderRepository
		List<ChooseOrder> chooseOrderList = chooseOrderRepository.findAllByCompletiondateAndStatus(
				utils.addDaysToProvidedDateTime(utils.getLocalDateTime(),getDaysForCompletetion()), EStatus.ACCEPTED);
		if(null!=chooseOrderList) {
			for(ChooseOrder eachChooseOrderReq : chooseOrderList) {
				boolean isfoundFlag = false;
				
				//setting flag to check if the required ChooseOrder entry is already present in notification table
				for(Notification eachUserNotification : allNotifications) {
					if(null!=eachUserNotification && eachChooseOrderReq.getCompletiondate()!=null && null!=eachUserNotification.getEndDate()
							&& (null!=eachChooseOrderReq.getUserid()&& eachUserNotification.getUserid().toString().equals(eachChooseOrderReq.getUserid().toString())
							&& eachUserNotification.getModuleId().toString().equals(eachChooseOrderReq.getId().toString())
							&& eachUserNotification.getModule().equals(PublicConstants.NOTIFICATION_MODULE_OFFER)
							&& eachUserNotification.getStartDate().toLocalDate().isAfter(utils.subtractDaysToProvidedDateTime(eachChooseOrderReq.getCompletiondate(),
									getDaysForCompletetion()).toLocalDate()))) {
						isfoundFlag = true;
					}
				}
				
				//if the ChooseOrder entry is not present in notification table, insert new entry
				if(!isfoundFlag ) {
					if(eachChooseOrderReq.getCompletiondate().toLocalDate().isBefore(utils.addDaysToProvidedDateTime(eachChooseOrderReq.getCompletiondate(),
							getDaysForCompletetion()).toLocalDate())) {
						createGenericNotificationForCompletionDate(eachChooseOrderReq.getUserid(), PublicConstants.NOTIFICATION_MODULE_OFFER, 
								eachChooseOrderReq.getId(), eachChooseOrderReq.getCompletiondate());
					}
				}
				
			}
		}
	}


	private void createGenericNotificationForCompletionDate(long userId, String order_offer_type,
			long order_offer_Id, LocalDateTime completiondate) {
    	int notificationRetryCount = 0;
    	NotificationRequest userNotificationRequest = new NotificationRequest();
    	userNotificationRequest = formNotificationRequest(userId, order_offer_type, order_offer_Id, completiondate);
    	NotificationResponse response = createNotificationFromUserId(userId, userNotificationRequest);
    	if(notificationRetryCount<2 && !response.getResponseStatus().equals(PublicConstants.SUCCESS)) {
    		notificationRetryCount++;
    		createGenericNotificationForCompletionDate(userId, order_offer_type, order_offer_Id, completiondate);
    	}else if(notificationRetryCount>=2) {
    		logger.error("failed to create notification for userId: "+userId+" and Order/Offer Id: "+order_offer_Id);
    	}
	}

	private NotificationRequest formNotificationRequest(long userId, String order_offer_type,
			long order_offer_Id, LocalDateTime completiondate) {
		NotificationRequest userNotificationRequest = new NotificationRequest();
		NotificationDTO notificationDTO = new NotificationDTO();
		notificationDTO.setUserid(userId);
		notificationDTO.setNotificationType(PublicConstants.NOTIFICATION_TYPE_ALERT);
		notificationDTO.setMessage("The completion date for the "+order_offer_type+" "+order_offer_Id+" is coming nearer. Check it out now!!");
		notificationDTO.setModule(PublicConstants.NOTIFICATION_MODULE_ORDER);
		notificationDTO.setModuleId(order_offer_Id);
		notificationDTO.setReadStatus(PublicConstants.NO);
		notificationDTO.setStartDate(utils.getLocalDateTime());
		notificationDTO.setEndDate(completiondate);
		notificationDTO.setReminderStatus(false);
		return userNotificationRequest;
	}

	public NotificationResponse createNotificationFromUserId(long userId, NotificationRequest notificationRequest) {
		NotificationResponse response = new NotificationResponse();
		Optional<User> userDTO = userRepository.findById(userId);
    	if(userDTO==null||!userDTO.isPresent()) {
			return generateNotificationFailResponse("User not found.");
		}
    	if(null!=notificationRequest && null!=notificationRequest.getNotification()) {
    		Notification userNotification = new Notification();
    		BeanUtils.copyProperties(notificationRequest.getNotification(), userNotification);
    		userNotification.setUserid(null==userNotification.getUserid()?userDTO.get().getId():userNotification.getUserid());
    		userNotification = notificationDao.save(userNotification);
    		response = generateNotificationSuccessResponse("Notification created successfully", response);
    		response.setUserNotificationDetails(notificationRequest.getNotification());
    	} else {
    		response = generateNotificationSuccessResponse("No Notification data found to insert", response);
    	}
		return response;
	}

	//*******************************************************************************************************************************
	 * * * * * * * * * * * * * *
	 * Scheduler Functions End *
	 * * * * * * * * * * * * * * 
	//*******************************************************************************************************************************
	*/
	private NotificationResponse generateNotificationFailResponse(String msg) {
		NotificationResponse response = new NotificationResponse();
		response.setResponseStatus(PublicConstants.FAIL);
		response.setResponseDesc(msg);
		return response;
	}
	
	private NotificationResponse generateNotificationSuccessResponse(String msg, NotificationResponse response) {
		response.setResponseStatus(PublicConstants.SUCCESS);
		response.setResponseDesc(msg);
		return response;
	}


	public String getDaysForCompletetion() {
		return daysForCompletetion;
	}


	public void setDaysForCompletetion(String daysForCompletetion) {
		this.daysForCompletetion = daysForCompletetion;
	}

	
}
