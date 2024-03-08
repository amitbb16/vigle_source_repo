package com.retro.dev.security;

public interface ISecurityUserService {

    String validatePasswordResetToken(String token);

}
