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
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = this.dtoToUser(userDTO);
        User savedUser = this.userRepository.save(user);
        return this.userToDto(savedUser);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, Integer userId) {

        User user = this.userRepository.findById(userId)
                .orElseThrow((() -> new ResourceNotFoundException("User","Id", userId)));

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setAbout(userDTO.getAbout());

        User updatedUser = this.userRepository.save(user);

        return this.userToDto(updatedUser);

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
        return this.userToDto(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = this.userRepository.findAll();
//      List<UserDTO> userDtos = users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());
        return users.stream().map(this::userToDto).collect(Collectors.toList());
    }


    public User dtoToUser(UserDTO userDTO){
        return this.modelMapper.map(userDTO, User.class);
    }

    public UserDTO userToDto(User user){
//        UserDTO userDTO = new UserDTO();
//        userDTO.setId(user.getId());
//        userDTO.setName(user.getName());
//        userDTO.setPassword(user.getPassword());
//        userDTO.setEmail(user.getEmail());
//        userDTO.setAbout(user.getAbout());
        return this.modelMapper.map(user, UserDTO.class);
    }
}
