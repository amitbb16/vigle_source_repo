package com.retro.dev.security.services;

import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.retro.dev.dtos.BusinessServicesDTO;
import com.retro.dev.dtos.NotificationDTO;
import com.retro.dev.models.ChooseOrder;
import com.retro.dev.models.Notification;
import com.retro.dev.models.User;
import com.retro.dev.payload.request.NotificationRequest;
import com.retro.dev.payload.response.BusinessServicesResponse;
import com.retro.dev.payload.response.MessageResponse;
import com.retro.dev.payload.response.NotificationResponse;
import com.retro.dev.repository.BusinessServicesRepository;
import com.retro.dev.repository.ChooseOrderRepository;
import com.retro.dev.repository.SubCategoryRepository;
import com.retro.dev.repository.UserCategoryRepository;
import com.retro.dev.repository.UserRepository;
import com.retro.dev.util.DevUtils;
import com.retro.dev.util.PublicConstants;

@Service
public class ChooseOrderService {
    @Autowired
    ChooseOrderRepository chooseOrderRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    CategoryService categoryService;
    
    @Autowired
    UserCategoryRepository userCategoryDao;
    
    @Autowired
    SubCategoryRepository subCategoryDao;
    
    @Autowired
    DevUtils utils;
    
	@Autowired
	BusinessServicesRepository userOrdersDao;

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private Environment env;

	@Value("${app.default-email-sender}")
	private String defaultMailSender;

    
    @Autowired
    NotificationService notificationService;
	

    
    private static final Logger logger = LoggerFactory.getLogger(BusinessService.class);
    
    public ResponseEntity<?> createProviderOrder(String username, ChooseOrder chooseOrderRequest, HttpServletRequest httpRequest) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user==null||!user.isPresent()) {
        	return ResponseEntity.ok(new MessageResponse("Order creation failed as the UserName is not found."));
		}
        /*ChooseOrder newServiceRequest = new ChooseOrder(user.get().getId(),
                chooseOrderRequest.getName(), chooseOrderRequest.getDescription(),
                chooseOrderRequest.getPrice(), chooseOrderRequest.getCompletiondate(),
                chooseOrderRequest.getMessage(), chooseOrderRequest.getStatus(), chooseOrderRequest.getServiceId(),
                chooseOrderRequest.getMediaPaths(), chooseOrderRequest.getDiscountPercentage());*/
        chooseOrderRequest = chooseOrderRepository.save(chooseOrderRequest);
        if (chooseOrderRequest != null) {
        	try {
				mailSender.send(sendConfirmationEmail(httpRequest.getLocale(), user.get(), chooseOrderRequest));
				generateNotification(user, chooseOrderRequest);
			} catch (MailException e) {
				logger.error("Failed to send confirmation mail");
				e.printStackTrace();
			}
        	return ResponseEntity.ok(new MessageResponse("Order chosen"));
        }
        else
            return ResponseEntity.ok(new MessageResponse("failed to choose this order."));
    }

	private SimpleMailMessage sendConfirmationEmail(final Locale locale, final User user, ChooseOrder newServiceRequest) {
		final String message = "The Order "+newServiceRequest.getName()+ " is confirmed succesfully. Your reference Id is: "+newServiceRequest.getId();
		return constructEmail("Order confirmation for "+newServiceRequest.getName(), message + " \r\n", user);
	}
	
    private SimpleMailMessage constructEmail(String subject, String body, User user) {
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject);
		email.setText(body);
		email.setTo(user.getEmail());
		email.setFrom(defaultMailSender);
		return email;
	}

    public ResponseEntity<?> updateProviderOrder(String username, ChooseOrder request, HttpServletRequest httpRequest) {
    	Optional<User> user = userRepository.findByUsername(username);
    	ChooseOrder chOrderRequest = chooseOrderRepository.findById(request.getId());
    	Long tempoId = chOrderRequest.getId();
    	BeanUtils.copyProperties(request, chOrderRequest, utils.getNullPropertyNames(request));
    	chOrderRequest.setId(tempoId);
    	//can set updated date here//
        ChooseOrder dealres = chooseOrderRepository.save(chOrderRequest);
        if (dealres != null) {
        	try {
				mailSender.send(constructEmail("Your Order just got updated!", "Your Order with OrderId: "+tempoId+" got updated.", user.get()));
			} catch (MailException e) {
				logger.error("Failed to send confirmation mail");
				e.printStackTrace();
			}
        	return ResponseEntity.ok("Your order updated");
        }
        else
            return ResponseEntity.badRequest().body("failed to update your order");
    }


    public List<ChooseOrder> getCurrentProviderOrderList(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return chooseOrderRepository.findAllByUserid(user.get().getId());

    }

	public Object getProviderOrderDetails(Principal principal, Long id) {
		return chooseOrderRepository.findById(id);
	}
	

	public Object getAllProvidersOrderList(String name) {
		 return chooseOrderRepository.findAll();
	}

    
	public BusinessServicesResponse generateFailResponse(String msg) {
		BusinessServicesResponse response = new BusinessServicesResponse();
		BusinessServicesDTO bServiceDetail = new BusinessServicesDTO();
		response.setStatus(PublicConstants.FAIL);
		response.setRespDesc(msg);
		response.setBusinessServices(bServiceDetail);
		return response;
	}
	
	public BusinessServicesResponse generateSuccessResponse(String msg, BusinessServicesDTO bServiceDetail, BusinessServicesResponse response) {
		response.setStatus(PublicConstants.SUCCESS);
		response.setRespDesc(msg);
		response.setBusinessServices(bServiceDetail);
		return response;
	}
	
    private void generateNotification(Optional<User> user, ChooseOrder chooseOrderRequest) {
    	int notificationRetryCount = 0;
    	NotificationRequest userNotificationRequest = new NotificationRequest();
    	userNotificationRequest = formNotificationRequest(user, chooseOrderRequest);
    	NotificationResponse response = notificationService.createUserNotification(user.get().getName(), userNotificationRequest);
    	if(notificationRetryCount<2 && !response.getResponseStatus().equals(PublicConstants.SUCCESS)) {
    		notificationRetryCount++;
    		generateNotification(user, chooseOrderRequest);
    	}
	}

	private NotificationRequest formNotificationRequest(Optional<User> user, ChooseOrder chooseOrderRequest) {
		NotificationRequest userNotificationRequest = new NotificationRequest();
		NotificationDTO notificationDTO = new NotificationDTO();
		notificationDTO.setUserid(user.get().getId());
		notificationDTO.setNotificationType(PublicConstants.NOTIFICATION_TYPE_ACTION);
		notificationDTO.setMessage("Please choose to Accept or Reject the Order");
		notificationDTO.setModule(PublicConstants.NOTIFICATION_MODULE_ORDER);
		notificationDTO.setModuleId(chooseOrderRequest.getId());
		notificationDTO.setReadStatus(PublicConstants.NO);
		notificationDTO.setStartDate(utils.getLocalDateTime());
		notificationDTO.setEndDate(chooseOrderRequest.getCompletiondate());
		notificationDTO.setReminderStatus(false);
		return userNotificationRequest;
	}


}
