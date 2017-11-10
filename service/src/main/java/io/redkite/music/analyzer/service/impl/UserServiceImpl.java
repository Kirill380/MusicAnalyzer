package io.redkite.music.analyzer.service.impl;


import com.redkite.plantcare.common.dto.PasswordUpdateDto;
import com.redkite.plantcare.common.dto.UserRequest;
import com.redkite.plantcare.common.dto.UserResponse;


import io.redkite.music.analyzer.MusicAnalyzerException;
import io.redkite.music.analyzer.controller.filters.UserFilter;
import io.redkite.music.analyzer.convertors.UserConverter;
import io.redkite.music.analyzer.model.Role;
import io.redkite.music.analyzer.model.User;
import io.redkite.music.analyzer.repository.RoleRepository;
import io.redkite.music.analyzer.repository.UserRepository;
import io.redkite.music.analyzer.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

@Service
public class UserServiceImpl implements UserService {

  private static final String REGULAR_USER_ROLE = "regularUser";
  private static final String ADMIN_ROLE = "admin";

  // @Autowired
  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleDao;

  @Autowired
  private UserConverter userConverter;


  @Autowired
  @Qualifier("transactionManager")
  private PlatformTransactionManager txManager;

  @Value("${spring.jpa.hibernate.ddl-auto}")
  private String createDefaults;



  //TODO move defaults creation to separate SQL script

  /**
   * Create default admin in database add two roles -- regularUser and admin.
   */
  @PostConstruct
  public void init() {
    if (!createDefaults.equals("create-drop")) {
      return;
    }

    TransactionTemplate tmpl = new TransactionTemplate(txManager);
    tmpl.execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
        Role regularUser = new Role();
        regularUser.setName(REGULAR_USER_ROLE);
        roleDao.save(regularUser);

        Role adminUser = new Role();
        adminUser.setName(ADMIN_ROLE);
        roleDao.save(adminUser);

        UserRequest user = new UserRequest();
        user.setEmail("admin@gmail.com");
        user.setPassword("admin123");
        createUser(user, ADMIN_ROLE);
      }
    });
  }


  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public UserResponse createUser(UserRequest userRequest) {
    UserResponse userResponse = userConverter.toDto(createUser(userRequest, REGULAR_USER_ROLE));
    userResponse.setEmail(null);
    userResponse.setFirstName(null);
    userResponse.setLastName(null);
    userResponse.setRoles(null);
    return userResponse;
  }

  private User createUser(UserRequest userRequest, String roleName) {
    if (userRepository.existsByEmail(userRequest.getEmail())) {
      throw new MusicAnalyzerException("User with email [" + userRequest.getEmail() + "] already exists", HttpStatus.CONFLICT);
    }
    User user = userConverter.toModel(userRequest);
    user.setCreationDate(LocalDateTime.now());
    user.setRole(roleDao.findByName(roleName));
    return userRepository.save(user);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserResponse> findUsers(UserFilter filter) {
    Page<User> users = userRepository.findUserByFilter(filter.getEmail(), filter);
    return users.map(user -> userConverter.toDto(user));
  }

  @Override
  public UserResponse getUserByEmail(String email) {
    checkExistence(email);
    return userConverter.toDto(userRepository.findByEmail(email));
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse getUser(Long userId) {
    checkExistence(userId);
    return userConverter.toDto(userRepository.getOne(userId));
  }

  @Override
  @Transactional(readOnly = true)
  public User getFullUser(Long userId) {
    checkExistence(userId);
    return userRepository.getOne(userId);
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void editUser(Long userId, UserRequest userRequest) {
    checkExistence(userId);
    User user = userRepository.getOne(userId);
    user.merge(userRequest);
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void deleteUser(Long userId) {
    checkExistence(userId);
    User user = userRepository.getOne(userId);
    //TODO add removing of music profiles
    userRepository.delete(userId);
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void changePassword(Long userId, PasswordUpdateDto passwordUpdateDto) {
    checkExistence(userId);
    User user = userRepository.getOne(userId);
    if (!passwordEncoder.matches(passwordUpdateDto.getOldPassword(), user.getPasswordHash())) {
      throw new MusicAnalyzerException("Password mismatch", HttpStatus.FORBIDDEN);
    }
    user.setPasswordHash(passwordEncoder.encode(passwordUpdateDto.getNewPassword()));
  }

  @Override
  public boolean checkPasswordMatching(String email, String password) {
    User user = userRepository.findByEmail(email);
    return passwordEncoder.matches(password, user.getPasswordHash());
  }

  private void checkExistence(Long userId) {
    if (!userRepository.exists(userId)) {
      throw new MusicAnalyzerException("User with id [" + userId + "] does not exist", HttpStatus.NOT_FOUND);
    }
  }

  private void checkExistence(String email) {
    if (!userRepository.existsByEmail(email)) {
      throw new MusicAnalyzerException("User with email [" + email + "] does not exist", HttpStatus.NOT_FOUND);
    }
  }

}
