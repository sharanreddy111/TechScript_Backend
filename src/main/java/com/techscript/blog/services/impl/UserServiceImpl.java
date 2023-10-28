package com.techscript.blog.services.impl;

import com.techscript.blog.entities.User;
import com.techscript.blog.exceptions.ResourceNotFoundException;
import com.techscript.blog.payloads.UserDTO;
import com.techscript.blog.repositories.UserRepository;
import com.techscript.blog.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = this.dtoToEntity(userDTO);
        User savedUser = this.userRepository.save(user);
        return this.entityToDto(savedUser);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, Integer userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow((() -> new ResourceNotFoundException("User","UserId", userId)));

        user.setUserName(userDTO.getUserName());
        user.setUserEmail(userDTO.getUserEmail());
        user.setUserPassword(userDTO.getUserPassword());
        user.setAboutUser(userDTO.getAboutUser());

        User updatedUser = this.userRepository.save(user);

        return this.entityToDto(updatedUser);
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        this.userRepository.delete(user);
    }

    @Override
    public UserDTO getUserById(Integer userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User", "Id", userId));
        return this.entityToDto(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = this.userRepository.findAll();
        return users.stream().map(this::entityToDto).toList();
    }

    public User dtoToEntity(UserDTO userDTO){
        return this.modelMapper.map(userDTO, User.class);
    }

    public UserDTO entityToDto(User user){
//        UserDTO userDTO = new UserDTO();
//        userDTO.setId(user.getId());
//        userDTO.setName(user.getName());
//        userDTO.setPassword(user.getPassword());
//        userDTO.setEmail(user.getEmail());
//        userDTO.setAbout(user.getAbout());
        return this.modelMapper.map(user, UserDTO.class);
    }
}