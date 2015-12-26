package unittestcases.Controller_Tests;

import com.tjazi.profiles.client.ProfilesClient;
import com.tjazi.profilescreator.messages.CreateBasicProfileRequestMessage;
import com.tjazi.profilescreator.service.core.ProfilesCreatorImpl;
import com.tjazi.security.client.SecurityClient;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by Krzysztof Wasiak on 26/12/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProfilesCreator_Tests {

    @InjectMocks
    public ProfilesCreatorImpl profilesCreator;

    @Mock
    public ProfilesClient profilesClientMock;

    @Mock
    public SecurityClient securityClientMock;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void exceptionOnNullInputMessage_Test() {

        thrown.expect(IllegalArgumentException.class);
        profilesCreator.createProfile(null);
    }

    @Test
    public void exceptionOnNullOrEmptyUserName_Test() {

        CreateBasicProfileRequestMessage requestMessage = new CreateBasicProfileRequestMessage();
        requestMessage.setUserName(null);
        requestMessage.setPasswordHash("Sample password hash");
        requestMessage.setUserEmail("Sample email");

        thrown.expect(IllegalArgumentException.class);
        profilesCreator.createProfile(requestMessage);
    }

    @Test
    public void exceptionOnNullOrEmptyEmail_Test() {

        CreateBasicProfileRequestMessage requestMessage = new CreateBasicProfileRequestMessage();
        requestMessage.setUserName("Sample user name");
        requestMessage.setPasswordHash("Sample password hash");
        requestMessage.setUserEmail("");

        thrown.expect(IllegalArgumentException.class);
        profilesCreator.createProfile(requestMessage);
    }

    @Test
    public void exceptionOnNullOrEmptyPasswordHash_Test() {

        CreateBasicProfileRequestMessage requestMessage = new CreateBasicProfileRequestMessage();
        requestMessage.setUserName("Sample user name");
        requestMessage.setPasswordHash("");
        requestMessage.setUserEmail("Sample email");

        thrown.expect(IllegalArgumentException.class);
        profilesCreator.createProfile(requestMessage);
    }

    @Test
    public void passExceptionFromProfilesService_Test() {

        final String userName = "Sample user name";
        final String passwordHash = "Sample passwordHash";
        final String userEmail = "Sample user email";

        CreateBasicProfileRequestMessage requestMessage = new CreateBasicProfileRequestMessage();
        requestMessage.setUserName(userName);
        requestMessage.setUserEmail(userEmail);
        requestMessage.setPasswordHash(passwordHash);

        thrown.expect(IllegalArgumentException.class);

        when(profilesClientMock
                .registerNewProfile(any(UUID.class), eq(userName), eq(userEmail), eq(null), eq(null)))
        .thenThrow(IllegalArgumentException.class);

        // main call
        profilesCreator.createProfile(requestMessage);

        // validation
        verify(profilesClientMock, times(1)).registerNewProfile(any(UUID.class), eq(userName), eq(userEmail), eq(null), eq(null));
        verify(securityClientMock, times(0)).registerNewUserCredentials(any(), any());
    }

    @Test
    public void dontCallSecurityOnFailedProfileCall_Test() {

        final String userName = "Sample user name";
        final String passwordHash = "Sample passwordHash";
        final String userEmail = "Sample user email";

        CreateBasicProfileRequestMessage requestMessage = new CreateBasicProfileRequestMessage();
        requestMessage.setUserName(userName);
        requestMessage.setUserEmail(userEmail);
        requestMessage.setPasswordHash(passwordHash);

        when(profilesClientMock
                .registerNewProfile(any(UUID.class), eq(userName), eq(userEmail), eq(null), eq(null)))
                .thenReturn(false);

        // main call
        profilesCreator.createProfile(requestMessage);

        // validation
        verify(profilesClientMock, times(1)).registerNewProfile(any(UUID.class), eq(userName), eq(userEmail), eq(null), eq(null));
        verify(securityClientMock, times(0)).registerNewUserCredentials(any(), any());
    }

    @Test
    public void callSecurityAfterProfileCreation_Test() {

        final String userName = "Sample user name";
        final String passwordHash = "Sample passwordHash";
        final String userEmail = "Sample user email";

        CreateBasicProfileRequestMessage requestMessage = new CreateBasicProfileRequestMessage();
        requestMessage.setUserName(userName);
        requestMessage.setUserEmail(userEmail);
        requestMessage.setPasswordHash(passwordHash);

        when(profilesClientMock
                .registerNewProfile(any(UUID.class), eq(userName), eq(userEmail), eq(null), eq(null)))
                .thenReturn(true);

        when(securityClientMock
                .registerNewUserCredentials(any(), any()))
                .thenReturn(true);

        // main call
        profilesCreator.createProfile(requestMessage);

        // validation
        ArgumentCaptor<UUID> profileUuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(profilesClientMock, times(1)).registerNewProfile(
                profileUuidArgumentCaptor.capture(), eq(userName), eq(userEmail), eq(null), eq(null));

        ArgumentCaptor<UUID> securityProfileUuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(securityClientMock, times(1)).registerNewUserCredentials(
                securityProfileUuidArgumentCaptor.capture(), eq(passwordHash));

        // validate if both UUIDs are the same
        assertEquals(profileUuidArgumentCaptor.getValue(), securityProfileUuidArgumentCaptor.getValue());
    }

}
