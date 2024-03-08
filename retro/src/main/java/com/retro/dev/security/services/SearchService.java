package com.retro.dev.security.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retro.dev.dtos.BusinessServicesDTO;
import com.retro.dev.dtos.SearchDTO;
import com.retro.dev.models.BusinessServices;
import com.retro.dev.models.Category;
import com.retro.dev.models.Discount;
import com.retro.dev.models.SubCategory;
import com.retro.dev.payload.request.SearchRequest;
import com.retro.dev.payload.response.BusinessServicesResponse;
import com.retro.dev.payload.response.SearchResponse;
import com.retro.dev.repository.BusinessServicesRepository;
import com.retro.dev.repository.DiscountRepository;
import com.retro.dev.repository.SubCategoryRepository;
import com.retro.dev.repository.UserCategoryRepository;
import com.retro.dev.repository.UserRepository;
import com.retro.dev.util.DevUtils;
import com.retro.dev.util.PublicConstants;



@Service
public class SearchService {
    
	@Autowired
	BusinessServicesRepository businessServiceDao;
    
	@Autowired
    UserRepository userRepository;
    
    @Autowired
    CategoryService categoryService;
    
    @Autowired
    UserCategoryRepository userCategoryDao;
    
    @Autowired
    SubCategoryRepository subCategoryDao;
    
    @Autowired
    DiscountRepository discountDao;
    
    @Autowired
    DevUtils utils;

    
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
    
	
	public BusinessServicesResponse generateFailResponse(String msg) {
		BusinessServicesResponse response = new BusinessServicesResponse();
		BusinessServicesDTO bServiceDetail = new BusinessServicesDTO();
		response.setStatus(PublicConstants.FAIL);
		response.setRespDesc(msg);
		response.setBusinessServices(bServiceDetail);
		return response;
	}
	
	public SearchResponse generateSuccessResponse(String msg, List<SearchDTO> searchResultsList, SearchResponse response) {
		response.setResponseStatus(PublicConstants.SUCCESS);
		response.setResponseDesc(msg);
		response.setSearchResultsList(searchResultsList);
		return response;
	}

	public SearchResponse searchServicesAndOffers(Principal principal, SearchRequest searchRequest) {
		SearchResponse response = new SearchResponse();
		if(null==searchRequest.getSearchFilterTypes()||searchRequest.getSearchFilterTypes().size()<1) {
			response = generateSuccessResponse("Please select a search filter first!", new ArrayList<SearchDTO>(), response);
		}
		for(String eachSearchFilter : searchRequest.getSearchFilterTypes()) {
			response = mergeFilteredData(eachSearchFilter, response, searchRequest);
		}
		response = generateSuccessResponse("Search completed succesfully.", response.getSearchResultsList(), response);
		return response;
	}

	private SearchResponse mergeFilteredData(String eachSearchFilter, SearchResponse response, SearchRequest searchRequest) {
		List<SearchDTO> existingSearchResultsList = null!=response.getSearchResultsList()?response.getSearchResultsList():new ArrayList<SearchDTO>();
		if(null!=existingSearchResultsList && existingSearchResultsList.size()>0) {
			for(SearchDTO eachExistingSearchResultDTO : existingSearchResultsList) {
				response = filterBasedOnType(eachSearchFilter, response, eachExistingSearchResultDTO);
			}
		} else {
			response = filterBasedOnType(eachSearchFilter, response, searchRequest.getSearchDTO());
		}
		return response;
	}
	
	
	private SearchResponse filterBasedOnType(String eachSearchFilter, SearchResponse response, SearchDTO eachSearchDTO) {
		switch (eachSearchFilter.toString().toUpperCase()) {
		case "CATEGORIES":
			response = setCategoriesFilterResponse(response, eachSearchDTO);
			break;
		case "PRICE":
			response = setPriceFilterResponse(response, eachSearchDTO);
			break;
		case "PLACE":
			response = setPlaceFilterResponse(response, eachSearchDTO);
			break;
		case "DISCOUNT":
			response = setDiscountFilterResponse(response, eachSearchDTO);
			break;
		case "TIME":
			response = setTimeFilterResponse(response, eachSearchDTO);
			break;
		default:
			response = setCategoriesFilterResponse(response, eachSearchDTO);
			break;
		}
		return response;
	}

