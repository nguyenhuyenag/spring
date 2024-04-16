package com.controller;

import com.dto.request.ApiResponse;
import com.dto.request.UserCreationRequest;
import com.dto.response.UserResponse;
import com.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@RestController
@RequestMapping("/users")
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    @PostMapping // ("create")
    public ApiResponse<?> createUser(@RequestBody @Valid UserCreationRequest request) {
        UserResponse user = userService.createUser(request);
        return ApiResponse.<UserResponse>builder()
                .result(user)
                .build();
    }

    public void showAuthenticationInfo() {
        // Lấy thông tin của user đang được authentication trong request hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOG.info("The authenticates using the Token created by the user:");
        LOG.info("Username: {}", authentication.getName());
        LOG.info("Roles: {}", Arrays.toString(authentication.getAuthorities().toArray()));
    }

    @GetMapping("/{userId}")
    public ApiResponse<?> getUser(@PathVariable("userId") String userId) {
        showAuthenticationInfo();
        UserResponse user = userService.getUserById(userId);
        return ApiResponse.<UserResponse>builder()
                .result(user)
                .build();
    }

    @GetMapping
    public ApiResponse<?> getUsers() {
        List<UserResponse> users = userService.getUsers();
        return ApiResponse.builder()
                .result(users)
                .build();
    }

}
