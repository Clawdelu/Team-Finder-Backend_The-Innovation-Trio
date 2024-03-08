package com.theinnovationtrio.TeamFinderAPI.invite;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class InviteController {
    private final IInviteService inviteService;

    @GetMapping("/invites")
    public ResponseEntity<List<Invite>> getAllInvites() {
        List<Invite> invites = inviteService.getAllInvites();
        if (invites.isEmpty()) {
            return ResponseEntity.noContent().header("Message", "There is no invitation.").build();
        } else {
            return ResponseEntity.ok(invites);
        }
    }

    @GetMapping("/invites/{inviteId}")
    public ResponseEntity<?> getInviteById(@PathVariable UUID inviteId) {
        try {
            Invite invite = inviteService.getInviteById(inviteId);
            return ResponseEntity.ok(invite);
        } catch (EntityNotFoundException ex) {
            String errorMessage = "The invitation with the ID " + inviteId + "was not found.";
            ErrorMessage errorResponse = new ErrorMessage(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse);
        }
    }

    @PostMapping("/{userId}/invites")
    public ResponseEntity<?> createInvite(@PathVariable UUID userId) {
        try {
            Invite savedInvite = inviteService
                    .createInvite(userId);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedInvite.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @DeleteMapping("/invites/{inviteId}")
    public void deleteInviteById(@PathVariable UUID inviteId) {
        inviteService.deleteInviteById(inviteId);
    }

    @PutMapping("/invites/{inviteId}")
    public ResponseEntity<Invite> updateInviteStatus(@PathVariable UUID inviteId, @RequestBody boolean status) {
        try {
            Invite updatedInvite = inviteService.updateInvite(inviteId, status);
            return ResponseEntity.ok(updatedInvite);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
