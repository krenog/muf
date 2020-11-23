package com.krenog.myf.event.services.invite;

import com.krenog.myf.dto.FilterParameters;
import com.krenog.myf.event.entities.Invite;

import java.util.List;

public interface InviteService {
    List<Invite> getUserInvites(Long userId, FilterParameters filterParameters);

    void checkAndCreateInvite(InviteData inviteData);

    void acceptInvite(Long userId,Long inviteId);

    void rejectInvite(Long userId,Long inviteId);
}
