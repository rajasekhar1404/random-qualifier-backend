package com.randomqualifier.controller;

import com.randomqualifier.model.Group;
import com.randomqualifier.model.GroupUsers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class WebSocketController {

    Map<String, List<String>> groupUsers = new HashMap<>();

    @MessageMapping("/join")
    @SendTo("/qualifier/join")
    public Map<String, List<String>> joinGroup(@Payload Group group) {

        if (group.getUsername() == null || group.getUsername().isEmpty()) {
            return groupUsers;
        }

        List<String> users = groupUsers.get(group.getGroupId());
        if (users == null || users.size() == 0) {
            groupUsers.put(group.getGroupId(), Arrays.asList(group.getUsername()));
//            return groupUsers.get(group.getGroupId());
            return groupUsers;
        }

        List<String> existingUsers = new ArrayList<>(users);
        existingUsers.add(group.getUsername());

        groupUsers.put(group.getGroupId(), existingUsers);

        return groupUsers;
    }

    @MessageMapping("/pickone")
    @SendTo("/qualifier/pickone")
    public GroupUsers pickQualifier(@Payload GroupUsers groupUsers) {
        if (groupUsers != null && groupUsers.getUsers().size() > 0 ) {
            Random random = new Random();
            int index = random.nextInt(groupUsers.getUsers().size());
            String qualifier = groupUsers.getUsers().get(index);
            System.out.println(qualifier);
            groupUsers.setQualifier(qualifier);
            return groupUsers;
        }
        return null;
    }


}