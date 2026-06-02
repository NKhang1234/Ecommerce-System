package com.khangpham.store.mappers;

import com.khangpham.store.dtos.RegisterUserRequest;
import com.khangpham.store.dtos.UpdateUserRequest;
import com.khangpham.store.dtos.UserDto;
import com.khangpham.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    UserDto toDto(User user);

    User toEntity(RegisterUserRequest request);

    void update(UpdateUserRequest request, @MappingTarget User user);
}
