package io.redkite.music.analyzer.controller;


import static io.redkite.music.analyzer.common.Constants.Web.API_V1;

import com.redkite.plantcare.common.dto.ErrorDto;
import com.redkite.plantcare.common.dto.FieldErrorDto;
import com.redkite.plantcare.common.dto.PasswordUpdateDto;
import com.redkite.plantcare.common.dto.UserRequest;
import com.redkite.plantcare.common.dto.UserResponse;


import io.redkite.music.analyzer.MusicAnalyzerException;
import io.redkite.music.analyzer.controller.filters.UserFilter;
import io.redkite.music.analyzer.security.UserContext;
import io.redkite.music.analyzer.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(API_V1 + "/users")
public class UserController {

  @Autowired
  private UserService userService;

//  @Autowired
//  private TokenInvalidationService tokenInvalidationService;


  /**
   * Creates new user based on user request.
   *
   * @param userRequest the dto that contains user info
   * @return the user response dto
   */
  @RequestMapping(method = RequestMethod.POST)
  public UserResponse createUser(@Validated @RequestBody UserRequest userRequest,
                                 BindingResult result) {
    if (result.hasErrors()) {
      throw new MusicAnalyzerException(getErrors("Validation failed during user creation", result), HttpStatus.BAD_REQUEST);
    }
    return userService.createUser(userRequest);
  }

  private ErrorDto getErrors(String errorMessage, BindingResult result) {
    List<FieldErrorDto> fieldErrors = result.getFieldErrors().stream()
            .map(er -> new FieldErrorDto(er.getField(), er.getDefaultMessage()))
            .collect(Collectors.toList());
    return new ErrorDto(errorMessage, fieldErrors);
  }

  @RequestMapping(method = RequestMethod.GET)
  public Page<UserResponse> getUsers(@ModelAttribute UserFilter filter) {
    return userService.findUsers(filter);
  }

  @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
  public UserResponse getUser(@PathVariable("userId") Long userId) {
    return userService.getUser(userId);
  }

  @RequestMapping(value = "/current", method = RequestMethod.GET)
  public UserResponse getCurrentUser() {
    return userService.getUserCurrent();
  }


  @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
  public void editUser(@PathVariable("userId") Long userId,
                       @RequestBody @Validated({UserRequest.UserUpdate.class}) UserRequest userRequest,
                       BindingResult result) {

    if (result.hasErrors()) {
      throw new MusicAnalyzerException(getErrors("Validation failed during user creation", result), HttpStatus.BAD_REQUEST);
    }

    if (!checkUpdateRequest(userRequest)) {
      throw new MusicAnalyzerException("Validation failed during updating user - empty update request", HttpStatus.BAD_REQUEST);
    }
    userService.editUser(userId, userRequest);
  }

  @RequestMapping(value = "/{userId}/password", method = RequestMethod.PUT)
  public void changePassword(@PathVariable("userId") Long userId,
                             @RequestBody @Validated PasswordUpdateDto updateDto,
                             BindingResult result) {
    if (result.hasErrors()) {
      throw new MusicAnalyzerException(getErrors("Validation failed during user creation", result), HttpStatus.BAD_REQUEST);
    }
    userService.changePassword(userId, updateDto);
//    UserContext currentUser = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    tokenInvalidationService.invalidateAllTokensForUser(currentUser.getUserId());
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
  public void deleteUser(@PathVariable("userId") Long userId) {
    userService.deleteUser(userId);
  }

  private boolean checkUpdateRequest(UserRequest userRequest) {
    return StringUtils.isNotBlank(userRequest.getFirstName())
            || StringUtils.isNotBlank(userRequest.getLastName());

  }
}
