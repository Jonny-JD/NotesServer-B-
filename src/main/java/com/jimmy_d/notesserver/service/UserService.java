package com.jimmy_d.notesserver.service;

import com.jimmy_d.notesserver.database.entity.User;
import com.jimmy_d.notesserver.database.repository.UserRepository;
import com.jimmy_d.notesserver.dto.UserCreateDto;
import com.jimmy_d.notesserver.mapper.UserCreateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserCreateMapper userCreateMapper;

    @Transactional
    public Long save(UserCreateDto userCreateDto) {
        var user = userCreateMapper.map(userCreateDto);
        var savedUser = userRepository.saveAndFlush(user);
        return savedUser.getId();
    }

    @Transactional
    public boolean deleteByUsername(String username) {
        return userRepository.deleteByUsername(username) != 0;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
