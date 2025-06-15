package com.app.ecom.mapper;

import com.app.ecom.dto.AddressDTO;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.model.Address;
import com.app.ecom.model.User;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class UserMapper {

    public UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setRole(user.getRole());

        if (!ObjectUtils.isEmpty(user.getAddress())) {
            AddressDTO addressDTO = new AddressDTO();
            Address address = user.getAddress();
            addressDTO.setStreet(address.getStreet());
            addressDTO.setCity(address.getCity());
            addressDTO.setState(address.getState());
            addressDTO.setCountry(address.getCountry());
            addressDTO.setZipcode(address.getZipcode());
            userResponse.setAddressDTO(addressDTO);
        }
        return userResponse;
    }

    public User mapToUserEntity(UserRequest userRequest) {
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());

        if (!ObjectUtils.isEmpty(userRequest.getAddress())) {
            AddressDTO requestAddress = userRequest.getAddress();
            Address address = new Address();
            address.setStreet(requestAddress.getStreet());
            address.setCity(requestAddress.getCity());
            address.setState(requestAddress.getState());
            address.setCountry(requestAddress.getCountry());
            address.setZipcode(requestAddress.getZipcode());
            user.setAddress(address);
        }
        return user;
    }
}
