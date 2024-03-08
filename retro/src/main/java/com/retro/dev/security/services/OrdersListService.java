package com.retro.dev.security.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.retro.dev.dtos.UserOrdersListDTO;
import com.retro.dev.models.BusinessServices;
import com.retro.dev.models.ChooseOrder;
import com.retro.dev.models.Discount;
import com.retro.dev.models.EStatus;
import com.retro.dev.models.Review;
import com.retro.dev.models.ServiceRequest;
import com.retro.dev.models.User;
import com.retro.dev.payload.request.UserOrdersListRequest;
import com.retro.dev.payload.response.BusinessOffersListResponse;
import com.retro.dev.payload.response.UserOrdersListResponse;
import com.retro.dev.payload.response.UserResponse;
import com.retro.dev.repository.BusinessServicesRepository;
import com.retro.dev.repository.ChooseOrderRepository;
import com.retro.dev.repository.DiscountRepository;
import com.retro.dev.repository.ReviewRepository;
import com.retro.dev.repository.ServiceRequestRepository;
import com.retro.dev.repository.UserRepository;
import com.retro.dev.util.DevUtils;
import com.retro.dev.util.PublicConstants;



@Service
public class OrdersListService {
    
	@Autowired
	BusinessServicesRepository businessServiceDao;
    
	@Autowired
    UserRepository userRepository;
    
    @Autowired
    DevUtils utils;
    
    @Autowired
    ChooseOrderRepository chooseOrderRepository;
    
    @Autowired
    ServiceRequestRepository serviceRequestRepository;
    
	@Autowired
    ReviewRepository reviewRepository;
    
	@Autowired
	BusinessServicesRepository userOrdersDao;
	
    
	@Autowired
	DiscountRepository discountDao;

    
    private static final Logger logger = LoggerFactory.getLogger(OrdersListService.class);
    
   
	public UserOrdersListResponse getUserOrdersList(Principal principal, UserOrdersListRequest orderListRequest) {
		UserOrdersListResponse response = new UserOrdersListResponse();
		Optional<User> bServiceUser = userRepository.findByUsername(principal.getName());
    	if(bServiceUser==null||!bServiceUser.isPresent()) {
			return generateBusinessOffersListFailResponse("User not found.");
		}
    	if(null==orderListRequest.getUserOrders().getCompletiondate()) {
    		orderListRequest.getUserOrders().setCompletiondate(utils.getLocalDateTime());
    	}
    	//response = fetchRecordsFromChooseOrderTable(bServiceUser, orderListRequest);
    	response = fetchRecordsFromServiceRequestTable(orderListRequest, response, bServiceUser);
    	response = generateBusinessOffersListSuccessResponse("Orders List feched successfully", response);
		return response;
	}
	

	

	private Pageable setPaginationData(UserOrdersListRequest orderListRequest) {
		//Pageable pageable = PageRequest.of(offerListRequest.getPageNo(), offerListRequest.getPageSize(), Sort.by(offerListRequest.getSortValue()));
		Pageable pageable = PageRequest.of(orderListRequest.getPageNo(), orderListRequest.getPageSize());
		return pageable;
	}

