package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import static dungeonmania.TestUtils.getInventory;


public class BuildTests {
    @Test
    public void testCollectAndBuildBow() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("testBuildBow", "c_movementTest_testMovementDown");

        // collect arrows and wood
        dmc.tick(Direction.DOWN);
        // ensures wood in inventory
        assertEquals(1, getInventory(dmc.getDungeonResponseModel(), "wood").size());
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);

        // build without enough materials
        assertThrows(InvalidActionException.class, () -> dmc.build("bow"));

        dmc.tick(Direction.DOWN);
        DungeonResponse res = dmc.build("bow");

        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(0, getInventory(res, "arrow").size());
        assertEquals(1, getInventory(res, "bow").size());
    }

    @Test
    public void testCollectAndBuildShieldTreasure() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("testBuildShield", "c_movementTest_testMovementDown");

        // collect wood
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);

        // build without enough materials
        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));

        // collect treasure
        dmc.tick(Direction.DOWN);
        DungeonResponse res = dmc.build("shield");

        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(0, getInventory(res, "treasure").size());
        assertEquals(1, getInventory(res, "shield").size());
    }
    @Test
    public void testCollectAndBuildShieldKey() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("testBuildShield", "c_movementTest_testMovementDown");

        // collect key and wood
        dmc.tick(Direction.UP);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        DungeonResponse res = dmc.build("shield");

        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(0, getInventory(res, "key").size());
        assertEquals(1, getInventory(res, "shield").size());
    }
    @Test
    public void testCollectAndBuildShieldKeyAndTreasure() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("testBuildShield", "c_movementTest_testMovementDown");

        // collect key, wood and treasure
        dmc.tick(Direction.UP);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        DungeonResponse res = dmc.build("shield");

        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(0, getInventory(res, "treasure").size());
        assertEquals(1, getInventory(res, "key").size());
        assertEquals(1, getInventory(res, "shield").size());
    }
    @Test
    public void testBuildIllegalArgumentException() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_movementTest_testMovementDown", "c_movementTest_testMovementDown");
        
        assertThrows(IllegalArgumentException.class, () -> dmc.build("player"));
    }
    //@Test IOException, JSONException? If no tests, 8 lines missed in coverage report
}
