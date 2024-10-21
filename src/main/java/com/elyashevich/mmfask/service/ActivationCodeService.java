package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.entity.ActivationCode;

public interface ActivationCodeService {

    ActivationCode findByEmail(final String email);

    ActivationCode create(final String code, final String email);

    void update(final String email, final String resetCode);
}
