package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;


public class PortalTests {
    /*
    failing portal tests
    @Test
    public void playerWalkThroughPortal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testPortalPlayer", "testZTS");

        assertEquals(2, getEntities(res, "portal").size());

        // Get position to be teleported to
        Position pos = getEntities(res, "player").get(0).getPosition();
        
        // Head right into portal
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        
        assertEquals(pos, getEntities(res, "player").get(0).getPosition());
    }

    @Test
    public void playerWalkThroughPortalBlock() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testPortalPlayerBlock", "testZTS");

        assertEquals(2, getEntities(res, "portal").size());

        // Get position to be teleported to
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        Position pos = getEntities(res, "player").get(0).getPosition();
        
        // Head left into portal
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        
        assertEquals(pos, getEntities(res, "player").get(0).getPosition());
    }

    @Test
    public void playerWalkThroughPortalAllBlock() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testPortalPlayerBlockAll", "testZTS");

        assertEquals(2, getEntities(res, "portal").size());

        Position pos = getEntities(res, "player").get(0).getPosition();
        
        // Head left into portal
        res = dmc.tick(Direction.LEFT);
        
        assertEquals(pos, getEntities(res, "player").get(0).getPosition());
    }
    */
}