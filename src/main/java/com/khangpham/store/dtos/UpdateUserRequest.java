package com.khangpham.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateUserRequest {
    private String name;
    private String email;
}
