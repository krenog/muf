package com.krenog.myf.event.entity;

import com.krenog.myf.event.entities.Event;
import com.krenog.myf.event.entities.EventMember;
import com.krenog.myf.event.entities.MemberRole;
import com.krenog.myf.user.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.krenog.myf.event.utils.EventUtils.getTestEventWithId;
import static com.krenog.myf.utils.TestUtils.getTestUserWithId;

@Tag("CommonTest")
public class EventMemberTests {

    @Test
    void eventMemberGuestTest() {
        Event event = getTestEventWithId();
        User user = getTestUserWithId();
        EventMember eventMember = EventMember.createGuest(user, event);
        Assertions.assertEquals(event.getId(), eventMember.getEvent().getId());
        Assertions.assertEquals(user.getId(), eventMember.getUser().getId());
        Assertions.assertEquals(MemberRole.GUEST, eventMember.getRole());
    }

    @Test
    void eventMemberOwnerTest() {
        Event event = getTestEventWithId();
        User user = getTestUserWithId();
        event.setOwner(user);
        EventMember eventMember = EventMember.createOwner(event);
        Assertions.assertEquals(event.getId(), eventMember.getEvent().getId());
        Assertions.assertEquals(user.getId(), eventMember.getUser().getId());
        Assertions.assertEquals(MemberRole.OWNER, eventMember.getRole());
    }


    @Test
    void eventMemberAdminTest() {
        Event event = getTestEventWithId();
        User user = getTestUserWithId();
        EventMember eventMember = EventMember.createAdmin(user, event);
        Assertions.assertEquals(event.getId(), eventMember.getEvent().getId());
        Assertions.assertEquals(user.getId(), eventMember.getUser().getId());
        Assertions.assertEquals(MemberRole.ADMIN, eventMember.getRole());
    }
}
