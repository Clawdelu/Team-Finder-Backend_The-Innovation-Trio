package com.theinnovationtrio.TeamFinderAPI.user;

import com.theinnovationtrio.TeamFinderAPI.auth.AdminRegisterRequest;
import com.theinnovationtrio.TeamFinderAPI.auth.UserRegisterRequest;
import com.theinnovationtrio.TeamFinderAPI.auth1.AdminSignUpDto;
import com.theinnovationtrio.TeamFinderAPI.auth1.SignUpDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userDtotoUser(UserDto userDto);

    @Mapping(target = "password", ignore = true)
    @Mapping(source = "name", target = "userName")
    User signUpDtotoUser(SignUpDto signUpDto);

    default User signUpDtotoUser(SignUpDto signUpDto, String password) {
        User user = signUpDtotoUser(signUpDto);
        user.setPassword(password);
        return user;
    }
    @Mapping(target = "password", ignore = true)
    @Mapping(source = "name", target = "userName")
    User adminSignUpDtotoUser(AdminSignUpDto adminSignUpDto);
    default User adminSignUpDtotoUser(AdminSignUpDto adminSignUpDto, String password){
        User user = adminSignUpDtotoUser(adminSignUpDto);
        user.setPassword(password);
        return user;
    }

    @Mapping(target = "password", ignore = true)
    @Mapping(source = "name", target = "userName")
    User mapToUser(AdminRegisterRequest adminRegisterRequest);
    default User mapToUser(AdminRegisterRequest adminRegisterRequest, String password){
        User user = mapToUser(adminRegisterRequest);
        user.setPassword(password);
        return user;
    }

    @Mapping(target = "password", ignore = true)
    @Mapping(source = "name", target = "userName")
    User mapToUser(UserRegisterRequest userRegisterRequest);
    default User mapToUser(UserRegisterRequest userRegisterRequest, String password){
        User user = mapToUser(userRegisterRequest);
        user.setPassword(password);
        return user;
    }


}
