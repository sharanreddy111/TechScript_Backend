package com.techscript.blog.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private int userId;

    @NotEmpty
    @Size(min = 4,message = "Username must be min of 4 characters")
    private String userName;

    @Email(message = "Email Address is not valid !")
    private String userEmail;

    @NotEmpty
    @Size(min = 3, max = 10, message = "Password must be between 3 and 10 characters")
    private String userPassword;

    @NotEmpty
    private String aboutUser;
}