package com.controller;

import com.dto.request.ApiResponse;
import com.dto.request.AuthenticationRequest;
import com.dto.request.IntrospectRequest;
import com.dto.response.AuthenticationResponse;
import com.dto.response.IntrospectResponse;
import com.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @PostMapping("login")
    public ApiResponse<?> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(response)
                .build();
    }

    @PostMapping("verify-token")
    public ApiResponse<?> authenticate(@RequestBody IntrospectRequest request) {
        IntrospectResponse response = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(response)
                .build();
    }

}