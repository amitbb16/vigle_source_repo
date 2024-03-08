package com.retro.dev.dtos;

import com.retro.dev.payload.validation.PasswordMatches;
import com.retro.dev.payload.validation.ValidEmail;
import com.retro.dev.payload.validation.ValidPassword;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@PasswordMatches
public class UserDto {
    
	private Long id;
	
	@NotNull
    @Size(min = 1, message = "{Size.userDto.companyName}")
    private String companyName;

    @NotNull
    @Size(min = 1, message = "{Size.userDto.userName}")
    private String userName;

    private String name;

    @ValidPassword
    private String password;

    @NotNull
    @Size(min = 1)
    private String matchingPassword;

    @ValidEmail
    @NotNull
    @Size(min = 1, message = "{Size.userDto.email}")
    private String email;

    @NotNull
    @Size(min = 1, message = "{Size.userDto.companyStartDate}")
    private Date companyStartDate;

    @NotNull
    private String backgroundImage;

    @NotNull
    private String profileImage;

    @NotNull
    @Size(min = 1)
    private String dob;

    @NotNull
    @Size(min = 1)
    private String contactNumber;

    @NotNull
    @Size(min = 1)
    private String address;

    @NotNull
    @Size(min = 1)
    private String addressone;

    private boolean isUsing2FA;
    
    private MultipartFile backgroundImageFile;

    private MultipartFile profileImageFile;
    

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCompanyStartDate() {
        return companyStartDate;
    }

    public void setCompanyStartDate(Date companyStartDate) {
        this.companyStartDate = companyStartDate;
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

    public String getAddressone() {
        return addressone;
    }

    public void setAddressone(String addressone) {
        this.addressone = addressone;
    }

    public boolean isUsing2FA() {
        return isUsing2FA;
    }

    public void setUsing2FA(boolean using2FA) {
        isUsing2FA = using2FA;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
//        builder.append("UserDto [username=").append(username).append(", lastName=").append(lastName).append(", password=").append(password).append(", matchingPassword=").append(matchingPassword).append(", email=").append(email).append(", isUsing2FA=")
//                .append(isUsing2FA).append(", role=").append(role).append("]");
        builder.append("[email=").append(email).append("]");
        return builder.toString();
    }

	public MultipartFile getBackgroundImageFile() {
		return backgroundImageFile;
	}

	public void setBackgroundImageFile(MultipartFile backgroundImageFile) {
		this.backgroundImageFile = backgroundImageFile;
	}

	public MultipartFile getProfileImageFile() {
		return profileImageFile;
	}

	public void setProfileImageFile(MultipartFile profileImageFile) {
		this.profileImageFile = profileImageFile;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
