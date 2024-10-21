package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.ActivationCode;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.ActivationCodeRepository;
import com.elyashevich.mmfask.service.ActivationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActivationCodeServiceImpl implements ActivationCodeService {

    private final ActivationCodeRepository activationCodeRepository;

    @Override
    public ActivationCode findByEmail(final String email) {
        return this.activationCodeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Activations code was not found."));
    }

    @Override
    public ActivationCode create(final String code, final String email) {
        var activationCode = ActivationCode.builder()
                .value(code)
                .email(email)
                .build();
        return this.activationCodeRepository.save(activationCode);
    }

    @Transactional
    @Override
    public void update(String email, String resetCode) {
        var code = this.findByEmail(email);
        code.setValue(resetCode);
        this.activationCodeRepository.save(code);
    }
}
