package com.theinnovationtrio.TeamFinderAPI.auth;

import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import com.theinnovationtrio.TeamFinderAPI.invite.IInviteService;
import com.theinnovationtrio.TeamFinderAPI.invite.Invite;
import com.theinnovationtrio.TeamFinderAPI.invite.InviteService;
import com.theinnovationtrio.TeamFinderAPI.organization.Organization;
import com.theinnovationtrio.TeamFinderAPI.organization.OrganizationDto;
import com.theinnovationtrio.TeamFinderAPI.organization.OrganizationService;
import com.theinnovationtrio.TeamFinderAPI.user.*;
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

import java.util.*;

@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IInviteService inviteService;
    @Autowired
    private IUserService userService;
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //String jwtToken = jwtTokenProvider.generateToken(authentication);

            User user = userRepository.findByEmail(loginDto.getEmail());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User login successfully!...");
            response.put("userId", user.getId());
           // UserIdDto userIdDto = new UserIdDto(user.getId());

            return ResponseEntity.ok(response);
            //return new ResponseEntity<>("User login successfully!...", HttpStatus.OK);
        }catch (AuthenticationException e) {
            return new ResponseEntity<>("Authentication failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/signup/{inviteId}")
    public ResponseEntity<?> registerUser(@PathVariable UUID inviteId, @RequestBody SignUpDto signUpDto){
        if(!inviteService.existsById(inviteId)){
            return new ResponseEntity<>("The invitation is not valid!", HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("Email is already exist!", HttpStatus.BAD_REQUEST);
        }
        Invite invite = inviteService.getInviteById(inviteId);
        if(!invite.isAvailable()){
            return new ResponseEntity<>("The invitation is not valid!", HttpStatus.BAD_REQUEST);
        }
        try{
            userService.createUser(signUpDto,invite.getOrganizationId());
            inviteService.updateInvite(inviteId,false);
            return new ResponseEntity<>("User is registered successfully!", HttpStatus.CREATED);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminSignUpDto adminSignUpDto){
        if(userRepository.existsByEmail(adminSignUpDto.getEmail())){
            return new ResponseEntity<>("Email is already exist!", HttpStatus.BAD_REQUEST);
        }
        try{
            userService.createUser(adminSignUpDto);
            return new ResponseEntity<>("User is registered successfully!", HttpStatus.CREATED);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }

    }
}