	private SearchResponse setCategoriesFilterResponse(SearchResponse response, SearchDTO eachSearchDTO) {
		if(null!=eachSearchDTO && eachSearchDTO.getCategoryId()!=null) {
			List<SearchDTO> searchResultDTOs = new ArrayList<SearchDTO>();
			if(response.getSearchResultsList()==null || response.getSearchResultsList().size()<1) {
				Category category = new Category();
				category.setCategoryId(eachSearchDTO.getCategoryId());
				List<SubCategory> subcategoriesList = subCategoryDao.findByCategory(category);
				List<Long> subCatIdsList = subcategoriesList.stream().map(item -> item.getSubcategoryId()).collect(Collectors.toList());
				List<BusinessServices> searchResultBusinessServicesDTOs =  businessServiceDao.findAllBycategoryIds(subCatIdsList);
				if(null!=searchResultBusinessServicesDTOs) {
					for(BusinessServices eachBService : searchResultBusinessServicesDTOs) {
						SearchDTO eachSearchresultDTO = new SearchDTO();
						BeanUtils.copyProperties(eachBService, eachSearchresultDTO);
						searchResultDTOs.add(eachSearchresultDTO);
					}
				}
			} else {
				List<SearchDTO> existingSearchResultsList = response.getSearchResultsList();
				for(SearchDTO eachexistingSearchResultDTO : existingSearchResultsList) {
					if(eachexistingSearchResultDTO.getCategoryId().toString().equals(eachSearchDTO.getCategoryId().toString())) {
						searchResultDTOs.add(eachexistingSearchResultDTO);
					}
				}
			}
			response.setSearchResultsList(searchResultDTOs);
		}
		return response;
	}

	private SearchResponse setPriceFilterResponse(SearchResponse response, SearchDTO eachSearchDTO) {
		if(null!=eachSearchDTO && eachSearchDTO.getCategoryId()!=null) {
			List<SearchDTO> searchResultDTOs = new ArrayList<SearchDTO>();
			if(response.getSearchResultsList()==null || response.getSearchResultsList().size()<1) {
				List<BusinessServices> searchResultBusinessServicesDTOs =  businessServiceDao.findAllByPrice(eachSearchDTO.getServicePrice(), eachSearchDTO.getPriceTo());
				if(null!=searchResultBusinessServicesDTOs) {
					for(BusinessServices eachBService : searchResultBusinessServicesDTOs) {
						SearchDTO eachSearchresultDTO = new SearchDTO();
						BeanUtils.copyProperties(eachBService, eachSearchresultDTO);
						searchResultDTOs.add(eachSearchresultDTO);
					}
				}
			} else {
				List<SearchDTO> existingSearchResultsList = response.getSearchResultsList();
				for(SearchDTO eachexistingSearchResultDTO : existingSearchResultsList) {
					if(eachexistingSearchResultDTO.getServicePrice()>=eachSearchDTO.getServicePrice() &&
							eachexistingSearchResultDTO.getPriceTo()<=eachSearchDTO.getPriceTo()) {
						searchResultDTOs.add(eachexistingSearchResultDTO);
					}
				}
			}
			response.setSearchResultsList(searchResultDTOs);
		}
		return response;
	}

