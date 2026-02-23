package com.blogapplication.payload;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class JwtResponse {
    private int userId;
    private String username;
    private String token;
}
