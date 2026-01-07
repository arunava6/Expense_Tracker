package com.example.ExpenseTracker.Service;

import com.example.ExpenseTracker.Entities.Type.ProviderType;
import com.example.ExpenseTracker.Entities.User;
import com.example.ExpenseTracker.Exception.UserAlreadyExistsException;
import com.example.ExpenseTracker.Repository.UserRepo;
import com.example.ExpenseTracker.Repository.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Oauth2ServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Override
    public User processOAuth2User(OAuth2User oAuth2User, String registrationId) {
        ProviderType providerType = ProviderType.valueOf(registrationId.toUpperCase());
        String providerId = extractProviderId(providerType, oAuth2User);
        String userEmail = extractUserEmail(providerType, oAuth2User, providerId);
        return userRepo.findByEmail(userEmail)
                .map(existingUser -> {

                    if (existingUser.getProviderType() == ProviderType.LOCAL) {
                        throw new UserAlreadyExistsException();
                    }

                    return existingUser;
                })
                .orElseGet(() -> {

                    User user = new User();
                    user.setEmail(userEmail);
                    user.setProviderType(ProviderType.GOOGLE);
                    user.setProviderId(providerId);
                    user.setPassword(null);
                    return userRepo.save(user);
                });

    }

    private String extractProviderId(ProviderType providerType, OAuth2User oAuth2User) {
        return switch (providerType) {
            case GOOGLE -> oAuth2User.getAttribute("sub");
            case GITHUB, FACEBOOK -> String.valueOf(oAuth2User.getAttribute("id"));
            default -> throw new IllegalArgumentException("Unsupported provider");
        };
    }

    private String extractUserEmail(ProviderType providerType, OAuth2User oAuth2User, String providerId) {
        return switch (providerType) {
            case GOOGLE -> oAuth2User.getAttribute("email");
            case GITHUB -> oAuth2User.getAttribute("login"); // GitHub has no guaranteed email
            case FACEBOOK -> "facebook_" + providerId;
            default -> throw new IllegalArgumentException("Unsupported provider");
        };
    }
}
