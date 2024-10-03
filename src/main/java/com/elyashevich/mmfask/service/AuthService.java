package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.api.dto.AuthRequestDto;

public interface AuthService {

    String register(final AuthRequestDto authRequestDto);

    String login(final AuthRequestDto authRequestDto);
}