	private UserOrdersListResponse fetchRecordsFromServiceRequestTable(UserOrdersListRequest orderListRequest,
			UserOrdersListResponse response, Optional<User> bServiceUser) {
		Pageable pageable = setPaginationData(orderListRequest);
		response.setActiveOrdersList(new ArrayList<UserOrdersListDTO >());
		response.setCompletedOrdersList(new ArrayList<UserOrdersListDTO >());
		response.setPendingOrdersList(new ArrayList<UserOrdersListDTO >());
		if(!StringUtils.isEmpty(orderListRequest.getUserOrders().getIsPendingStatusFlag())&&
				orderListRequest.getUserOrders().getIsPendingStatusFlag().equalsIgnoreCase("Y")) {
			/*Pageable pageable = PageRequest.of(offerListRequest.getPageNo(), 
					offerListRequest.getPageSize()-response.getActiveOffersList().size());*/	//, Sort.by(offerListRequest.getSortValue())); 
			List<ServiceRequest> serviceRequestsList = serviceRequestRepository.findAllByUseridAndStatus(bServiceUser.get().getId(),
					EStatus.PENDING, pageable);
			if(null!=serviceRequestsList && serviceRequestsList.size()>0) {
				for(ServiceRequest eachServiceReq : serviceRequestsList) {
					if(eachServiceReq.getCompletiondate().toLocalDate().equals(orderListRequest.getUserOrders().getCompletiondate().toLocalDate())) {
						UserOrdersListDTO activeOrderDTO = new UserOrdersListDTO();
						BeanUtils.copyProperties(eachServiceReq, activeOrderDTO);
						activeOrderDTO.setIsChooseOrderFlag("Y");	// to know from which table details are fetched
						addRatings(activeOrderDTO,"serviceRequest");
						response.getPendingOrdersList().add(activeOrderDTO);
					}
				}
			}
		}
		if(!StringUtils.isEmpty(orderListRequest.getUserOrders().getIsCurrentStatusFlag())&&
				orderListRequest.getUserOrders().getIsCurrentStatusFlag().equalsIgnoreCase("Y")) {
			/*Pageable pageable = PageRequest.of(offerListRequest.getPageNo(), 
					offerListRequest.getPageSize()-response.getActiveOffersList().size());*/	//, Sort.by(offerListRequest.getSortValue())); 
			List<ServiceRequest> serviceRequestsList = serviceRequestRepository.findAllByUseridAndStatus(bServiceUser.get().getId(),
					EStatus.ACCEPTED, pageable);
			if(null!=serviceRequestsList && serviceRequestsList.size()>0) {
				for(ServiceRequest eachServiceReq : serviceRequestsList) {
					if(eachServiceReq.getCompletiondate().toLocalDate().equals(orderListRequest.getUserOrders().getCompletiondate().toLocalDate())) {
						UserOrdersListDTO activeOrderDTO = new UserOrdersListDTO();
						BeanUtils.copyProperties(eachServiceReq, activeOrderDTO);
						activeOrderDTO.setIsChooseOrderFlag("Y");	// to know from which table details are fetched
						addRatings(activeOrderDTO,"serviceRequest");
						response.getActiveOrdersList().add(activeOrderDTO);
					}
				}
			}
		}
		if(!StringUtils.isEmpty(orderListRequest.getUserOrders().getIsCompletedStatusFlag())&&
				orderListRequest.getUserOrders().getIsCompletedStatusFlag().equalsIgnoreCase("Y")) {
			/*Pageable pageable = PageRequest.of(offerListRequest.getPageNo(), 
					offerListRequest.getPageSize()-response.getCompletedOffersList().size());*/	//, Sort.by(offerListRequest.getSortValue())); 
			List<ServiceRequest> serviceRequestsList = serviceRequestRepository.findAllByUseridAndStatus(bServiceUser.get().getId(),
					EStatus.COMPLETED, pageable);
			if(null!=serviceRequestsList && serviceRequestsList.size()>0) {
				for(ServiceRequest eachServiceReq : serviceRequestsList) {
					if(eachServiceReq.getCompletiondate().toLocalDate().equals(orderListRequest.getUserOrders().getCompletiondate().toLocalDate())) {
						UserOrdersListDTO completedOrderDTO = new UserOrdersListDTO();
						BeanUtils.copyProperties(eachServiceReq, completedOrderDTO);
						completedOrderDTO.setIsChooseOrderFlag("Y");	// to know from which table details are fetched
						addRatings(completedOrderDTO,"serviceRequest");
						response.getCompletedOrdersList().add(completedOrderDTO);
					}
				}
			}
		}
		return response;
	}

	
	private void addRatings(UserOrdersListDTO dtoObject, String string) {
		switch (string) {
		case "chooseOrder"		:	List<Review> chooseOrderRating = reviewRepository.findAllByUseridAndChooseOrderId(dtoObject.getUserid(), dtoObject.getId());
									for(Review eachReview : chooseOrderRating) {
										if(null!=eachReview && eachReview.getChooseOrderId()!=null) {
											dtoObject.setRating(eachReview.getRating());
										}
									}
									break;
		case "serviceRequest"	:	List<Review> serviceRequestRating = reviewRepository.findAllByUseridAndChooseOrderId(dtoObject.getUserid(), dtoObject.getId());
									for(Review eachReview : serviceRequestRating) {
										if(null!=eachReview && eachReview.getServiceRequestId()!=null) {
											dtoObject.setRating(eachReview.getRating());
										}
									}
									break;
		case "discount"			:	List<Review> discountRating = reviewRepository.findAllByUseridAndChooseOrderId(dtoObject.getUserid(), dtoObject.getId());
									for(Review eachReview : discountRating) {
										if(null!=eachReview && eachReview.getDiscountId()!=null) {
											dtoObject.setRating(eachReview.getRating());
										}
									}
									break;
		}
	}
	
