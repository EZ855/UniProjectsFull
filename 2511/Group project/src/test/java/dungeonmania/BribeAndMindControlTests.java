package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;


public class BribeAndMindControlTests {

    private void assertBattleCalculations(String enemyType, BattleResponse battle, boolean enemyDies, String configFilePath) {
        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", configFilePath));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType + "_health", configFilePath));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", configFilePath));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));

        for (RoundResponse round : rounds) {
            assertEquals(round.getDeltaCharacterHealth(), -(enemyAttack / 10), 0.001);
            assertEquals(round.getDeltaEnemyHealth(), -(playerAttack / 5), 0.001);
            enemyHealth += round.getDeltaEnemyHealth();
            playerHealth += round.getDeltaCharacterHealth();
        }

        if (enemyDies) {
            assertTrue(enemyHealth <= 0);
        } else {
            assertTrue(playerHealth <= 0);
        }
    }
    
    @Test
    public void mercenaryBribeNoGoldNoSceptre() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testBribeMercenary", "testBribeMercenaryBigRadius");

        String entityId = getEntities(res, "mercenary").get(0).getId();
        assertThrows(InvalidActionException.class, () -> dmc.interact(entityId));
    }

    @Test
    public void mercenaryBribeOutOfRadius() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testBribeMercenary", "testBribeMercenary");

        res = dmc.tick(Direction.UP);

        assertEquals(1, getInventory(res, "treasure").size());

        String entityId = getEntities(res, "mercenary").get(0).getId();
        assertThrows(InvalidActionException.class, () -> dmc.interact(entityId));
    }

    @Test
    public void mercenaryBribeGoldPaid() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testBribeMercenary", "testBribeMercenaryBigRadius");

        res = dmc.tick(Direction.UP);

        assertEquals(1, getInventory(res, "treasure").size());

        String entityId = getEntities(res, "mercenary").get(0).getId();
        res = dmc.interact(entityId);
        assertEquals(0, getInventory(res, "treasure").size());

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        DungeonResponse postBattleResponse = dmc.getDungeonResponseModel();
        assertEquals(0, postBattleResponse.getBattles().size());
    }

    @Test
    public void mercenaryBribeSceptre() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testBribeMercenary", "testBribeMercenary");

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.build("sceptre");

        assertEquals(1, getInventory(res, "sceptre").size());

        String entityId = getEntities(res, "mercenary").get(0).getId();
        res = dmc.interact(entityId);

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        DungeonResponse postBattleResponse = dmc.getDungeonResponseModel();
        assertEquals(0, postBattleResponse.getBattles().size());
    }

    @Test
    public void assassinBribeNoGoldNoSceptre() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testBribeAssassin", "testBribeMercenaryBigRadius");

        String entityId = getEntities(res, "assassin").get(0).getId();
        assertThrows(InvalidActionException.class, () -> dmc.interact(entityId));
    }

    @Test
    public void assassinBribeOutOfRadius() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testBribeAssassin", "testBribeMercenary");

        res = dmc.tick(Direction.UP);

        assertEquals(1, getInventory(res, "treasure").size());

        String entityId = getEntities(res, "assassin").get(0).getId();
        assertThrows(InvalidActionException.class, () -> dmc.interact(entityId));
    }

    // Seed equals the current dungeon tick, which is 2 for when interacting with the assassin. This always produces 0.731147
    @Test
    public void assassinBribeGoldPaid() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testBribeAssassin", "testBribeMercenaryBigRadius");

        res = dmc.tick(Direction.UP);

        assertEquals(1, getInventory(res, "treasure").size());

        String entityId = getEntities(res, "assassin").get(0).getId();
        res = dmc.interact(entityId);
        assertEquals(0, getInventory(res, "treasure").size());
    }

    @Test
    public void assassinBribeSceptre() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testBribeAssassin", "testBribeMercenary");

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.build("sceptre");

        assertEquals(1, getInventory(res, "sceptre").size());

        String entityId = getEntities(res, "assassin").get(0).getId();
        res = dmc.interact(entityId);

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        DungeonResponse postBattleResponse = dmc.getDungeonResponseModel();
        assertEquals(0, postBattleResponse.getBattles().size());
        
    }
    // Seed equals the current dungeon tick, which is 3 for when interacting with the assassin. This always produces 0.731057. The assassin fail rate here is 0.8.
    @Test
    public void assassinBribeFailFirst() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testBribeAssassin", "testBribeAssassinFail");

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);

        assertEquals(2, getInventory(res, "treasure").size());

        String entityId = getEntities(res, "assassin").get(0).getId();
        res = dmc.interact(entityId);
        assertEquals(1, getInventory(res, "treasure").size());
    }
}