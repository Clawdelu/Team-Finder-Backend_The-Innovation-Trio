package com.theinnovationtrio.TeamFinderAPI.auth;

import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import com.theinnovationtrio.TeamFinderAPI.user.User;
import com.theinnovationtrio.TeamFinderAPI.user.UserMapper;
import com.theinnovationtrio.TeamFinderAPI.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  UserMapper userMapper;
//    @Autowired
//    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new ResponseEntity<>("User login successfully!...", HttpStatus.OK);
        }catch (AuthenticationException e) {
            return new ResponseEntity<>("Authentication failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/signup/{organizationId}")
    public ResponseEntity<?> registerUser(@PathVariable UUID organizationId, @RequestBody SignUpDto signUpDto){
        if(userRepository.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("Email is already exist!", HttpStatus.BAD_REQUEST);
        }
        //User user = userMapper.INSTANCE.userDtotoUser(signUpDto);

        String encryptedPassword = passwordEncoder.encode(signUpDto.getPassword());
        User user = userMapper.signUpDtotoUser(signUpDto, encryptedPassword);
        user.setRoles(new ArrayList<>(Arrays.asList(Role.Employee)));
        user.setId(UUID.randomUUID());
        user.setAvailableHours(8);
        user.setOrganizationId(organizationId);
        /*
        User user = new User();
        user.setUserName(signUpDto.getName());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setRoles(new ArrayList<>(Arrays.asList(Role.Employee)));
        user.setId(UUID.randomUUID());
        user.setAvailableHours(8);
        user.setOrganizationId(organizationID);
        */

       // Role roles = roleRepository.findByName("ROLE_ADMIN").get();
       // user.setRoles(Collections.singleton(roles));
        userRepository.save(user);
        return new ResponseEntity<>("User is registered successfully!", HttpStatus.OK);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminSignUpDto adminSignUpDto){
        if(userRepository.existsByEmail(adminSignUpDto.getEmail())){
            return new ResponseEntity<>("Email is already exist!", HttpStatus.BAD_REQUEST);
        }
        //User user = userMapper.INSTANCE.userDtotoUser(signUpDto);

        User user = new User();
        user.setUserName(adminSignUpDto.getName());
        user.setEmail(adminSignUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(adminSignUpDto.getPassword()));
        user.setRoles(new ArrayList<>(Arrays.asList(Role.Employee)));
        user.setId(UUID.randomUUID());
        user.setAvailableHours(8);

        //user.setOrganizationId(adminSignUpDto.getOrganizationName());
        // Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        // user.setRoles(Collections.singleton(roles));
        userRepository.save(user);
        return new ResponseEntity<>("User is registered successfully!11", HttpStatus.CREATED);
    }
}
