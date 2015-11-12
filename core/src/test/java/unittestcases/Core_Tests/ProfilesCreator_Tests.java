package unittestcases.Core_Tests;

import com.tjazi.profiles.client.ProfilesClient;
import com.tjazi.profiles.messages.RegisterNewProfileResponseMessage;
import com.tjazi.profiles.messages.RegisterNewProfileResponseStatus;
import com.tjazi.profilescreator.messages.CreateBasicProfileRequestMessage;
import com.tjazi.profilescreator.messages.CreateBasicProfileResponseMessage;
import com.tjazi.profilescreator.messages.CreateBasicProfileResponseStatus;
import com.tjazi.profilescreator.service.core.ProfilesCreator;
import com.tjazi.profilescreator.service.core.ProfilesCreatorImpl;
import com.tjazi.security.client.SecurityClient;
import com.tjazi.security.messages.RegisterNewUserCredentialsResponseMessage;
import com.tjazi.security.messages.RegisterNewUserCredentialsResponseStatus;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by Krzysztof Wasiak on 22/10/2015.
 */

@RunWith(MockitoJUnitRunner.class)
public class ProfilesCreator_Tests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private ProfilesCreatorImpl profilesCreator;

    @Mock
    private SecurityClient securityClient;

    @Mock
    private ProfilesClient profilesClient;

    @Test
    public void createProfile_NullArgument_Test() {

        ProfilesCreator creator = new ProfilesCreatorImpl();

        thrown.expect(IllegalArgumentException.class);

        creator.createProfile(null);
    }

    @Test
    public void createProfile_CreateProfileInProfilesThenInSecurities_AllSuccess() {

        this.parametrizedCreateProfileTest(
                RegisterNewProfileResponseStatus.OK,
                RegisterNewUserCredentialsResponseStatus.OK,
                CreateBasicProfileResponseStatus.OK);
    }

    @Test
    public void createProfile_GeneralErrorOnSecurityRegistrationGeneralError_AllSuccess() {

        this.parametrizedCreateProfileTest(
                RegisterNewProfileResponseStatus.OK,
                RegisterNewUserCredentialsResponseStatus.GENERAL_ERROR,
                CreateBasicProfileResponseStatus.GENERAL_ERROR);
    }

    @Test
    public void createProfile_CreateProfileInProfilesThenInSecurities_DuplicateEmail() {

        this.parametrizedCreateProfileTest(
                RegisterNewProfileResponseStatus.USER_EMAIL_ALREADY_REGISTERED_WITH_DIFFERENT_USER,
                null,
                CreateBasicProfileResponseStatus.USER_EMAIL_ALREADY_REGISTERED);
    }

    @Test
    public void createProfile_GeneralErrorOnGeneralRegistrationError_DuplicateEmail() {

        this.parametrizedCreateProfileTest(
                RegisterNewProfileResponseStatus.GENERAL_REGISTRATION_ERROR,
                null,
                CreateBasicProfileResponseStatus.GENERAL_ERROR);
    }

    @Test
    public void createProfile_CreateProfileInProfilesThenInSecurities_UserNameEmail() {

        this.parametrizedCreateProfileTest(
                RegisterNewProfileResponseStatus.USER_NAME_ALREADY_REGISTERED,
                null,
                CreateBasicProfileResponseStatus.USER_NAME_ALREADY_REGISTERED);
    }

    private void parametrizedCreateProfileTest(
            RegisterNewProfileResponseStatus profileRegistrationResponseStatus,
            RegisterNewUserCredentialsResponseStatus credentialsRegistrationResponseStatus,
            CreateBasicProfileResponseStatus expectedCreateBasicProfileResponseStatus) {

        final String userName = "sample user name";
        final String md5PasswordHash = "sampleMD5PasswordHash";
        final String userEmail = "sample user email";
        final UUID newProfileUuid =
                profileRegistrationResponseStatus == RegisterNewProfileResponseStatus.OK ? UUID.randomUUID() : null;

        // we skip name and surname in simple profile creation
        // those can be added later, when updating profile
        // the reason: those are not critical for profile to work successfully
        String name = null;
        String surname = null;

        RegisterNewProfileResponseMessage registerNewProfileResponseMessage = new RegisterNewProfileResponseMessage();
        registerNewProfileResponseMessage.setRegisterNewProfileResponseStatus(profileRegistrationResponseStatus);
        registerNewProfileResponseMessage.setNewProfileUuid(newProfileUuid);

        when(profilesClient.registerNewProfile(userName, userEmail, name, surname))
                .thenReturn(registerNewProfileResponseMessage);

        RegisterNewUserCredentialsResponseMessage registerNewUserCredentialsResponseMessage = new RegisterNewUserCredentialsResponseMessage();
        registerNewUserCredentialsResponseMessage.setRegistrationStatus(credentialsRegistrationResponseStatus);

        when(securityClient.registerNewUserCredentials(newProfileUuid, md5PasswordHash))
                .thenReturn(registerNewUserCredentialsResponseMessage);

        CreateBasicProfileRequestMessage requestMessage = new CreateBasicProfileRequestMessage();
        requestMessage.setUserEmail(userEmail);
        requestMessage.setUserName(userName);
        requestMessage.setPasswordHash(md5PasswordHash);

        CreateBasicProfileResponseMessage profileCreationResponse = profilesCreator.createProfile(requestMessage);

        assertEquals(expectedCreateBasicProfileResponseStatus, profileCreationResponse.getResponseStatus());
        assertEquals(newProfileUuid, profileCreationResponse.getCreatedProfileUuid());

        verify(profilesClient, times(1)).registerNewProfile(userName, userEmail, name, surname);

        // security client should be called always ONLY if creating profile has succeed
        if (profileRegistrationResponseStatus == RegisterNewProfileResponseStatus.OK) {
            verify(securityClient, times(1)).registerNewUserCredentials(newProfileUuid, md5PasswordHash);
        }
    }
}
