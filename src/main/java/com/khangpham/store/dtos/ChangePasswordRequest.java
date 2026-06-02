package com.khangpham.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
