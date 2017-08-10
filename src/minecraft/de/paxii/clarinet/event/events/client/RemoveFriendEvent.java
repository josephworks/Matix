package de.paxii.clarinet.event.events.client;

import de.paxii.clarinet.event.events.Event;
import lombok.Data;
import lombok.Getter;

/**
 * Created by iJuhan on 10.08.2017.
 */

@Data
public class RemoveFriendEvent implements Event {

    @Getter
    private String friendName;

    public RemoveFriendEvent(String friendName) {
        this.friendName = friendName;
    }
}
