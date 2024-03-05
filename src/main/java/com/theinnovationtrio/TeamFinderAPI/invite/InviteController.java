package com.theinnovationtrio.TeamFinderAPI.invite;

import com.theinnovationtrio.TeamFinderAPI.organization.Organization;
import com.theinnovationtrio.TeamFinderAPI.user.IUserService;
import com.theinnovationtrio.TeamFinderAPI.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class InviteController {
    private final IInviteService inviteService;
    private final IUserService userService;

    @GetMapping("/invites")
    public ResponseEntity<List<Invite>> getAllInvites(){
        List<Invite> invites = inviteService.getAllInvites();
        if(invites.isEmpty()){
            return ResponseEntity.noContent().header("Message", "Nu este nici o invitatie.").build();
        } else {
            return ResponseEntity.ok(invites);
        }
    }

    @GetMapping("/invites/{inviteId}")
    public ResponseEntity<?> getInviteById(@PathVariable UUID inviteId){
        try{
            Invite invite = inviteService.getInviteById(inviteId);
            return ResponseEntity.ok(invite);
        } catch (EntityNotFoundException ex){
            String errorMessage = "Invitatia cu ID-ul " + inviteId + " nu a fost gasita.";
            ErrorMessage errorResponse = new ErrorMessage(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse);
        }
    }

    @PostMapping("/users/{userId}/invites")
    public ResponseEntity<?> createInvite(@PathVariable UUID userId){
        User user = userService.getUserById(userId);
        Invite savedInvite = inviteService
                .createInvite(new InviteDto(user.getOrganizationId()), userId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedInvite.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/invites/{inviteId}")
    public void deleteInviteById(@PathVariable UUID inviteId){
        inviteService.deleteInviteById(inviteId);
    }

    @PutMapping("/invites/{inviteId}")
    public ResponseEntity<Invite> updateInviteStatus(@PathVariable UUID inviteId, @RequestBody boolean status){
        try{
            Invite updatedInvite = inviteService.updateInvite(inviteId,status);
            return ResponseEntity.ok(updatedInvite);
        } catch(EntityNotFoundException ex){
            return ResponseEntity.notFound().build();
        }
    }
}
