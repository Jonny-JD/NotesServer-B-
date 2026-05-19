package com.jimmy_d.notes_backend.service;

import com.jimmy_d.notes_backend.database.entity.Role;
import com.jimmy_d.notes_backend.database.repository.UserRepository;
import com.jimmy_d.notes_backend.dto.UserCreateDto;
import com.jimmy_d.notes_backend.dto.UserInfoDto;
import com.jimmy_d.notes_backend.dto.UserDetailsDto;
import com.jimmy_d.notes_backend.dto.UserUpdateDto;
import com.jimmy_d.notes_backend.exceptions.rest.RoleAlreadyExistsException;
import com.jimmy_d.notes_backend.exceptions.rest.RoleNotFoundException;
import com.jimmy_d.notes_backend.exceptions.rest.UserExistsException;
import com.jimmy_d.notes_backend.exceptions.rest.UserNotFoundException;
import com.jimmy_d.notes_backend.mapper.UserCreateMapper;
import com.jimmy_d.notes_backend.mapper.UserInfoMapper;
import com.jimmy_d.notes_backend.mapper.UserDetailsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.jimmy_d.notes_backend.exceptions.rest.Types.*;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserCreateMapper userCreateMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoMapper userInfoMapper;
    private final UserDetailsMapper userDetailsMapper;

    @Transactional
    public UserInfoDto createUser(UserCreateDto dto) {
        var findUser = userRepository.findOneByEmailOrUsername(dto.email(), dto.username());

        if (findUser.isPresent()) {
            boolean usernameExists = dto.username().equalsIgnoreCase(findUser.get().getUsername());
            var type = usernameExists ? USERNAME : EMAIL;
            if (usernameExists) {
                throw new UserExistsException(type.value(), dto.username());
            } else {
                throw new UserExistsException(type.value(), dto.email());
            }
        }
        var newUser = userCreateMapper.map(dto);

        return userInfoMapper.map(userRepository.save(newUser));

    }

    @Transactional
    public UserInfoDto updateUser(UserUpdateDto dto) {
        var findUser = userRepository.findById(dto.id())
                .orElseThrow(() -> new UserNotFoundException(ID.value(), dto.id()));

        userRepository.findByUsername(dto.username())
                .filter(u -> !u.getId().equals(dto.id()))
                .ifPresent(u -> {
                    throw new UserExistsException(USERNAME.value(), dto.username());
                });
        userRepository.findByEmail(dto.email())
                .filter(u -> !u.getId().equals(dto.id()))
                .ifPresent(u -> {
                    throw new UserExistsException(EMAIL.value(), dto.email());
                });

        if (dto.username() != null) findUser.setUsername(dto.username());
        if (dto.email() != null)findUser.setEmail(dto.email());
        if (dto.newPassword() != null) {
            if (!passwordEncoder.matches(dto.currentPassword(), findUser.getPassword())) {
                throw new IllegalArgumentException("Wrong current password");
            }
            findUser.setPassword(passwordEncoder.encode(dto.newPassword()));
        }

        return userInfoMapper.map(findUser);
    }


    @Transactional
    public void deleteByUsername(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new UserNotFoundException(USERNAME.value(), username);
        }
        userRepository.deleteByUsername(username);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(ID.value(), id);
        }
        userRepository.deleteById(id);
    }

    public UserInfoDto getByUsername(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(USERNAME.value(), username));

        return userInfoMapper.map(user);
    }

    public UserDetailsDto getById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(ID.value(), id));
        return userDetailsMapper.map(user);
    }

    @Transactional
    public void addUserRole(UserDetailsDto dto, Role role) {
        var user = userRepository.findById(dto.id())
                .orElseThrow(() -> new UserNotFoundException("id", dto.id()));
        if (user.getRoles().contains(role)) {
            throw new RoleAlreadyExistsException(role);
        }
        user.addRole(role);
    }

    @Transactional
    public void removeUserRole(UserDetailsDto dto, Role role) {
        var user = userRepository.findById(dto.id())
                .orElseThrow(() -> new UserNotFoundException("id", dto.id()));
        if (!user.getRoles().contains(role)) {
            throw new RoleNotFoundException(role);
        }
        user.removeRole(role);
    }
}
