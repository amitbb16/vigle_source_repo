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

import com.retro.dev.dtos.BusinessOffersListDTO;
import com.retro.dev.models.BusinessServices;
import com.retro.dev.models.ChooseOrder;
import com.retro.dev.models.Discount;
import com.retro.dev.models.EStatus;
import com.retro.dev.models.Review;
import com.retro.dev.models.ServiceRequest;
import com.retro.dev.models.User;
import com.retro.dev.payload.request.BusinessOffersListRequest;
import com.retro.dev.payload.response.BusinessOffersListResponse;
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
public class OffersListService {
    
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
	DiscountRepository discountDao;
	

    
    private static final Logger logger = LoggerFactory.getLogger(OffersListService.class);
    
   
	public BusinessOffersListResponse getBusinessOffersList(Principal principal, BusinessOffersListRequest offerListRequest) {
		BusinessOffersListResponse response = new BusinessOffersListResponse();
		Optional<User> bServiceUser = userRepository.findByUsername(principal.getName());
    	if(bServiceUser==null||!bServiceUser.isPresent()) {
			return generateBusinessOffersListFailResponse("Business Service User not found.");
		}
    	if(null==offerListRequest.getBusinessOffers().getCompletiondate()) {
    		offerListRequest.getBusinessOffers().setCompletiondate(utils.getLocalDateTime());
    	}
    	response = fetchRecordsFromChooseOrderTable(bServiceUser, offerListRequest);
    	response = fetchRecordsFromServiceRequestTable(offerListRequest, response, bServiceUser);
    	response = generateBusinessOffersListSuccessResponse("Offers List feched successfully", response);
		return response;
	}
	

	private BusinessOffersListResponse fetchRecordsFromChooseOrderTable(Optional<User> bServiceUser, BusinessOffersListRequest offerListRequest) {
		BusinessOffersListResponse response = new BusinessOffersListResponse();
		Pageable pageable = setPaginationData(offerListRequest);
		List<BusinessOffersListDTO> pendingOffersList = new ArrayList<BusinessOffersListDTO>();
		List<BusinessOffersListDTO> activeOffersList = new ArrayList<BusinessOffersListDTO>();
		List<BusinessOffersListDTO> completedOffersList = new ArrayList<BusinessOffersListDTO>();
		if(!StringUtils.isEmpty(offerListRequest.getBusinessOffers().getIsCompletedStatusFlag())&&
				offerListRequest.getBusinessOffers().getIsCompletedStatusFlag().equalsIgnoreCase("Y")) {
			List<ChooseOrder> chooseOrdersList = chooseOrderRepository.findAllByUseridAndStatus(bServiceUser.get().getId(),
					EStatus.COMPLETED, pageable);
			if(null!=chooseOrdersList && chooseOrdersList.size()>0) {
				for(ChooseOrder eachOrder : chooseOrdersList) {
					if(eachOrder.getCompletiondate().toLocalDate().equals(offerListRequest.getBusinessOffers().getCompletiondate().toLocalDate())) {
						BusinessOffersListDTO completedOrderDTO = new BusinessOffersListDTO();
						BeanUtils.copyProperties(eachOrder, completedOrderDTO);
						completedOrderDTO.setIsChooseOrderFlag("Y");	// to know from which table details are fetched
						addRatings(completedOrderDTO, "chooseOrder");
						completedOffersList.add(completedOrderDTO);
					}
				}
			}
		}
		if(!StringUtils.isEmpty(offerListRequest.getBusinessOffers().getIsPendingStatusFlag())&&
				offerListRequest.getBusinessOffers().getIsPendingStatusFlag().equalsIgnoreCase("Y")) {
			List<ChooseOrder> chooseOrdersList = chooseOrderRepository.findAllByUseridAndStatus(bServiceUser.get().getId(),
					EStatus.PENDING, pageable);
			if(null!=chooseOrdersList && chooseOrdersList.size()>0) {
				for(ChooseOrder eachOrder : chooseOrdersList) {
					if(eachOrder.getCompletiondate().toLocalDate().equals(offerListRequest.getBusinessOffers().getCompletiondate().toLocalDate())) {
						BusinessOffersListDTO pendingOrderDTO = new BusinessOffersListDTO();
						BeanUtils.copyProperties(eachOrder, pendingOrderDTO);
						pendingOrderDTO.setIsChooseOrderFlag("Y");	// to know from which table details are fetched
						addRatings(pendingOrderDTO, "chooseOrder");
						pendingOffersList.add(pendingOrderDTO);
					}
				}
			}
		}
		if(!StringUtils.isEmpty(offerListRequest.getBusinessOffers().getIsActiveStatusFlag())&&
				offerListRequest.getBusinessOffers().getIsActiveStatusFlag().equalsIgnoreCase("Y")) {
			List<ChooseOrder> chooseOrdersList = chooseOrderRepository.findAllByUseridAndStatus(bServiceUser.get().getId(),
					EStatus.ACCEPTED, pageable);
			if(null!=chooseOrdersList && chooseOrdersList.size()>0) {
				for(ChooseOrder eachOrder : chooseOrdersList) {
					if(eachOrder.getCompletiondate().toLocalDate().equals(offerListRequest.getBusinessOffers().getCompletiondate().toLocalDate())) {
						BusinessOffersListDTO activeOrderDTO = new BusinessOffersListDTO();
						BeanUtils.copyProperties(eachOrder, activeOrderDTO);
						activeOrderDTO.setIsChooseOrderFlag("Y");	// to know from which table details are fetched
						addRatings(activeOrderDTO, "chooseOrder");
						activeOffersList.add(activeOrderDTO);
					}
				}
			}
		}
		response.setActiveOffersList(activeOffersList);
		response.setCompletedOffersList(completedOffersList);
		return response;
	}

