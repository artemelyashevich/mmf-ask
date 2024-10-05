package com.elyashevich.mmfask.api.mapper;

import com.elyashevich.mmfask.api.dto.user.UserDto;
import com.elyashevich.mmfask.entity.User;

/**
 * Mapper interface for mapping between User and UserDto objects.
 */
public interface UserMapper extends Mappable<User, UserDto> {
}