package com.theinnovationtrio.TeamFinderAPI.invite;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class InviteService implements IInviteService{
    private final InviteRepository inviteRepository;

    @Override
    public Invite createInvite(InviteDto inviteDto, UUID userId) {
        Invite invite = new Invite();
        invite.setId(UUID.randomUUID());
        invite.setOrganizationId(inviteDto.getOrganizationId());
        invite.setCreatedBy(userId);
        invite.setAvailable(true);
        return inviteRepository.save(invite);
    }

    @Override
    public boolean existsById(UUID inviteId) {
        return inviteRepository.existsById(inviteId);
    }

    @Override
    public Invite getInviteById(UUID inviteId) {
        return inviteRepository.findById(inviteId).orElseThrow(() -> new EntityNotFoundException("Invite not found"));
    }

    @Override
    public List<Invite> getAllInvites() {
        return inviteRepository.findAll();
    }

    @Override
    public Invite updateInvite(UUID inviteId, boolean available) {
        Invite invite = getInviteById(inviteId);
        invite.setAvailable(available);
        return inviteRepository.save(invite);
    }

    @Override
    public void deleteInviteById(UUID inviteId) {
        Invite invite = getInviteById(inviteId);
        inviteRepository.deleteById(inviteId);
    }
}