	private Pageable setPaginationData(BusinessOffersListRequest offerListRequest) {
		//Pageable pageable = PageRequest.of(offerListRequest.getPageNo(), offerListRequest.getPageSize(), Sort.by(offerListRequest.getSortValue()));
		Pageable pageable = PageRequest.of(offerListRequest.getPageNo(), offerListRequest.getPageSize());
		return pageable;
	}

	private BusinessOffersListResponse fetchRecordsFromServiceRequestTable(BusinessOffersListRequest offerListRequest,
			BusinessOffersListResponse response, Optional<User> bServiceUser) {
		if(!StringUtils.isEmpty(offerListRequest.getBusinessOffers().getIsPendingStatusFlag())&&
				offerListRequest.getBusinessOffers().getIsPendingStatusFlag().equalsIgnoreCase("Y")&&
				response.getActiveOffersList().size()<offerListRequest.getPageSize()) {
			/*Pageable pageable = PageRequest.of(offerListRequest.getPageNo(), 
					offerListRequest.getPageSize()-response.getActiveOffersList().size());*/	//, Sort.by(offerListRequest.getSortValue())); 
			List<BusinessServices> userServicesList = businessServiceDao.findAllByUserId(bServiceUser.get().getId());
			if(null!=userServicesList && userServicesList.size()>0) {
				for(BusinessServices eachBService : userServicesList) {
					List<ServiceRequest> serviceRequestsList = serviceRequestRepository.findAllByServiceId(eachBService.getServiceId());
					if(null!=serviceRequestsList && serviceRequestsList.size()>0) {
						for(ServiceRequest eachServiceReq : serviceRequestsList) {
							if(eachServiceReq.getCompletiondate().toLocalDate().equals(offerListRequest.getBusinessOffers().getCompletiondate().toLocalDate())) {
								BusinessOffersListDTO activeOrderDTO = new BusinessOffersListDTO();
								BeanUtils.copyProperties(eachServiceReq, activeOrderDTO);
								activeOrderDTO.setIsChooseOrderFlag("Y");	// to know from which table details are fetched
								addRatings(activeOrderDTO,"serviceRequest");
								response.getActiveOffersList().add(activeOrderDTO);
							}
						}
					}
				}
			}
		}

		if(!StringUtils.isEmpty(offerListRequest.getBusinessOffers().getIsCompletedStatusFlag())&&
				offerListRequest.getBusinessOffers().getIsCompletedStatusFlag().equalsIgnoreCase("Y")&&
				response.getCompletedOffersList().size()<offerListRequest.getPageSize()) {
			/*Pageable pageable = PageRequest.of(offerListRequest.getPageNo(), 
					offerListRequest.getPageSize()-response.getCompletedOffersList().size());*/	//, Sort.by(offerListRequest.getSortValue())); 
			List<BusinessServices> userServicesList = businessServiceDao.findAllByUserId(bServiceUser.get().getId());
			if(null!=userServicesList && userServicesList.size()>0) {
				for(BusinessServices eachBService : userServicesList) {
					List<ServiceRequest> serviceRequestsList = serviceRequestRepository.findAllByServiceId(eachBService.getServiceId());
					if(null!=serviceRequestsList && serviceRequestsList.size()>0) {
						for(ServiceRequest eachServiceReq : serviceRequestsList) {
							if(eachServiceReq.getCompletiondate().toLocalDate().equals(offerListRequest.getBusinessOffers().getCompletiondate().toLocalDate())) {
								BusinessOffersListDTO completedOrderDTO = new BusinessOffersListDTO();
								BeanUtils.copyProperties(eachServiceReq, completedOrderDTO);
								completedOrderDTO.setIsChooseOrderFlag("Y");	// to know from which table details are fetched
								addRatings(completedOrderDTO,"serviceRequest");
								response.getCompletedOffersList().add(completedOrderDTO);
							}
						}
					}
				}
			}
		}
		return response;
	}

	
	private void addRatings(BusinessOffersListDTO dtoObject, String string) {
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

	
	public BusinessOffersListResponse generateBusinessOffersListFailResponse(String msg) {
		BusinessOffersListResponse response = new BusinessOffersListResponse();
		response.setResponseStatus(PublicConstants.FAIL);
		response.setResponseDesc(msg);
		return response;
	}
	
	public BusinessOffersListResponse generateBusinessOffersListSuccessResponse(String msg, BusinessOffersListResponse response) {
		response.setResponseStatus(PublicConstants.SUCCESS);
		response.setResponseDesc(msg);
		return response;
	}


	public BusinessOffersListResponse getBusinessDiscountList(Principal principal,
			BusinessOffersListRequest offerListRequest) {
		BusinessOffersListResponse response = new BusinessOffersListResponse();
		Optional<User> bServiceUser = userRepository.findByUsername(principal.getName());
    	if(bServiceUser==null||!bServiceUser.isPresent()) {
			return generateBusinessOffersListFailResponse("Business Service User not found.");
		}
		List<BusinessOffersListDTO> discountedOffersList = new ArrayList<BusinessOffersListDTO>();
		List<Discount> bUsersDiscounts = discountDao.findAllByUserid(bServiceUser.get().getId());
		if(null!=bUsersDiscounts && bUsersDiscounts.size()>0) {
			for(Discount eachDiscountObj : bUsersDiscounts) {
				if((eachDiscountObj.getFromdate().toLocalDate().compareTo(offerListRequest.getBusinessOffers().getFromdate().toLocalDate())>0
						|| eachDiscountObj.getFromdate().toLocalDate().compareTo(offerListRequest.getBusinessOffers().getFromdate().toLocalDate())==0)
					&& (eachDiscountObj.getTodate().toLocalDate().compareTo(offerListRequest.getBusinessOffers().getTodate().toLocalDate())==0
						||eachDiscountObj.getTodate().toLocalDate().compareTo(offerListRequest.getBusinessOffers().getTodate().toLocalDate())<0)
					&& eachDiscountObj.getEstatus().equals(EStatus.PENDING)) {
					BusinessOffersListDTO discountDTO = new BusinessOffersListDTO();
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


	private void addMediaPaths(BusinessOffersListDTO discountDTO) {
		Optional<BusinessServices> services = businessServiceDao.findById(discountDTO.getServiceId());
		if(null!=services && services.isPresent()) {
			discountDTO.setMediaPaths(services.get().getServiceMediaLinks());
		}
	}


	public UserResponse getBusinessOffersCount(User user, UserResponse response) {
		int OffersCount = 0;
		List<ChooseOrder> chooseOrdersList = chooseOrderRepository.findAllByUseridAndStatus(user.getId(),
				EStatus.COMPLETED, null);
		OffersCount = chooseOrdersList!=null?chooseOrdersList.size():OffersCount;
		List<BusinessServices> userServicesList = businessServiceDao.findAllByUserId(user.getId());
		if(null!=userServicesList && userServicesList.size()>0) {
			for(BusinessServices eachBService : userServicesList) {
				List<ServiceRequest> serviceRequestsList = serviceRequestRepository.findAllByServiceId(eachBService.getServiceId());
				OffersCount = serviceRequestsList!=null?serviceRequestsList.size()+OffersCount:OffersCount;
			}
		}
		response.setOrdersDone(OffersCount);
		return response;
	}

}
