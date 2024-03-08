package com.retro.dev.security.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.retro.dev.dtos.NotificationDTO;
import com.retro.dev.models.Discount;
import com.retro.dev.models.ServiceRequest;
import com.retro.dev.models.User;
import com.retro.dev.payload.request.NotificationRequest;
import com.retro.dev.repository.DiscountRepository;
import com.retro.dev.repository.ServiceRequestRepository;
import com.retro.dev.repository.UserRepository;
import com.retro.dev.util.DevUtils;
import com.retro.dev.util.PublicConstants;

@Service
public class DiscountService {
    
	@Autowired
    DiscountRepository discountRepository;
	
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    ServiceRequestRepository serviceRequestRepository;
    
    @Autowired
    DevUtils utils;

    @Autowired
    NotificationService notificationService;
	
    

    public ResponseEntity<?> save(String username, Discount discount) {
        Optional<User> user = userRepository.findByUsername(username);
        Discount newDiscount = new Discount(user.get().getId(), discount.getName(), discount.getServiceId(),
                discount.getActualprice(), discount.getDiscountprice(), discount.getFromdate(),
                discount.getTodate(), discount.getEstatus());
        discountRepository.save(newDiscount);
        List<Long> usersList = fetchUsersListBasedOnServiceId(newDiscount);
        createNotificationsForConnectedUsers(newDiscount, usersList);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }


	public ResponseEntity<?> update(String username, Discount pDiscount) {
        Discount discount = discountRepository.findById(pDiscount.getId());
        discount.setUserid(pDiscount.getUserid());
        discount.setName(pDiscount.getName());
        discount.setServiceId(pDiscount.getServiceId());
        discount.setActualprice(pDiscount.getActualprice());
        discount.setDiscountprice(pDiscount.getDiscountprice());
        discount.setFromdate(pDiscount.getFromdate());
        discount.setTodate(pDiscount.getTodate());
        discount.setEstatus(pDiscount.getEstatus());

        Discount updateDiscount = discountRepository.save(discount);
        if (updateDiscount != null)
            return ResponseEntity.ok("Discount updated");
        else
            return ResponseEntity.badRequest().body("failed to update the category");
    }


    public List<Discount> listDiscount(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return discountRepository.findAllByUserid(user.get().getId());

    }


    private List<Long> fetchUsersListBasedOnServiceId(Discount newDiscount) {
    	List<Long> usersList = new ArrayList<Long>();
    	usersList.addAll(getServiceRequestUsers(newDiscount));
    	//usersList.addAll(getOrdersUsers(newDiscount));
		return usersList;
	}


	private List<Long> getServiceRequestUsers(Discount newDiscount) {
		List<Long> usersList = new ArrayList<Long>();
		List<ServiceRequest> serviceRequestsList = serviceRequestRepository.findAllByServiceId(newDiscount.getServiceId());
		if(null!=serviceRequestsList && serviceRequestsList.size()>0) {
			for(ServiceRequest eachServiceReq : serviceRequestsList) {
				usersList.add(eachServiceReq.getUserid());
			}
		}
		return usersList;
	}
    

	private void createNotificationsForConnectedUsers(Discount newDiscount, List<Long> usersList) {
		if(null!=usersList) {
			for(Long eachUser : usersList) {
				generateNotification(eachUser, newDiscount);
			}
		}
	}


	private void generateNotification(Long eachUser, Discount newDiscount) {
    	int notificationRetryCount = 0;
    	NotificationRequest userNotificationRequest = new NotificationRequest();
    	userNotificationRequest = formNotificationRequest(eachUser, newDiscount);
    	String response = notificationService.createUserNotificationForOtherModules(userNotificationRequest);
    	if(notificationRetryCount<2 && !response.equals(PublicConstants.SUCCESS)) {
    		notificationRetryCount++;
    		generateNotification(eachUser, newDiscount);
    	}
	}

	private NotificationRequest formNotificationRequest(Long eachUser, Discount newDiscount) {
		NotificationRequest userNotificationRequest = new NotificationRequest();
		NotificationDTO notificationDTO = new NotificationDTO();
		notificationDTO.setUserid(eachUser);
		notificationDTO.setNotificationType(PublicConstants.NOTIFICATION_TYPE_ACTION);
		notificationDTO.setMessage("Product price discounted! Check now.");
		notificationDTO.setModule(PublicConstants.NOTIFICATION_MODULE_DISCOUNT);
		notificationDTO.setModuleId(newDiscount.getId());
		notificationDTO.setReadStatus(PublicConstants.NO);
		notificationDTO.setStartDate(utils.getLocalDateTime());	//ignoring fetching from fromDate as the discount entry just got created
		notificationDTO.setEndDate(newDiscount.getTodate());
		notificationDTO.setReminderStatus(false);
		return userNotificationRequest;
	}


}
