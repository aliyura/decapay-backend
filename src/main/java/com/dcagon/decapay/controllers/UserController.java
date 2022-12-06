package com.dcagon.decapay.controllers;


import com.dcagon.decapay.pojos.ApiResponse;
import com.dcagon.decapay.pojos.user.AuthRequest;
import com.dcagon.decapay.pojos.user.SignupRequest;
import com.dcagon.decapay.services.UserService;
import com.dcagon.decapay.utils.ResponseProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
public class UserController {

    private  final UserService userService;
    private  final ResponseProvider responseProvider;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Object>>  createUser(@RequestBody SignupRequest request){
        return  responseProvider.success(userService.createUser(request));
    }
    @PostMapping("/auth")
    public ResponseEntity<ApiResponse<Object>>  authenticateUser(@RequestBody AuthRequest request){
        return  responseProvider.success(userService.signIn(request));
    }

    @PostMapping("/hello")
    public ResponseEntity<ApiResponse<Object>>  sayHello(){
        return  responseProvider.success("You are welcome");
    }
}
