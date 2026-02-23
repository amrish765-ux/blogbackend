package com.blogapplication.payload;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentDto {

    private Integer id;

    private String content;
}
