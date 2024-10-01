package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.User;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.UserRepository;
import com.elyashevich.mmfask.service.UserService;
import com.elyashevich.mmfask.service.converter.UserConverter;
import com.elyashevich.mmfask.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public User findById(String id) {
        return this.userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with id = %s was not found.".formatted(id))
        );
    }

    @Override
    public User create(User user) {
        var candidate = this.userConverter.update(user);
        candidate.setRoles(Set.of(Role.ROLE_USER));
        candidate.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(candidate);
    }
}
