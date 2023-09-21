package ru.ystu.encryptionapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.ystu.encryptionapp.dto.UserDTO;
import ru.ystu.encryptionapp.entity.UserEntity;

import java.util.Collection;
import java.util.UUID;

public interface UserService extends UserDetailsService {
    UserDTO createUser(UserEntity user);

    UserDTO getUserById(UUID id);

    UserDTO getUserByUsername(String username);

    Page<UserDTO> getAllUsers(Pageable pageable);

    UserDTO updateUserById(UUID id, UserEntity user);

    UserDTO updateUserByUsername(String username, UserEntity user);

    boolean deleteUserById(UUID id);

    boolean deleteUserByUsername(String username);

    void processGoogleOAuth2PostLogin(String email, String name);

    void processVKOAuth2PostLogin(String idAndFirstnameAndLastname, String name);

    void processGithubOAuth2PostLogin(String login, String name);

    void processDiscordOAuth2PostLogin(String username, String name, String email);
}
