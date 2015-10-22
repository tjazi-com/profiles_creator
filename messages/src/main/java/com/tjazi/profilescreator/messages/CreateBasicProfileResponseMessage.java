package com.tjazi.profilescreator.messages;

import java.util.UUID;

/**
 * Created by Krzysztof Wasiak on 18/10/15.
 */
public class CreateBasicProfileResponseMessage {

    private CreateBasicProfileResponseStatus responseStatus;
    private UUID createdProfileUuid;

    public CreateBasicProfileResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(CreateBasicProfileResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public UUID getCreatedProfileUuid() {
        return createdProfileUuid;
    }

    public void setCreatedProfileUuid(UUID createdProfileUuid) {
        this.createdProfileUuid = createdProfileUuid;
    }
}
