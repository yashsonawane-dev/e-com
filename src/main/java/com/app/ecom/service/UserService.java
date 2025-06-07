package com.app.ecom.service;

import com.app.ecom.dto.AddressDTO;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.mapper.UserMapper;
import com.app.ecom.model.Address;
import com.app.ecom.model.User;
import com.app.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponse> fetchAllUsers() {
        return userRepository.findAll().stream().map(userMapper::mapToUserResponse).collect(Collectors.toList());
    }

    public void addUser(UserRequest userRequest) {
        User user = userMapper.mapToUser(userRequest);
        userRepository.save(user);
    }

    public Optional<UserResponse> fetchUser(Long id) {
        return userRepository.findById(id)
                .map(userMapper::mapToUserResponse);
    }

    public boolean updateUser(Long id, UserRequest updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> { // map works like optionalUser.ifPresent()
                    updateUserFromRequest(existingUser, updatedUser);
                    userRepository.save(existingUser);
                    return true;
                }).orElse(false);
    }

    public void updateUserFromRequest(User existingUser, UserRequest userRequest) {
        existingUser.setFirstName(userRequest.getFirstName());
        existingUser.setLastName(userRequest.getLastName());
        existingUser.setEmail(userRequest.getEmail());
        existingUser.setPhone(userRequest.getPhone());

        if (!ObjectUtils.isEmpty(userRequest.getAddress())) {
            AddressDTO requestAddress = userRequest.getAddress();
            Address address = existingUser.getAddress();
            address.setStreet(requestAddress.getStreet());
            address.setCity(requestAddress.getCity());
            address.setState(requestAddress.getState());
            address.setCountry(requestAddress.getCountry());
            address.setZipcode(requestAddress.getZipcode());
        }
    }
}
