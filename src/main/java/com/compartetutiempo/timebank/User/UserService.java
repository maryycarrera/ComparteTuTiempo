package com.compartetutiempo.timebank.user;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository; 

    @Transactional
    public UserResponse updateUser(UserRequest userRequest) {
    
        User user = User.builder()
            .id(userRequest.id)
            .name(userRequest.getName())
            .lastName(userRequest.getLastName())
            .email(userRequest.getEmail())
            .role(Role.MEMBER)
            .build();

        userRepository.updateUser(user.id, user.name, user.lastName);

        return new UserResponse("El usuario se actualizó correctamente");
    }

    public UserDTO getUser(Integer id) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            UserDTO userDTO = UserDTO.builder()
                .id(user.id)
                .username(user.username)
                .name(user.name)
                .lastName(user.lastName)
                .email(user.email)
                .build();
            
            return userDTO;
        }

        return null;
    }
}