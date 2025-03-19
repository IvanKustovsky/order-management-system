package com.example.online_shop.mapper;

import com.example.online_shop.dto.RegisterUserDto;
import com.example.online_shop.dto.UpdateUserDto;
import com.example.online_shop.dto.UserDto;
import com.example.online_shop.entity.UserEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(UserEntity user);

    UserEntity toEntity(RegisterUserDto registerUserDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserEntityFromDto(UpdateUserDto updateUserDto, @MappingTarget UserEntity existingUser);
}
