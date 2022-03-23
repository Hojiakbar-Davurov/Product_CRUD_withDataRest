package com.example.appauthemailauditing.payload;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegisterDto {

    @NotNull
    @Size(min = 3, max = 25)                              // table max & min length
    private String firstName;

    @NotNull
    @Length(min = 3, max = 25)                            // table max & min length     @length  =  @size
    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;
}
