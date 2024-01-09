package ru.ystu.encryptionapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ystu.encryptionapp.dto.UserDTO;
import ru.ystu.encryptionapp.entity.UserEntity;
import ru.ystu.encryptionapp.enumeration.Provider;
import ru.ystu.encryptionapp.enumeration.Role;
import ru.ystu.encryptionapp.exception.UserNotFoundException;
import ru.ystu.encryptionapp.mapper.UserMapper;
import ru.ystu.encryptionapp.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository repository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.repository = repository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDTO createUser(UserEntity user) {
        if (repository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with such email is already registered");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ADMIN"));

        if (!isAdmin && user.getRole() != null && user.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Только администраторы могут создавать других администраторов");
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setJoinDate(LocalDate.now());
        user.setProvider(Provider.LOCAL);
        user.setEnabled(true);

        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        return UserMapper.INSTANCE.userToUserDTO(repository.save(user));
    }

    @Override
    public UserDTO getUserById(UUID id) {
        return UserMapper.INSTANCE.userToUserDTO(repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        return UserMapper.INSTANCE.userToUserDTO(repository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username)));
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        Page<UserEntity> userEntityPage = repository.findAll(pageable);
        List<UserDTO> users = userEntityPage.stream()
                .map(UserMapper.INSTANCE::userToUserDTO)
                .toList();

        return new PageImpl<>(users, pageable, userEntityPage.getTotalElements());
    }

    @Override
    public UserDTO updateUserById(UUID id, UserEntity user) {
        UserEntity savedUser = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return updateUserFields(user, savedUser);
    }

    @Override
    public UserDTO updateUserByUsername(String username, UserEntity user) {
        UserEntity savedUser = repository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return updateUserFields(user, savedUser);
    }

    private UserDTO updateUserFields(UserEntity user, UserEntity savedUser) {
        if (user.getId() != null) {
            throw new IllegalArgumentException("Rejected: id cannot be changed by this method");
        }
        if (user.getUsername() != null) {
            throw new IllegalArgumentException("Rejected: username cannot be changed by this method");
        }
        if (user.getPassword() != null) {
            throw new IllegalArgumentException("Rejected: password cannot be changed by this method");
        }
        if (user.getEnabled() != null) {
            throw new IllegalArgumentException("Rejected: enabled param cannot be changed by this method");
        }

        savedUser.setName(user.getName() != null ? user.getName() : savedUser.getName());
        savedUser.setRole(user.getRole() != null ? user.getRole() : savedUser.getRole());
        savedUser.setEmail(user.getEmail() != null ? user.getEmail() : savedUser.getEmail());
        savedUser.setProvider(user.getProvider() != null ? user.getProvider() : savedUser.getProvider());
        savedUser.setJoinDate(user.getJoinDate() != null ? user.getJoinDate() : savedUser.getJoinDate());

        return UserMapper.INSTANCE.userToUserDTO(repository.save(savedUser));
    }

    @Override
    public boolean deleteUserById(UUID id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);

            return repository.findById(id).isEmpty();
        } else {
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public boolean deleteUserByUsername(String username) {
        if (repository.findByUsername(username).isPresent()) {
            repository.deleteByUsername(username);

            return repository.findByUsername(username).isEmpty();
        } else {
            throw new UserNotFoundException(username);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));

        if (!user.isEnabled()) {
            throw new DisabledException(String.format("User %s is banned", username));
        }

        return user;
    }

    @Override
    public void processGoogleOAuth2PostLogin(String email, String name) {
        saveUser(email, name, email, Provider.GOOGLE);
    }

    @Override
    public void processVKOAuth2PostLogin(String idAndFirstnameAndLastname, String name) {
        saveUser(idAndFirstnameAndLastname, name, null, Provider.VK);
    }

    @Override
    public void processGithubOAuth2PostLogin(String login, String name) {
        LOG.info("Name attribute: {}", name);
        saveUser(login, name, null, Provider.GITHUB);
    }

    @Override
    public void processDiscordOAuth2PostLogin(String username, String name, String email) {
        saveUser(username, name, email, Provider.DISCORD);
    }

    private void saveUser(String username, String name, String email, Provider provider) {
        Optional<UserEntity> existingUser = repository.findByUsername(username);

        if (existingUser.isEmpty()) {
            UserEntity user = new UserEntity();
            LOG.info("Name attribute: {}", name);
            user.setName(name);
            user.setUsername(username);
            user.setPassword(bCryptPasswordEncoder.encode(username));
            user.setRole(Role.USER);
            user.setEmail(email);
            user.setJoinDate(LocalDate.now());
            user.setProvider(provider);
            user.setEnabled(true);
            LOG.info("User created using " + provider.name() + " OAuth2: {}", UserMapper.INSTANCE.userToUserDTO(user));

            repository.save(user);
        }
    }
}
