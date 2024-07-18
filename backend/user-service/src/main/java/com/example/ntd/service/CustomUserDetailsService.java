package com.example.ntd.service;

import com.example.ntd.exception.UserInactiveException;
import com.example.ntd.exception.UserNotFoundException;
import com.example.ntd.model.User;
import com.example.ntd.model.UserStatusEnum;
import com.example.ntd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
    Optional<User> user = userRepository.findByUsername(username);
    if (!user.isPresent()) {
      throw new UserNotFoundException("User not found with username: " + username);
    }
    if (UserStatusEnum.INACTIVE.equals(user.get().getStatus())) {
      throw new UserInactiveException("User is inactive. User: " + username);
    }
    return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(), new ArrayList<>());
  }
}