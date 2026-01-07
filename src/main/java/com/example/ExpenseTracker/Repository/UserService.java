package com.example.ExpenseTracker.Repository;

import com.example.ExpenseTracker.Entities.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


public interface UserService {
    User processOAuth2User(OAuth2User oAuth2User,String registrationId);
}
