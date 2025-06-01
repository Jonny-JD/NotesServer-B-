package com.jimmy_d.notesserver.service;

import com.jimmy_d.notesserver.database.repository.UserRepository;
import com.jimmy_d.notesserver.exceptions.rest.UserNotFoundException;
import com.jimmy_d.notesserver.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            return userRepository.findByUsername(username)
                    .map(user -> new CustomUserPrincipal(
                            user.getId(),
                            user.getUsername(),
                            user.getPassword(),
                            new HashSet<>(user.getRoles())))
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        } catch (UsernameNotFoundException exception) {

            throw new UserNotFoundException("username", username);
        }
    }
}
