package com.retro.dev.security.services.user;


import com.retro.dev.models.PasswordResetToken;
import com.retro.dev.models.User;
import com.retro.dev.models.VerificationToken;

import java.util.Optional;

public interface IUserService {


    User getUser(String verificationToken);

    void saveRegisteredUser(User user);

    void deleteUser(User user);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    VerificationToken generateNewVerificationToken(String token);

    void createPasswordResetTokenForUser(User user, String token);

    Optional<User> findUserByEmail(String email);

    PasswordResetToken getPasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    Optional<User> getUserByID(long id);

    void changeUserPassword(Optional<User> user, String password);

    boolean checkIfValidOldPassword(Optional<User> user, String password);
    
    public String fetchLoginUserProfileImage(String profileImagePath);

}
