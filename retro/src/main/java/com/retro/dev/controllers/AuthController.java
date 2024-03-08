package com.retro.dev.controllers;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.retro.dev.payload.request.PasswordDto;
import com.retro.dev.security.ISecurityUserService;
import com.retro.dev.security.services.user.IUserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.retro.dev.models.ERole;
import com.retro.dev.models.Role;
import com.retro.dev.models.User;
import com.retro.dev.payload.request.LoginRequest;
import com.retro.dev.payload.request.SignupRequest;
import com.retro.dev.payload.response.JwtResponse;
import com.retro.dev.payload.response.MessageResponse;
import com.retro.dev.repository.RoleRepository;
import com.retro.dev.repository.UserRepository;
import com.retro.dev.security.jwt.JwtUtils;
import com.retro.dev.security.services.UserDetailsImpl;
import org.springframework.web.servlet.view.RedirectView;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ISecurityUserService securityUserService;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Qualifier("messageSource")
	@Autowired
	private MessageSource messages;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Environment env;


	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
								.collect(Collectors.toList());
		ResponseEntity<?> validatedUSerRoleObj = validateUserRole(loginRequest, userDetails);
		if(null!= validatedUSerRoleObj) return validatedUSerRoleObj;
		/*return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), 
												 userDetails.getEmail(), roles, userDetails.getName()));*/
		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), 
				 userDetails.getEmail(), roles, userDetails.getName(), userDetails.getCompanyName(), 
				 userDetails.getCompanyStartDate(), userDetails.getBackgroundImage(), 
				 userService.fetchLoginUserProfileImage(userDetails.getProfileImage()),
				 userDetails.getDob(), userDetails.getContactNumber(), userDetails.getAddress()));
		
	}

	private ResponseEntity<?> validateUserRole(@Valid LoginRequest loginRequest, UserDetailsImpl userDetails) {
		boolean userRoleValid = false;
		if(StringUtils.isEmpty(loginRequest.getUserRole())) {
			loginRequest.setUserRole("user");
		}
		if(null!=userDetails && !StringUtils.isEmpty(loginRequest.getUserRole())) {
			String userRoleId = getIDfromMap(loginRequest);
			for(Role eachRole : userDetails.getUserRoles()) {
				if(!StringUtils.isEmpty(eachRole) &&
						eachRole.getId().toString().equals(userRoleId)) {
					userRoleValid = true;
				}
			}
		}
		if(!userRoleValid) return ResponseEntity.badRequest().body(new MessageResponse("Error: User Role validation failed"));
		return null;
	}

	private String getIDfromMap(@Valid LoginRequest loginRequest) {
		Role userRole = new Role();
		switch (loginRequest.getUserRole().toString()) {
		case "admin":
			userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			break;
		case "business":
			userRole = roleRepository.findByName(ERole.ROLE_BUSINESS)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			break;
		default:
			userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		}
		return userRole.getId().toString();
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), 
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()),
				signUpRequest.getCompanyName(),
				signUpRequest.getCompanyStartDate(),
				signUpRequest.getBackgroundImage(),
				signUpRequest.getProfileImage(),
				signUpRequest.getDob(),
				signUpRequest.getContactNumber(),
				signUpRequest.getAddress(),
				signUpRequest.getAddressone(),
				signUpRequest.getName()
		);

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "business":
					if(StringUtils.isEmpty(signUpRequest.getAddress())&&StringUtils.isEmpty(signUpRequest.getContactNumber())) 
						throw new RuntimeException("Address and Contact Number are mandatory");
					Role modRole = roleRepository.findByName(ERole.ROLE_BUSINESS)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/user/resetPassword")
	@ResponseBody
	public ResponseEntity<?> resetPassword(final Locale locale,final HttpServletRequest request, @RequestParam("email") final String userEmail) {
		final Optional<User> user = userService.findUserByEmail(userEmail);
		if (user != null) {
			final String token = RandomStringUtils.randomNumeric(6);//UUID.randomUUID().toString();
			userService.createPasswordResetTokenForUser(user.get(), token);
			mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user.get()));
		}
		//return ResponseEntity.ok(new MessageResponse(messages.getMessage("message.resetPasswordEmail",null, locale)));
		return ResponseEntity.ok(new MessageResponse("Reset Password sent"));
	}

	private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final User user) {
		final String url = contextPath + "/user/changePassword?token=riIM" + token;
		final String message = "OTP";//messages.getMessage("message.resetPassword", null, locale);
		return constructEmail("Reset Password", message + " \r\n" + token, user);
	}

	@GetMapping("/user/changePassword")
	public RedirectView showChangePasswordPage(
			final Locale locale,
			final Model model,
			@RequestParam("token") final String token
	) {
		final String result = securityUserService.validatePasswordResetToken(token);

		if (result != null) {
			String message = messages.getMessage("auth.message." + result, null, locale);
			return new RedirectView("/login.html?lang=" + locale.getLanguage() + "&message=" + message);
		} else {
			model.addAttribute("token", token);
			return new RedirectView("updatePassword.html?lang=" + locale.getLanguage());
		}
	}


	@PostMapping("/user/savePassword")
	@ResponseBody
	public ResponseEntity<?> savePassword(final Locale locale, @RequestBody @Valid PasswordDto passwordDto) {

		final String result = securityUserService.validatePasswordResetToken(passwordDto.getToken());

		if(result != null) {
			return ResponseEntity.ok(new MessageResponse("Your token is:" + result));
		}

		Optional<User> user = userService.getUserByPasswordResetToken(passwordDto.getToken());
		if(user.isPresent()) {
			userService.changeUserPassword(Optional.of(user.get()), passwordDto.getNewPassword());
			return ResponseEntity.ok(new MessageResponse("reset Password Suc"));
		} else {
			return ResponseEntity.badRequest().body(new MessageResponse("reset token invalid"));
		}
	}

	//change user password
	@PostMapping("/user/updatePassword")
	@ResponseBody
	public ResponseEntity<?> changeUserPassword(final Locale locale, @Valid PasswordDto passwordDto) {
		final Optional<User> user = userService.findUserByEmail(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
		if (!userService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
			return ResponseEntity.badRequest().body("");
		}
		userService.changeUserPassword(user, passwordDto.getNewPassword());
		return ResponseEntity.ok(messages.getMessage("message.updatePasswordSuc", null, locale));
	}
	private SimpleMailMessage constructEmail(String subject, String body, User user) {
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject);
		email.setText(body);
		email.setTo(user.getEmail());
		email.setFrom(env.getProperty("support.email"));
		return email;
	}

	private String getAppUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
}
