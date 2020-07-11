package com.example.store.user;

import com.example.store.user.data.User;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;

@ConditionalOnProperty(prefix = "app.user", name = "enabled", havingValue = "true", matchIfMissing = true)
public interface UserRepository extends JpaRepository<User, Long> {

}
