package com.jimmy_d.notesserver.service;

import com.jimmy_d.notesserver.database.repository.UserRepository;
import com.jimmy_d.notesserver.exceptions.rest.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);
        try {
            UserDetails userDetails = userRepository.findByUsername(username)
                    .map(user -> new org.springframework.security.core.userdetails.User(
                            user.getUsername(),
                            user.getPassword(),
                            user.getRoles().stream()
                                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                                    .collect(Collectors.toList())))
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            log.info("User found: {}", username);
            return userDetails;
        } catch (UsernameNotFoundException exception) {
            log.warn("User not found exception for username: {}", username);
            throw new UserNotFoundException("username", username);
        }
    }
}
