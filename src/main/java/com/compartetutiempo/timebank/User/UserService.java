package com.compartetutiempo.timebank.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compartetutiempo.timebank.exceptions.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // @Transactional
    // public User saveUser(User user) throws DataAccessException {
    //     return userRepository.save(user);
    // }

    @Transactional(readOnly = true)
    public User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    @Transactional(readOnly = true)
    public User findUser(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Transactional(readOnly = true)
    public User findCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            throw new AuthenticationCredentialsNotFoundException("No hay usuario autenticado actualmente.");
        }

        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return userRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Iterable<User> findAllByAuthority(Authority authority) {
        return userRepository.findAllByAuthority(authority);
    }

    // @Transactional
    // public User updateUser(@Valid User user, Integer idToUpdate) {
    //     User toUpdate = findUser(idToUpdate);
    //     BeanUtils.copyProperties(user, toUpdate, "id");
    //     userRepository.save(toUpdate);

    //     return toUpdate;
    // }
}