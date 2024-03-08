package com.retro.dev.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "users", 
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email") 
		})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;

	private String companyName;

	private Date companyStartDate;

	private String backgroundImage;

	private String profileImage;

	private String dob;


	@Size(max = 10)
	private String contactNumber;


	@Size(max = 200)
	private String address;

	@Size(max = 200)
	private String addressone;
	
	@NotBlank
	@Size(max = 50)
	private String name;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public User() {
	}

	public User(String username, String email, String password, String companyName,
				Date companyStartDate, String backgroundImage, String profileImage, 
				String dob, String contactNumber, String address, String addressone, 
				String name, Set<Role> roles) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.companyName = companyName;
		this.companyStartDate = companyStartDate;
		this.backgroundImage=backgroundImage;
		this.profileImage=profileImage;
		this.dob=dob;
		this.contactNumber=contactNumber;
		this.address = address;
		this.addressone = addressone;
		this.name = name;
		this.roles = roles;
	}
	
	public User(String username, String email, String password, String companyName,
			Date companyStartDate, String backgroundImage,
			String profileImage,
			String dob,
			String contactNumber,
			String address,
			String addressone,
			String name) {
	this.username = username;
	this.email = email;
	this.password = password;
	this.companyName = companyName;
	this.companyStartDate = companyStartDate;
	this.backgroundImage=backgroundImage;
	this.profileImage=profileImage;
	this.dob=dob;
	this.contactNumber=contactNumber;
	this.address = address;
	this.addressone = addressone;
	this.name = name;
}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
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

	public String getAddressone() {
		return addressone;
	}

	public void setAddressone(String addressone) {
		this.addressone = addressone;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
