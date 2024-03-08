package com.retro.dev.payload.response;

import java.util.Date;
import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
	private String name;
	
	//additional details
    private String companyName;
    private Date companyStartDate;
    private String backgroundImage;
    private String profileImage;
    private String dob;
    private String contactNumber;
    private String address;

    public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles, String name) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.name = name;
    }

    public JwtResponse(String token, Long id, String username, String email, List<String> roles,
			String name, String companyName, Date companyStartDate, String backgroundImage, String profileImage,
			String dob, String contactNumber, String address) {
		super();
		this.token = token;
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
		this.name = name;
		this.companyName = companyName;
		this.companyStartDate = companyStartDate;
		this.backgroundImage = backgroundImage;
		this.profileImage = profileImage;
		this.dob = dob;
		this.contactNumber = contactNumber;
		this.address = address;
	}

	public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Date getCompanyStartDate() {
		return companyStartDate;
	}

	public void setCompanyStartDate(Date companyStartDate) {
		this.companyStartDate = companyStartDate;
	}

	public String getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
    
}
