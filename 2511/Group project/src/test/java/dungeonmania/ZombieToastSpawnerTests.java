package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;


public class ZombieToastSpawnerTests {
    @Test
    public void spawnZeroZombieToast() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testZTSBlocked", "testZTS0");

        assertEquals(0, getEntities(res, "zombie_toast").size());

        res = dmc.tick(Direction.UP);

        assertEquals(0, getEntities(res, "zombie_toast").size());
    }

    @Test
    public void spawnOneZombieToast() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testZTS", "testZTS");

        assertEquals(0, getEntities(res, "zombie_toast").size());

        res = dmc.tick(Direction.UP);

        assertEquals(1, getEntities(res, "zombie_toast").size());

        res = dmc.tick(Direction.UP);

        assertEquals(2, getEntities(res, "zombie_toast").size());
    }

    @Test
    public void spawnOneZombieToastThreeBlocked() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testZTSThreeBlocked", "testZTS");

        assertEquals(0, getEntities(res, "zombie_toast").size());

        res = dmc.tick(Direction.UP);

        assertEquals(1, getEntities(res, "zombie_toast").size());

        res = dmc.tick(Direction.UP);

        assertEquals(2, getEntities(res, "zombie_toast").size());
    }

    @Test
    public void spawnZombieToastTickEqualsFive() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testZTS", "testZTSFive");

        assertEquals(0, getEntities(res, "zombie_toast").size());

        res = dmc.tick(Direction.UP); // tick = 0
        res = dmc.tick(Direction.UP); // tick = 1
        res = dmc.tick(Direction.UP); // tick = 2
        res = dmc.tick(Direction.UP); // tick = 3
        //res = dmc.tick(Direction.UP); // tick = 4

        assertEquals(0, getEntities(res, "zombie_toast").size());
        res = dmc.tick(Direction.UP); // tick = 5 -> 0
        assertEquals(1, getEntities(res, "zombie_toast").size());

        res = dmc.tick(Direction.UP); // tick = 1
        res = dmc.tick(Direction.UP); // tick = 2
        res = dmc.tick(Direction.UP); // tick = 3
        res = dmc.tick(Direction.UP); // tick = 4
        assertEquals(1, getEntities(res, "zombie_toast").size());
        res = dmc.tick(Direction.UP); // tick = 5 -> 0
        assertEquals(2, getEntities(res, "zombie_toast").size());
    }

    @Test
    public void spawnZombieToastTickEqualsZero() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testZTS", "c_battleTests_basicMercenaryMercenaryDies");

        assertEquals(0, getEntities(res, "zombie_toast").size());

        res = dmc.tick(Direction.UP); // tick = 1
        res = dmc.tick(Direction.UP); // tick = 2
        res = dmc.tick(Direction.UP); // tick = 3
        res = dmc.tick(Direction.UP); // tick = 4

        assertEquals(0, getEntities(res, "zombie_toast").size());

    }

    @Test
    public void destroyZombieToastSpawner() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testZTS", "testZTS");

        // Collect sword
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(res, "sword").size());

        // Destroy spawner
        assertEquals(1, getEntities(res, "zombie_toast_spawner").size());
        String ztsId = getEntities(res, "zombie_toast_spawner").get(0).getId();
        assertDoesNotThrow(() -> dmc.interact(ztsId));
        assertEquals(1, getEntities(res, "zombie_toast_spawner").size());
    }

    /*@Test
    public void destroyZombieToastSpawnerIllegalArgument() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testZTS", "testZTS");

        assertEquals(1, getEntities(res, "zombie_toast_spawner").size());
        assertEquals(1, getEntities(res, "sword").size());
        String swordId = getEntities(res, "sword").get(0).getId();
        assertThrows(IllegalArgumentException.class, () -> dmc.interact(swordId));
    }

    @Test
    public void destroyZombieToastSpawnerInvalidAction() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testZTS", "testZTS");
        String ztsId = getEntities(res, "zombie_toast_spawner").get(0).getId();
        
        // Go next to spawner WITHOUT sword
        dmc.tick(Direction.RIGHT);
        assertThrows(InvalidActionException.class, () -> dmc.interact(ztsId));

        // Try destroying diagonally
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);
        assertThrows(InvalidActionException.class, () -> dmc.interact(ztsId));
        
    }*/
}
