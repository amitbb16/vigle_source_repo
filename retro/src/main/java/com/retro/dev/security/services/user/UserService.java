package com.retro.dev.security.services.user;

import com.retro.dev.models.PasswordResetToken;
import com.retro.dev.models.User;
import com.retro.dev.models.VerificationToken;
import com.retro.dev.repository.PasswordResetTokenRepository;
import com.retro.dev.repository.RoleRepository;
import com.retro.dev.repository.UserRepository;
import com.retro.dev.repository.VerificationTokenRepository;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
    public static String APP_NAME = "SpringRegistration";

    // API

    @Override
    public User getUser(final String verificationToken) {
        final VerificationToken token = tokenRepository.findByToken(verificationToken);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }

    @Override
    public VerificationToken getVerificationToken(final String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public void saveRegisteredUser(final User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(final User user) {
        final VerificationToken verificationToken = tokenRepository.findByUser(user);

        if (verificationToken != null) {
            tokenRepository.delete(verificationToken);
        }

        final PasswordResetToken passwordToken = passwordTokenRepository.findByUser(user);

        if (passwordToken != null) {
            passwordTokenRepository.delete(passwordToken);
        }

        userRepository.delete(user);
    }

    @Override
    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID()
            .toString());
        vToken = tokenRepository.save(vToken);
        return vToken;
    }

    @Override
    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
       try{
          PasswordResetToken pt =  passwordTokenRepository.saveAndFlush(myToken);
           System.out.println(pt.getToken());
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    @Override
    public Optional<User> findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token);
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(final String token) {
        return Optional.ofNullable(passwordTokenRepository.findByToken(token).getUser());
    }

    @Override
    public Optional<User> getUserByID(final long id) {
        return userRepository.findById(id);
    }

    @Override
    public void changeUserPassword(final Optional<User> user, final String password) {
        user.get().setPassword(passwordEncoder.encode(password));
        userRepository.save(user.get());
    }

    @Override
    public boolean checkIfValidOldPassword(final Optional<User> user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.get().getPassword());
    }

    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate()
            .getTime() - cal.getTime()
            .getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        tokenRepository.delete(verificationToken);
        userRepository.save(user);
        return TOKEN_VALID;
    }

  /*  public String generateQRUrl(User user) throws UnsupportedEncodingException {
        return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME, user.getEmail(), user.getSecret(), APP_NAME), "UTF-8");
    }
*/

    private boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }
    
    
    public String fetchLoginUserProfileImage(String profileImagePath) {
    	String encodedStringForProfImg = "";
    	try {
			if(!StringUtils.isEmpty(profileImagePath)) {
				byte[] profileImgfileContent = FileUtils.readFileToByteArray(new File(profileImagePath));
				encodedStringForProfImg = Base64.getEncoder().encodeToString(profileImgfileContent);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return encodedStringForProfImg;
    }

}
