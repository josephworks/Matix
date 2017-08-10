package de.paxii.clarinet.event.events.client;

import de.paxii.clarinet.event.events.Event;
import lombok.Data;
import lombok.Getter;

/**
 * Created by iJuhan on 10.08.2017.
 */
@Data
public class AddFriendEvent implements Event{

    @Getter
    private String friendName;

    public AddFriendEvent(String friendName) {
        this.friendName = friendName;
    }
}
