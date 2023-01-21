package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getEntities;

public class BombTests {
    @Test
    @DisplayName("Test surrounding entities are removed when placing a bomb next to an active switch with config file bomb radius set to 1")
    public void placeBombRadius1() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius1");

        // Activate Switch
        res = dmc.tick(Direction.RIGHT);

        // Pick up Bomb
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(res, "bomb").size());

        // Place Cardinally Adjacent
        res = dmc.tick(Direction.RIGHT);
        String bombId = getInventory(res, "bomb").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(bombId));
        assertEquals(0, getInventory(res, "bomb").size());
        // Check Bomb exploded with radius 1
        //
        //              Boulder/Switch      Wall            Wall
        //              Bomb                Treasure
        //
        //              Treasure
        assertEquals(0, getEntities(res, "bomb").size());
        assertEquals(0, getEntities(res, "boulder").size());
        assertEquals(0, getEntities(res, "switch").size());
        assertEquals(1, getEntities(res, "wall").size());
        assertEquals(1, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size());
    }

    @Test
    @DisplayName("Test surrounding entities are removed when placing a bomb next to an active switch with config file bomb radius set to 1")
    public void placeBombThenSwitch() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius1");

        // Pick up Bomb
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "bomb").size());
        res = dmc.tick(Direction.RIGHT);
        // Place bomb below switch
        String bombId = getInventory(res, "bomb").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(bombId));
        // Check bomb placed and hasn't gone off
        assertEquals(0, getInventory(res, "bomb").size());
        assertEquals(1, getEntities(res, "bomb").size());
        assertEquals(1, getEntities(res, "boulder").size());
        assertEquals(1, getEntities(res, "switch").size());
        assertEquals(2, getEntities(res, "wall").size());
        assertEquals(2, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size());

        // Activate switch
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        // Check bomb gone off
        assertEquals(0, getEntities(res, "bomb").size());
        assertEquals(0, getEntities(res, "boulder").size());
        assertEquals(0, getEntities(res, "switch").size());
        assertEquals(1, getEntities(res, "wall").size());
        assertEquals(1, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size());
    }

    @Test
    public void placeBombCollectBomb() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius1");

        // Pick up Bomb
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "bomb").size());

        // Place bomb
        res = dmc.tick(Direction.RIGHT);
        String bombId = getInventory(res, "bomb").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(bombId));
        assertEquals(0, getInventory(res, "bomb").size());

        assertEquals(1, getEntities(res, "bomb").size());

        //Try pick up bomb again
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(0, getInventory(res, "bomb").size());
    }

    @Test
    public void assertThrowsIllegalArgument() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius1");
       // Collect treasure
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        String treasureId = getInventory(res, "treasure").get(0).getId();
        assertThrows(IllegalArgumentException.class, () -> dmc.tick(treasureId));
    }
    
    @Test
    public void assertThrowsInvalidAction() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius1");
        String bombId = getEntities(res, "bomb").get(0).getId();
        assertThrows(InvalidActionException.class, () -> dmc.tick(bombId));
    }
    
}
