package unittests;

import com.tjazi.lib.messaging.rest.RestClient;
import com.tjazi.profilescreator.client.ProfilesCreatorClient;
import com.tjazi.profilescreator.client.ProfilesCreatorClientImpl;
import com.tjazi.profilescreator.messages.CreateBasicProfileRequestMessage;
import com.tjazi.profilescreator.messages.CreateBasicProfileResponseMessage;
import com.tjazi.profilescreator.messages.CreateBasicProfileResponseStatus;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Krzysztof Wasiak on 03/11/2015.
 */

@RunWith(MockitoJUnitRunner.class)
public class ProfilesCreatorClient_Tests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    public RestClient restClientMock;

    @Test
    public void constructor_ExceptionOnNullRestClient_Test() {
        thrown.expect(IllegalArgumentException.class);

        ProfilesCreatorClient client = new ProfilesCreatorClientImpl(null);
    }

    @Test
    public void createBasicProfile_ExceptionOnNullUserName_Test() {
        thrown.expect(IllegalArgumentException.class);

        ProfilesCreatorClient client = new ProfilesCreatorClientImpl(restClientMock);

        client.createBasicProfile(null, "sample@email.com",  "sample passwordhash");
    }

    @Test
    public void createBasicProfile_ExceptionOnNullUserEmail_Test() {
        thrown.expect(IllegalArgumentException.class);

        ProfilesCreatorClient client = new ProfilesCreatorClientImpl(restClientMock);

        client.createBasicProfile("sample user name", null,  "sample passwordhash");
    }

    @Test
    public void createBasicProfile_ExceptionOnNullPasswordHash_Test() {
        thrown.expect(IllegalArgumentException.class);

        ProfilesCreatorClient client = new ProfilesCreatorClientImpl(restClientMock);

        client.createBasicProfile("sample user name", "sample@email.com", null);
    }

    @Test
    public void createBasicProfile_CallRestClientReturnResult_Success_Test() {

        final String userName = "sample user name";
        final String userEmail = "sample@user.email";
        final String passwordHash = "sample password hash";
        final UUID newProfileUuid = UUID.randomUUID();

        CreateBasicProfileRequestMessage requestMessage = new CreateBasicProfileRequestMessage();
        requestMessage.setUserName(userName);
        requestMessage.setUserEmail(userEmail);
        requestMessage.setPasswordHash(passwordHash);

        CreateBasicProfileResponseMessage expectedResponseMessage = new CreateBasicProfileResponseMessage();
        expectedResponseMessage.setCreatedProfileUuid(newProfileUuid);
        expectedResponseMessage.setResponseStatus(CreateBasicProfileResponseStatus.OK);

        when(restClientMock.sendRequestGetResponse(any(CreateBasicProfileRequestMessage.class), eq(CreateBasicProfileResponseMessage.class)))
                .thenReturn(expectedResponseMessage);

        ProfilesCreatorClient client = new ProfilesCreatorClientImpl(restClientMock);

        // call
        CreateBasicProfileResponseMessage actualReturnMessage = client.createBasicProfile(userName, userEmail, passwordHash);

        // validate
        ArgumentCaptor<CreateBasicProfileRequestMessage> captor = ArgumentCaptor.forClass(CreateBasicProfileRequestMessage.class);

        verify(restClientMock, times(1)).sendRequestGetResponse(captor.capture(), eq(CreateBasicProfileResponseMessage.class));

        assertEquals(userName, captor.getValue().getUserName());
        assertEquals(userEmail, captor.getValue().getUserEmail());
        assertEquals(passwordHash, captor.getValue().getPasswordHash());
        
        assertEquals(newProfileUuid, actualReturnMessage.getCreatedProfileUuid());
        assertEquals(CreateBasicProfileResponseStatus.OK, actualReturnMessage.getResponseStatus());

    }
}
