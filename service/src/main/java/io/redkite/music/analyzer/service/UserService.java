package io.redkite.music.analyzer.service;


import com.redkite.plantcare.common.dto.PasswordUpdateDto;
import com.redkite.plantcare.common.dto.UserRequest;
import com.redkite.plantcare.common.dto.UserResponse;

import io.redkite.music.analyzer.controller.filters.UserFilter;
import io.redkite.music.analyzer.model.User;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UserService {

  UserResponse createUser(UserRequest userRequest);

  Page<UserResponse> findUsers(UserFilter filter);

  UserResponse getUserByEmail(String email);


  UserResponse getUser(Long userId);


  User getFullUser(Long userId);


  void editUser(Long userId, UserRequest userRequest);


  void deleteUser(Long userId);

  void changePassword(Long userId, PasswordUpdateDto passwordUpdateDto);

  boolean checkPasswordMatching(String email, String password);

}
