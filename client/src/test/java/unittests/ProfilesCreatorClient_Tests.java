package unittests;

import com.tjazi.profilescreator.client.ProfilesCreatorClient;
import com.tjazi.profilescreator.client.ProfilesCreatorClientImpl;
import com.tjazi.profilescreator.messages.CreateBasicProfileRequestMessage;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by Krzysztof Wasiak on 03/11/2015.
 */

@RunWith(MockitoJUnitRunner.class)
public class ProfilesCreatorClient_Tests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    public MessageChannel messageChannel;

    @InjectMocks
    public ProfilesCreatorClientImpl profilesCreatorClient;

    @Test
    public void createBasicProfile_ExceptionOnNullUserName_Test() {
        thrown.expect(IllegalArgumentException.class);

        profilesCreatorClient.createBasicProfile(null, "sample@email.com",  "sample passwordhash");
    }

    @Test
    public void createBasicProfile_ExceptionOnNullUserEmail_Test() {
        thrown.expect(IllegalArgumentException.class);

        profilesCreatorClient.createBasicProfile("sample user name", null,  "sample passwordhash");
    }

    @Test
    public void createBasicProfile_ExceptionOnNullPasswordHash_Test() {
        thrown.expect(IllegalArgumentException.class);

        profilesCreatorClient.createBasicProfile("sample user name", "sample@email.com", null);
    }

    @Test
    public void createBasicProfile_CallRestClientReturnResult_Success_Test() {

        final String userName = "sample user name";
        final String userEmail = "sample@user.email";
        final String passwordHash = "sample password hash";

        CreateBasicProfileRequestMessage requestMessage = new CreateBasicProfileRequestMessage();
        requestMessage.setUserName(userName);
        requestMessage.setUserEmail(userEmail);
        requestMessage.setPasswordHash(passwordHash);

        // call
        profilesCreatorClient.createBasicProfile(userName, userEmail, passwordHash);

        // validate
        ArgumentCaptor<Message> captor =
                ArgumentCaptor.forClass(Message.class);

        verify(messageChannel, times(1)).send(captor.capture());

        CreateBasicProfileRequestMessage capturedMessage = (CreateBasicProfileRequestMessage) captor.getValue().getPayload();

        assertEquals(userName, capturedMessage.getUserName());
        assertEquals(userEmail, capturedMessage.getUserEmail());
        assertEquals(passwordHash, capturedMessage.getPasswordHash());
    }
}