	private SearchResponse setPlaceFilterResponse(SearchResponse response, SearchDTO eachSearchDTO) {
		if(null!=eachSearchDTO && eachSearchDTO.getCategoryId()!=null) {
			List<SearchDTO> searchResultDTOs = new ArrayList<SearchDTO>();
			if(response.getSearchResultsList()==null || response.getSearchResultsList().size()<1) {
				List<BusinessServices> searchResultBusinessServicesDTOs =  businessServiceDao.findAllByServiceLoc(eachSearchDTO.getServiceLoc());
				if(null!=searchResultBusinessServicesDTOs) {
					for(BusinessServices eachBService : searchResultBusinessServicesDTOs) {
						SearchDTO eachSearchresultDTO = new SearchDTO();
						BeanUtils.copyProperties(eachBService, eachSearchresultDTO);
						searchResultDTOs.add(eachSearchresultDTO);
					}
				}
			} else {
				List<SearchDTO> existingSearchResultsList = response.getSearchResultsList();
				for(SearchDTO eachexistingSearchResultDTO : existingSearchResultsList) {
					if(eachexistingSearchResultDTO.getServiceLoc().equals(eachSearchDTO.getServiceLoc())) {
						searchResultDTOs.add(eachexistingSearchResultDTO);
					}
				}
			}
			response.setSearchResultsList(searchResultDTOs);
		}
		return response;
	}

	private SearchResponse setDiscountFilterResponse(SearchResponse response, SearchDTO eachSearchDTO) {
		if(null!=eachSearchDTO && eachSearchDTO.getCategoryId()!=null) {
			List<SearchDTO> searchResultDTOs = new ArrayList<SearchDTO>();
			if(response.getSearchResultsList()==null || response.getSearchResultsList().size()<1) {
				List<Discount> discountList = discountDao.findAllByDiscount(eachSearchDTO.getDiscount());
				List<BusinessServices> searchResultBusinessServicesDTOs = null;
				if(null!=discountList) {
					List<Long> serviceIdsListFromDiscountList = discountList.stream().map(item -> item.getServiceId()).collect(Collectors.toList());
					searchResultBusinessServicesDTOs =  businessServiceDao.findAllServicesUsingServiceIds(serviceIdsListFromDiscountList);
				}
				if(null!=searchResultBusinessServicesDTOs) {
					for(BusinessServices eachBService : searchResultBusinessServicesDTOs) {
						SearchDTO eachSearchresultDTO = new SearchDTO();
						BeanUtils.copyProperties(eachBService, eachSearchresultDTO);
						searchResultDTOs.add(eachSearchresultDTO);
					}
				}
			} else {
				List<SearchDTO> existingSearchResultsList = response.getSearchResultsList();
				for(SearchDTO eachexistingSearchResultDTO : existingSearchResultsList) {
					if(((eachexistingSearchResultDTO.getPriceTo()-eachexistingSearchResultDTO.getServicePrice())/100)>=eachSearchDTO.getDiscount()) {
						searchResultDTOs.add(eachexistingSearchResultDTO);
					}
				}
			}
		}
		return response;
	}

	private SearchResponse setTimeFilterResponse(SearchResponse response, SearchDTO eachSearchDTO) {
		if(null!=eachSearchDTO && eachSearchDTO.getCategoryId()!=null) {
			List<SearchDTO> searchResultDTOs = new ArrayList<SearchDTO>();
			if(response.getSearchResultsList()==null || response.getSearchResultsList().size()<1) {
				List<BusinessServices> searchResultBusinessServicesDTOs =  businessServiceDao.findAllByCreatedDate(eachSearchDTO.getCreatedDate());
				if(null!=searchResultBusinessServicesDTOs) {
					for(BusinessServices eachBService : searchResultBusinessServicesDTOs) {
						SearchDTO eachSearchresultDTO = new SearchDTO();
						BeanUtils.copyProperties(eachBService, eachSearchresultDTO);
						searchResultDTOs.add(eachSearchresultDTO);
					}
				}
			} else {
				List<SearchDTO> existingSearchResultsList = response.getSearchResultsList();
				for(SearchDTO eachexistingSearchResultDTO : existingSearchResultsList) {
					if(eachexistingSearchResultDTO.getCreatedDate().after(eachSearchDTO.getCreatedDate())&&
							eachexistingSearchResultDTO.getCreatedDate().before(utils.getCurrentDateAndTime())) {
						searchResultDTOs.add(eachexistingSearchResultDTO);
					}
				}
			}
			response.setSearchResultsList(searchResultDTOs);
		}
		return response;
	}

}
