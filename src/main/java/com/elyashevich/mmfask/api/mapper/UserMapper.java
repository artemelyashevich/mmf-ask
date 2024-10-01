package com.elyashevich.mmfask.api.mapper;

import com.elyashevich.mmfask.api.dto.UserDto;
import com.elyashevich.mmfask.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {
}
