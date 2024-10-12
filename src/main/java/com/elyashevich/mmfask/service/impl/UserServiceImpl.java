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
        log.debug("Attempting to activate user with email '{}'.", email);

        var user = this.findByEmail(email);
        var roles = user.getRoles();
        roles.add(Role.ROLE_USER);
        user.setRoles(roles);
        var savedUser = this.userRepository.save(user);

        log.info("User with email '{}' has been activated.", email);
        return savedUser;
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

    @Override
    public User createAdmin(final User user) {
        log.debug("Attempting to create a new admin user with email '{}'.", user.getEmail());

        var newUser = User.builder()
                .email(user.getEmail())
                .password(this.passwordEncoder.encode(user.getPassword()))
                .roles(Set.of(Role.ROLE_GUEST, Role.ROLE_USER, Role.ROLE_MODERATOR, Role.ROLE_ADMIN))
                .image(null)
                .build();

        var savedUser = this.userRepository.save(newUser);

        log.info("Admin with email '{}' has been created.", user.getEmail());
        return savedUser;
    }

    @Override
    public User createModerator(final User user) {
        log.debug("Attempting to create a new moderator user with email '{}'.", user.getEmail());

        var roles = user.getRoles();
        roles.add(Role.ROLE_MODERATOR);
        user.setRoles(roles);
        var savedUser = this.userRepository.save(user);

        log.info("Moderator with email '{}' has been created.", user.getEmail());
        return savedUser;
    }

    @Transactional
    @Override
    public User uploadImage(final String email, final MultipartFile file) throws Exception {
        log.debug("Attempting to upload image to user with ID '{}'.", email);

        var user = this.findByEmail(email);
        var image = this.attachmentService.create(file);
        user.setImage(image);
        var updatedUser = this.userRepository.save(user);

        log.info("User with ID '{}' has been updated.", email);
        return updatedUser;
    }

    @Override
    public User resetPassword(
            final String email,
            final String code,
            final String oldPassword,
            final String newPassword
    ) {
        log.debug("Attempting to reset password of user with email '{}'.", email);

        var user = this.findByEmail(email);
        if (!user.getActivationCode().equals(code)) {
            throw new InvalidTokenException("Invalid reset code.");
        }
        if (!this.passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidPasswordException("Password mismatch.");
        }
        user.setPassword(this.passwordEncoder.encode(newPassword));
        var savedUser = this.userRepository.save(user);

        log.info("Password of user with email '{}' has been reseted.", email);
        return savedUser;
    }

    @Transactional
    @Override
    public void setActivationCode(final String email, final String resetCode) {
        log.debug("Attempting to activate user with email '{}'.", email);

        var user = this.findByEmail(email);
        user.setActivationCode(resetCode);
        this.userRepository.save(user);

        log.info("User with email '{}' has been activated.", email);
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