	/*private void addRatings(BusinessOffersListDTO dtoObject) {
		Review rating = reviewRepository.findByUseridAndChooseOrderId(dtoObject.getUserid(), dtoObject.getId());
		if(null!=rating) {
			dtoObject.setRating(rating.getRating());
		}
	}*/

	
	public UserOrdersListResponse generateBusinessOffersListFailResponse(String msg) {
		UserOrdersListResponse response = new UserOrdersListResponse();
		response.setResponseStatus(PublicConstants.FAIL);
		response.setResponseDesc(msg);
		return response;
	}
	
	public UserOrdersListResponse generateBusinessOffersListSuccessResponse(String msg, UserOrdersListResponse response) {
		response.setResponseStatus(PublicConstants.SUCCESS);
		response.setResponseDesc(msg);
		return response;
	}


	public UserOrdersListResponse getUserOrderRequestsList(Principal principal,
			UserOrdersListRequest orderListRequest) {
		UserOrdersListResponse response = new UserOrdersListResponse();
		Optional<User> bServiceUser = userRepository.findByUsername(principal.getName());
    	if(bServiceUser==null||!bServiceUser.isPresent()) {
			return generateBusinessOffersListFailResponse("Business Service User not found.");
		}
    	response = fetchRecordsFromServiceAndChooseOrderTable(bServiceUser, orderListRequest);
		//response.setDiscountedOffersList(discountedOffersList);
		return generateBusinessOffersListSuccessResponse("User Orders List fetched successfully", response);
	}
	
	private UserOrdersListResponse fetchRecordsFromServiceAndChooseOrderTable(Optional<User> bServiceUser, UserOrdersListRequest orderListRequest) {
		UserOrdersListResponse response = new UserOrdersListResponse();
		//Pageable pageable = setPaginationData(orderListRequest);
		List<UserOrdersListDTO> activeOrdersList = new ArrayList<UserOrdersListDTO>();
		List<UserOrdersListDTO> completedOffersList = new ArrayList<UserOrdersListDTO>();
		if(!StringUtils.isEmpty(orderListRequest.getUserOrders().getIsCompletedStatusFlag())&&
				orderListRequest.getUserOrders().getIsCompletedStatusFlag().equalsIgnoreCase("Y")) {
			List<BusinessServices> userOrdersList = userOrdersDao.findAllByUserId(bServiceUser.get().getId());
			for(BusinessServices eachUserOrder : userOrdersList) {
				List<ChooseOrder> chooseOrdersList = chooseOrderRepository.findAllByServiceId(eachUserOrder.getServiceId());
				if(null!=chooseOrdersList && chooseOrdersList.size()>0) {
					for(ChooseOrder eachOrder : chooseOrdersList) {
						if(eachOrder.getCompletiondate().toLocalDate().equals(orderListRequest.getUserOrders().getCompletiondate().toLocalDate())) {
							UserOrdersListDTO completedOrderDTO = new UserOrdersListDTO();
							BeanUtils.copyProperties(eachOrder, completedOrderDTO);
							completedOrderDTO.setIsChooseOrderFlag("Y");	// to know from which table details are fetched
							addRatings(completedOrderDTO, "chooseOrder");
							completedOffersList.add(completedOrderDTO);
						}
					}
				}
			}
		}
		if(!StringUtils.isEmpty(orderListRequest.getUserOrders().getIsPendingStatusFlag())&&
				orderListRequest.getUserOrders().getIsPendingStatusFlag().equalsIgnoreCase("Y")) {
			List<BusinessServices> userOrdersList = userOrdersDao.findAllByUserId(bServiceUser.get().getId());
			for(BusinessServices eachUserOrder : userOrdersList) {
				List<ChooseOrder> chooseOrdersList = chooseOrderRepository.findAllByServiceId(eachUserOrder.getServiceId());
				if(null!=chooseOrdersList && chooseOrdersList.size()>0) {
					for(ChooseOrder eachOrder : chooseOrdersList) {
						if(eachOrder.getCompletiondate().toLocalDate().equals(orderListRequest.getUserOrders().getCompletiondate().toLocalDate())) {
							UserOrdersListDTO pendingOrderDTO = new UserOrdersListDTO();
							BeanUtils.copyProperties(eachOrder, pendingOrderDTO);
							pendingOrderDTO.setIsChooseOrderFlag("Y");	// to know from which table details are fetched
							addRatings(pendingOrderDTO, "chooseOrder");
							activeOrdersList.add(pendingOrderDTO);
						}
					}
				}
			}
		}
		response.setActiveOrdersList(activeOrdersList);
		response.setCompletedOrdersList(completedOffersList);
		return response;
	}




