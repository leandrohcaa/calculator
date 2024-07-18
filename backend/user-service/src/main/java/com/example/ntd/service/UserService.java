package com.example.ntd.service;

import com.example.ntd.dto.UserResponse;
import com.example.ntd.exception.UserNotFoundException;
import com.example.ntd.mapper.UserMapper;
import com.example.ntd.model.User;
import com.example.ntd.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserMapper userMapper;

  public UserResponse getUserByUsername(String username) {
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isEmpty()){
      throw new UserNotFoundException("User not found. Username: " + username);
    }
    return userMapper.toDto(user.get());
  }

  @Transactional
  public UserResponse updateBalance(UUID id, BigDecimal balance) {
    Optional<User> optionalUser = userRepository.findById(id);
    if (optionalUser.isEmpty()){
      throw new UserNotFoundException("User not found.");
    }
    User user = optionalUser.get();
    user.setBalance(balance);
    return userMapper.toDto(userRepository.save(user));
  }
}