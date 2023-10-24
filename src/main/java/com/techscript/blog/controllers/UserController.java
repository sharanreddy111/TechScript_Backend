package com.techscript.blog.controllers;

import com.techscript.blog.payloads.ApiResponse;
import com.techscript.blog.payloads.UserDTO;
import com.techscript.blog.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/")
     public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO){
         UserDTO createdUserDTO = this.userService.createUser(userDTO);
         return new ResponseEntity<>(createdUserDTO, HttpStatus.CREATED);
     }

     @PutMapping("/{userId}")
     public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO, @PathVariable Integer userId){
         UserDTO updatedUser = this.userService.updateUser(userDTO, userId);
         return ResponseEntity.ok(updatedUser);
     }

     @DeleteMapping("/{userId}")
     public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId){
        this.userService.deleteUser(userId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("User Deleted Successfully",true), HttpStatus.OK);

     }

     @GetMapping("/")
     public ResponseEntity<List<UserDTO>> getAllUsers(){
         List<UserDTO> users = this.userService.getAllUsers();
         return ResponseEntity.ok(users);
     }

     @GetMapping("/{userId}")
     public ResponseEntity<UserDTO> getUserById(@PathVariable Integer userId){
         UserDTO user = this.userService.getUserById(userId);
         return ResponseEntity.ok(user);
     }

}
