package Core_Tests;

import com.tjazi.profilescreator.service.core.ProfilesCreator;
import com.tjazi.profilescreator.service.core.ProfilesCreatorImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by Krzysztof Wasiak on 22/10/2015.
 */
public class ProfilesCreator_Tests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void createProfile_NullArgument_Test() {

        ProfilesCreator creator = new ProfilesCreatorImpl();

        thrown.expect(IllegalArgumentException.class);

        creator.createProfile(null);

    }
}
