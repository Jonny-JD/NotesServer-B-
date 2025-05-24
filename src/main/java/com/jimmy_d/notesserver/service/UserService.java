package com.jimmy_d.notesserver.service;

import com.jimmy_d.notesserver.database.entity.Role;
import com.jimmy_d.notesserver.database.repository.UserRepository;
import com.jimmy_d.notesserver.dto.UserCreateDto;
import com.jimmy_d.notesserver.dto.UserReadDto;
import com.jimmy_d.notesserver.exceptions.rest.UserExistsException;
import com.jimmy_d.notesserver.mapper.UserCreateMapper;
import com.jimmy_d.notesserver.mapper.UserReadMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserCreateMapper userCreateMapper;
    private final UserReadMapper userReadMapper;

    @Transactional
    public Optional<UserReadDto> createUser(UserCreateDto user) {
        
        var foundedUser = userRepository.findOneByEmailOrUsername(user.email(), user.username());
        if (foundedUser.isPresent()) {
            var type = user.username().equals(foundedUser.get().getUsername()) ? "username" : "email";
            if (type.equals("username")) {
                throw new UserExistsException(type, user.username());
            } else {
                throw new UserExistsException(type, user.email());
            }
        }

        return Optional.of(user)
                .map(userCreateMapper::map)
                .map(userRepository::save)
                .map(userReadMapper::map);
    }

    public Optional<UserReadDto> updateUser(UserReadDto user) {
        
        return userRepository.findById(user.id())
                .map(userForUpdate -> {
                    userForUpdate.setUsername(user.username());
                    userForUpdate.setEmail(user.email());
                    userForUpdate.setRoles(user.roles().stream()
                            .map(Role::valueOf)
                            .collect(Collectors.toSet()));
                    return userReadMapper.map(userRepository.save(userForUpdate));
                });
    }

    @Transactional
    public boolean deleteByUsername(String username) {
        
        return userRepository.findByUsername(username)
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                }).orElse(false);
    }

    @Transactional
    public boolean deleteById(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                }).orElse(false);
    }

    public Optional<UserReadDto> findByUsername(String username) {
        return userRepository.findByUsername(username).map(userReadMapper::map);
    }

    public Optional<UserReadDto> findById(Long id) {
        return userRepository.findById(id).map(userReadMapper::map);
    }
}
