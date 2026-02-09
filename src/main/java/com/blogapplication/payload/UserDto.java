package com.blogapplication.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {
    private int id;

    @NotEmpty
    @Size(min = 4,message = "Username must be min of 4 char long")
    private String name;

    @NotEmpty
    @Email(message = "Email address is not valid")
    private String email;

    @NotEmpty
    @Size(min = 4,max = 10,message = "invalid password!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    private int age;

    @NotEmpty
    private String gender;
}
