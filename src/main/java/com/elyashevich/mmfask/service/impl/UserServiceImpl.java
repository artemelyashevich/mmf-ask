package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.Role;
import com.elyashevich.mmfask.entity.User;
import com.elyashevich.mmfask.exception.InvalidPasswordException;
import com.elyashevich.mmfask.exception.InvalidTokenException;
import com.elyashevich.mmfask.exception.ResourceAlreadyExistsException;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.UserRepository;
import com.elyashevich.mmfask.service.AttachmentService;
import com.elyashevich.mmfask.service.UserService;
import com.elyashevich.mmfask.service.converter.impl.UserConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final AttachmentService attachmentService;

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

    @Override
    public User findByEmail(final String email) {
        return this.userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User with email: %s was not found.".formatted(email))
        );
    }

    @Override
    public User activate(final String email) {
        var user = this.findByEmail(email);
        var roles = user.getRoles();
        roles.add(Role.ROLE_USER);
        user.setRoles(roles);
        return this.userRepository.save(user);
    }

    @Transactional
    @Override
    public User create(final User user) {
        log.debug("Attempting to create a new user with email '{}'.", user.getEmail());

        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new ResourceAlreadyExistsException("User with email = %s already exists".formatted(user.getEmail()));
        }
        var candidate = this.userConverter.update(new User(), user);
        candidate.setImage(null);
        candidate.setRoles(Set.of(Role.ROLE_GUEST));
        candidate.setPassword(this.passwordEncoder.encode(user.getPassword()));
        candidate.setActivationCode(user.getActivationCode());
        var newUser = this.userRepository.save(candidate);

        log.info("User with email '{}' has been created.", user.getEmail());
        return newUser;
    }

    @Transactional
    @Override
    public User uploadImage(final String id, final MultipartFile file) throws Exception {
        log.debug("Attempting to upload image to user with ID '{}'.", id);

        var user = this.findById(id);
        var image = this.attachmentService.create(file);
        user.setImage(image);
        var updatedUser = this.userRepository.save(user);

        log.info("User with ID '{}' has been updated.", id);
        return updatedUser;
    }

    @Override
    public User resetPassword(
            final String email,
            final String code,
            final String oldPassword,
            final String newPassword
    ) {
        var user = this.findByEmail(email);
        if (!user.getActivationCode().equals(code)) {
            throw new InvalidTokenException("Invalid reset code.");
        }
        if (!this.passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidPasswordException("Password mismatch.");
        }
        user.setPassword(this.passwordEncoder.encode(newPassword));
        return this.userRepository.save(user);
    }

    @Transactional
    @Override
    public void setActivationCode(final String email, final String resetCode) {
        var user = this.findByEmail(email);
        user.setActivationCode(resetCode);
        this.userRepository.save(user);
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
