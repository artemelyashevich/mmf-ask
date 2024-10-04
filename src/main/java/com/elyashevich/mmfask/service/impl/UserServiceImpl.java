package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.Role;
import com.elyashevich.mmfask.entity.User;
import com.elyashevich.mmfask.exception.ResourceAlreadyExistsException;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.UserRepository;
import com.elyashevich.mmfask.service.UserService;
import com.elyashevich.mmfask.service.converter.impl.UserConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public User findById(final String id) {
        return this.userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with id = %s was not found.".formatted(id))
        );
    }

    @Transactional
    @Override
    public User create(final User user) {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new ResourceAlreadyExistsException("User with email = %s already exists".formatted(user.getEmail()));
        }
        var candidate = this.userConverter.update(new User(), user);
        candidate.setRoles(Set.of(Role.ROLE_USER));
        candidate.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(candidate);
    }

    @Override
    public User findByEmail(final String email) {
        return this.userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User with email: %s was not found.".formatted(email))
        );
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        var user = this.findByEmail(email);
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .toList()
        );
    }
}
