package com.techscript.blog.services;


import com.techscript.blog.entities.User;
import com.techscript.blog.payloads.UserDTO;

import java.util.List;

public interface UserService {

   UserDTO createUser(UserDTO userDTO);
   UserDTO updateUser(UserDTO userDTO, Integer userId);
   UserDTO getUserById(Integer userId);
   List<UserDTO> getAllUsers();
   void deleteUser(Integer userId);

}
