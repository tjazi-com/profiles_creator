package unittestcases.Controller_Tests;

import com.tjazi.profilescreator.service.endpoint.ProfilesCreatorEndpoint;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by kr329462 on 22/10/2015.
 */
public class ProfilesCreatorController_Tests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void createProfile_NullInput() {

        ProfilesCreatorEndpoint controller = new ProfilesCreatorEndpoint();

        thrown.expect(IllegalArgumentException.class);
        //controller.createBasicProfile(null);

    }
}