	public UserOrdersListResponse getUserDiscountList(Principal principal, UserOrdersListRequest orderListRequest) {
		UserOrdersListResponse response = new UserOrdersListResponse();
			Optional<User> bServiceUser = userRepository.findByUsername(principal.getName());
	    	if(bServiceUser==null||!bServiceUser.isPresent()) {
				return generateBusinessOffersListFailResponse("User not found.");
			}
			List<UserOrdersListDTO> discountedOffersList = new ArrayList<UserOrdersListDTO>();
			List<Discount> bUsersDiscounts = discountDao.findAllByUserid(bServiceUser.get().getId());
			if(null!=bUsersDiscounts && bUsersDiscounts.size()>0) {
				for(Discount eachDiscountObj : bUsersDiscounts) {
					if((eachDiscountObj.getFromdate().toLocalDate().compareTo(orderListRequest.getUserOrders().getFromdate().toLocalDate())>0
							|| eachDiscountObj.getFromdate().toLocalDate().compareTo(orderListRequest.getUserOrders().getFromdate().toLocalDate())==0)
						&& (eachDiscountObj.getTodate().toLocalDate().compareTo(orderListRequest.getUserOrders().getTodate().toLocalDate())==0
							||eachDiscountObj.getTodate().toLocalDate().compareTo(orderListRequest.getUserOrders().getTodate().toLocalDate())<0)
						&& eachDiscountObj.getEstatus().equals(EStatus.PENDING)) {
						UserOrdersListDTO discountDTO = new UserOrdersListDTO();
						BeanUtils.copyProperties(eachDiscountObj, discountDTO);
						discountDTO.setPrice(eachDiscountObj.getActualprice().floatValue());
						addRatings(discountDTO,"discount");
						addMediaPaths(discountDTO);
						discountedOffersList.add(discountDTO);
					}
				}
			}
			response.setDiscountedOffersList(discountedOffersList);
			return generateBusinessOffersListSuccessResponse("Discount List fetched successfully", response);
		
	}

	private void addMediaPaths(UserOrdersListDTO discountDTO) {
		Optional<BusinessServices> services = businessServiceDao.findById(discountDTO.getServiceId());
		if(null!=services && services.isPresent()) {
			discountDTO.setMediaPaths(services.get().getServiceMediaLinks());
		}
	}

	public BusinessOffersListResponse generateBusinessOffersListSuccessResponse(String msg, BusinessOffersListResponse response) {
		response.setResponseStatus(PublicConstants.SUCCESS);
		response.setResponseDesc(msg);
		return response;
	}




	public UserResponse getUserOrdersCount(User user, UserResponse response) {
		int OrdersCount = 0;
		List<ServiceRequest> serviceRequestsList = serviceRequestRepository.findAllByUseridAndStatus(user.getId(),
				EStatus.PENDING, null);
		OrdersCount = serviceRequestsList!=null?serviceRequestsList.size():OrdersCount;
		response.setOrdersDone(OrdersCount);
		return response;
	}
	
}
