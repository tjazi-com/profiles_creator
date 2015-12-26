package unittestcases.Controller_Tests;

import com.tjazi.profilescreator.messages.CreateBasicProfileRequestMessage;
import com.tjazi.profilescreator.service.core.ProfilesCreator;
import com.tjazi.profilescreator.service.endpoints.queuehandlers.ProfilesCreatorEndpoint;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Created by Krysztof Wasiak on 22/10/2015.
 */

@RunWith(MockitoJUnitRunner.class)
public class ProfilesCreatorEndpoint_Tests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    public ProfilesCreator profilesCreatorMock;

    @InjectMocks
    public ProfilesCreatorEndpoint profilesCreatorEndpoint;

    @Test
    public void createProfile_NullInput() {

        thrown.expect(IllegalArgumentException.class);
        profilesCreatorEndpoint.createBasicProfileRequestHandler(null);
    }

    @Test
    public void createProfile_PassMessageToCore() {

        CreateBasicProfileRequestMessage requestMessage = new CreateBasicProfileRequestMessage();

        profilesCreatorEndpoint.createBasicProfileRequestHandler(requestMessage);

        verify(profilesCreatorMock, times(1)).createProfile(requestMessage);
    }

    @Test
    public void createProfile_ReThrowException() {
        CreateBasicProfileRequestMessage requestMessage = new CreateBasicProfileRequestMessage();

        thrown.expect(IllegalArgumentException.class);
        doThrow(new IllegalArgumentException()).when(profilesCreatorMock).createProfile(requestMessage);

        // main call
        profilesCreatorEndpoint.createBasicProfileRequestHandler(requestMessage);
    }
}
