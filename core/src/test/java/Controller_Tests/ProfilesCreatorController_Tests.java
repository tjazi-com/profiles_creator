package Controller_Tests;

import com.tjazi.profilescreator.service.controller.ProfilesCreatorController;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by kr329462 on 22/10/2015.
 */
public class ProfilesCreatorController_Tests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void createProfile_NullInput() {

        ProfilesCreatorController controller = new ProfilesCreatorController();

        thrown.expect(IllegalArgumentException.class);
        controller.createBasicProfile(null);

    }
}
