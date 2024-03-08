package com.retro.dev.security.services;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.retro.dev.dtos.UserDto;
import com.retro.dev.models.ERole;
import com.retro.dev.models.Role;
import com.retro.dev.models.User;
import com.retro.dev.payload.response.UserResponse;
import com.retro.dev.repository.UserRepository;
import com.retro.dev.security.jwt.JwtUtils;
import com.retro.dev.util.DevUtils;
import com.retro.dev.util.PublicConstants;


@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userDao;

	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Autowired
	DevUtils devUtils;
    
    @Autowired
    OffersListService offerListService;
    
    @Autowired
    OrdersListService orderListService;

    @Autowired
    ReviewService reviewService;

	
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userDao.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(),
				new ArrayList<>());
	}
	
	public Optional<User> loadUserProfile(String username) throws UsernameNotFoundException{
		Optional<User> userData = userDao.findByUsername(username);
		if(null==userData) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		userData = fetchUserImagesData(userData,true);
		return userData;
	}
	
	private Optional<User> fetchUserImagesData(Optional<User> userData, boolean imagesDataReqd) {
		try {
			if(imagesDataReqd) {
				if(!StringUtils.isEmpty(userData.get().getBackgroundImage())) {
					File bgImgFile = new File(System.getProperty("user.dir")+File.separator+ userData.get().getBackgroundImage());
					byte[] bgImgfileContent = FileUtils.readFileToByteArray(bgImgFile);
					String encodedStringForbgImg = Base64.getEncoder().encodeToString(bgImgfileContent);
					userData.get().setBackgroundImage(encodedStringForbgImg);
				}
				if(!StringUtils.isEmpty(userData.get().getProfileImage())) {
					byte[] profileImgfileContent = FileUtils.readFileToByteArray(new File(System.getProperty("user.dir")+File.separator+ userData.get().getProfileImage()));
					String encodedStringForProfImg = Base64.getEncoder().encodeToString(profileImgfileContent);
					userData.get().setProfileImage(encodedStringForProfImg);
				}
			}
		} catch (IOException e) {
			logger.error("error encoding user Image: "+e.getMessage());
			e.printStackTrace();
		}
		return userData;
	}
	
	public Optional<User> loadUserProfileDataAndImages(String username) throws UsernameNotFoundException{
		Optional<User> userData = userDao.findByUsername(username);
		if(null==userData) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		userData = fetchUserImagesData(userData,false);
		return userData;
	}
	
	
	public User update(String username, UserDto user) throws FileNotFoundException, UsernameNotFoundException {
		Optional<User> existingUser = userDao.findByUsername(username);
		if(null==existingUser) throw new UsernameNotFoundException("UserName: "+username+" not found");
		if(null!=user.getBackgroundImageFile() || null!=user.getProfileImageFile()) {
			user = saveImageFiles(user, existingUser);
		}
		if(null!=user.getBackgroundImageFile()) {
			existingUser.get().setBackgroundImage(user.getBackgroundImage());
		}
		if(null!=user.getProfileImageFile()) {
			existingUser.get().setProfileImage(user.getProfileImage());
		}
	//	existingUser.get().setPassword(bcryptEncoder.encode(user.getPassword()));
		existingUser.get().setContactNumber(user.getContactNumber());
		existingUser.get().setAddress(user.getAddress());
		existingUser.get().setAddressone(user.getAddressone());
		return userDao.save(existingUser.get());
	}

	private UserDto saveImageFiles(UserDto user, Optional<User> existingUser) throws FileNotFoundException {
		String fileUploadDir = devUtils.getFileUploadDir()+existingUser.get().getId()+File.separator;
		String bckgrndImgFileName = "";
		String profileImgFileName = "";
		try {
            if(null!=user.getBackgroundImageFile()) {
            	bckgrndImgFileName = StringUtils.cleanPath(user.getBackgroundImageFile().getOriginalFilename());
            	if(bckgrndImgFileName.contains(".."))
                    throw new FileNotFoundException("Sorry! Filename contains invalid path sequence " + bckgrndImgFileName);
            	bckgrndImgFileName = PublicConstants.BACKGRND_IMG+bckgrndImgFileName.substring(bckgrndImgFileName.lastIndexOf("."));
            	String bckgrndImgFilePath = createResourceSubFolder(fileUploadDir);
            	File newBckgrndImgFile = new File(bckgrndImgFilePath+File.separator+bckgrndImgFileName);
            	logger.debug("file getting created: "+newBckgrndImgFile.getPath());
            	user.getBackgroundImageFile().transferTo(Paths.get(bckgrndImgFilePath+File.separator+bckgrndImgFileName));
            	user.setBackgroundImage(fileUploadDir+File.separator+bckgrndImgFileName);
            }
            if(null!=user.getProfileImageFile()) {
	            profileImgFileName = StringUtils.cleanPath(user.getProfileImageFile().getOriginalFilename());
	            if(profileImgFileName.contains("..")) 
	                throw new FileNotFoundException("Sorry! Filename contains invalid path sequence " + profileImgFileName);
	            bckgrndImgFileName = PublicConstants.PROFILE_IMG+profileImgFileName.substring(profileImgFileName.lastIndexOf("."));
	            String profileImgFilePath = createResourceSubFolder(fileUploadDir);
	            File newBckgrndImgFile = new File(profileImgFilePath+File.separator+profileImgFileName);
	            logger.debug("file getting created: "+newBckgrndImgFile.getPath());
	            user.getProfileImageFile().transferTo(Paths.get(profileImgFilePath+File.separator+profileImgFileName));
	            user.setProfileImage(fileUploadDir+File.separator+profileImgFileName);
            }
            
        } catch (Exception ex) {
        	logger.error("Could not create the directory where the uploaded files will be stored.");
            throw new FileNotFoundException("Could not create the directory where the uploaded files will be stored.");
        }
		return user;
	}
	
	private String createResourceSubFolder(String folderName) throws URISyntaxException, IOException {
		
		/*java.net.URL url = JwtUserDetailsService.class.getResource("/media/blank.jpg");
		//java.net.URL url = JwtUserDetailsService.class.getResource(File.separator+"media"+File.separator+PublicConstants.SAMPLE_IMG);
	    if(null!=url) {
		    File fullPathToSubfolder = new File(url.toURI()).getAbsoluteFile();
		    String projectFolder = fullPathToSubfolder.getAbsolutePath().split("target")[0];
		    File testResultsFolder = new File(projectFolder + "src"+File.separator+"main"+File.separator+"resources" + folderName+File.separator);
		    logger.debug("file parent directory: "+testResultsFolder.getPath());
		    if (!testResultsFolder.exists()) {
		    	Files.createDirectories(Paths.get(testResultsFolder+File.separator));
		    }
		    return testResultsFolder.getPath();
	    }else {*/
	    	String currentDirectory = System.getProperty("user.dir")+File.separator+ folderName+File.separator;
	    	logger.debug("file parent directory: "+currentDirectory+File.separator+ folderName+File.separator);
	    	File testResultsFolder = new File(currentDirectory);
	    	if (!testResultsFolder.exists()) {
		    	Files.createDirectories(Paths.get(testResultsFolder+File.separator));
		    }
	    	return currentDirectory;
	    //}
	}
	
	
	public User save(UserDto user) {
		User newUser = new User();
		newUser.setCompanyName(user.getCompanyName());
		newUser.setUsername(user.getUserName());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setEmail(user.getEmail());
		newUser.setCompanyStartDate(user.getCompanyStartDate());
		newUser.setBackgroundImage(user.getBackgroundImage());
		newUser.setProfileImage(user.getProfileImage());
		newUser.setDob(user.getDob());
		newUser.setContactNumber(user.getContactNumber());
		newUser.setAddress(user.getAddress());


		return userDao.save(newUser);
	}

	
	
	public UserResponse getUserAdditionalDetails(Principal principal, Long userId) {
		UserResponse response = new UserResponse();
    	Optional<User> userData = userDao.findById(userId);
    	if(null==userData) {
    		return generateFailResponse("User not found!");
    	}
    	if(!StringUtils.isEmpty(userData.get().getEmail())) {
    		response = getAdditionalUserDetails(userData.get());
    	}else {
    		response = generateSuccessResponse("User data missing or is incomplete. Please update user info.", response);
    	}
		return response;
	}

	private UserResponse getAdditionalUserDetails(User user) {
		UserResponse response = new UserResponse();
		if(user.getRoles()!=null) {
			for(Role eachUserRole : user.getRoles()) {
				if(!StringUtils.isEmpty(eachUserRole.toString())) {
					if(eachUserRole.getName().toString().contains(ERole.ROLE_BUSINESS.toString())) {
						response = getBusinessOffersCountAndRatings(user, response);
					} else if(eachUserRole.getName().toString().contains(ERole.ROLE_USER.toString())) {
						response = getUserOrdersCountAndRatings(user,response);
					}
				}
			}
		}
		response = generateSuccessResponse("User additional data fetched successfully.", response);
		return response;
	}
	
	private UserResponse getBusinessOffersCountAndRatings(User user, UserResponse response) {
		response = offerListService.getBusinessOffersCount(user, response);
		response = reviewService.getUserAvgRatings(user, response);
		return response;
	}

	private UserResponse getUserOrdersCountAndRatings(User user, UserResponse response) {
		response = orderListService.getUserOrdersCount(user, response);
		response = reviewService.getUserAvgRatings(user, response);
		return response;
	}

	public UserResponse generateFailResponse(String respDesc) {
		UserResponse response = new UserResponse();
		response.setResponseStatus(PublicConstants.FAIL);
		response.setResponseDesc(respDesc);
		return response;
	}

	public UserResponse generateSuccessResponse(String respDesc, UserResponse response) {
		response.setResponseStatus(PublicConstants.SUCCESS);
		response.setResponseDesc(respDesc);
		return response;
	}
	
	
}