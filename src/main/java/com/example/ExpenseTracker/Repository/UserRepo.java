package com.example.ExpenseTracker.Repository;

import com.example.ExpenseTracker.Entities.Type.ProviderType;
import com.example.ExpenseTracker.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByProviderIdAndProviderType(String providerId, ProviderType providerType);
}
