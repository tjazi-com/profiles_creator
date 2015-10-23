package Controller_Tests;

import com.tjazi.profilescreator.service.controller.IndexController;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Krzysztof Wasiak on 22/10/2015.
 */
public class IndexController_Tests {

    @Test
    public void displayStatusPage_Test() {

        IndexController controller = new IndexController();

        assertEquals("status", controller.displayStatusPage());
    }
}
