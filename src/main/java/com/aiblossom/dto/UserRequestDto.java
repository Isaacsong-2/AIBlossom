package com.aiblossom.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserRequestDto {
    @NotNull
    @Size(min = 4, max = 10)
    @Pattern(regexp = "^[a-z0-9]+$")
    private String username;

    @NotNull
    @Size(min = 8, max = 15)
    @Pattern(regexp = "^[a-zA-z0-9]+$")
    private String password;

    private boolean admin = true;
    private String adminToken = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
//    private boolean admin = false;
//    private String adminToken = "";
}
