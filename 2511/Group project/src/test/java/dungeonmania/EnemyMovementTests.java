package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.vfs.Vfs.Dir;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class EnemyMovementTests {
    private Position getPositionEntity(String type, DungeonManiaController dmc) {
        DungeonResponse dungeonResponse = dmc.getDungeonResponseModel();
        for (EntityResponse e : dungeonResponse.getEntities()) {
            if (e.getType().equals(type)) {
                System.out.println(e.getPosition());
                return e.getPosition();
            }
        }
        return null;
    }
    
    // Zombie movement
    @Test
    @DisplayName("Test zombies can't pass through walls and doors")
    public void testZombieNoPassWallAndDoor() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("enemyMovementTestZombieNoPassWall&Door", "enemyMovementTest");

        // get current zombie position 
        Position currentPosition = getPositionEntity("zombie_toast", controller);

        controller.tick(Direction.UP);
        assertTrue(currentPosition.equals(getPositionEntity("zombie_toast", controller)));
    }

    @Test
    @DisplayName("Test zombies move randomly in an open area")
    public void testZombieMovementInOpenArea() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("enemyMovementTestZombieOpenArea", "enemyMovementTest");

        // get current zombie position 
        Position currentPosition = getPositionEntity("zombie_toast", controller);

        controller.tick(Direction.UP);
        assertTrue(
                    (getPositionEntity("zombie_toast", controller).getX() == currentPosition.getX() + 1 && getPositionEntity("zombie_toast", controller).getY() == currentPosition.getY() || 
                    getPositionEntity("zombie_toast", controller).getX() == currentPosition.getX() - 1 && getPositionEntity("zombie_toast", controller).getY() == currentPosition.getY() ||
                    getPositionEntity("zombie_toast", controller).getX() == currentPosition.getX() && getPositionEntity("zombie_toast", controller).getY() == currentPosition.getY() + 1 ||
                    getPositionEntity("zombie_toast", controller).getX() == currentPosition.getX() && getPositionEntity("zombie_toast", controller).getY() == currentPosition.getY() - 1 ||
                    currentPosition.equals(getPositionEntity("zombie_toast", controller)))
                );
    }

    @Test
    @DisplayName("Test zombies move randomly when next to a wall")
    public void testZombieMovementNextToWall() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("enemyMovementTestZombieAdjacentWall", "enemyMovementTest");

        // get current zombie position 
        Position currentPosition = getPositionEntity("zombie_toast", controller);

        controller.tick(Direction.UP);
        assertTrue(
            (getPositionEntity("zombie_toast", controller).getX() == currentPosition.getX() + 1 && getPositionEntity("zombie_toast", controller).getY() == currentPosition.getY() ||
            getPositionEntity("zombie_toast", controller).getX() == currentPosition.getX() && getPositionEntity("zombie_toast", controller).getY() == currentPosition.getY() + 1 ||
            getPositionEntity("zombie_toast", controller).getX() == currentPosition.getX() && getPositionEntity("zombie_toast", controller).getY() == currentPosition.getY() - 1 ||
            getPositionEntity("zombie_toast", controller).getX() == currentPosition.getX() && getPositionEntity("zombie_toast", controller).getY() == currentPosition.getY())
        );
    }

    // Mercenary movement
    @Test
    @DisplayName("Test mercenaries move towards player when mercenary is in line with player")
    public void testMercenaryMovementInLine() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("enemyMovementTestMercenaryInLine", "enemyMovementTest");

        // get current mercenary position 
        Position position = getPositionEntity("mercenary", controller);

        for (int i = 1; i < 4; i++) {
            controller.tick(Direction.LEFT);
            assertTrue(getPositionEntity("mercenary", controller).getX() == position.getX() - i &&
            getPositionEntity("mercenary", controller).getY() == position.getY());
        }
    }

    @Test
    @DisplayName("Test mercenaries can no longer follow the player if it cannot move any closer to the player")
    public void testMercenaryMovementStopsNoLongerCloseToPlayer() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("enemyMovementTestMercenaryUnableToFollow", "enemyMovementTest");

        // get current mercenary position 
        Position position = getPositionEntity("mercenary", controller);


        controller.tick(Direction.UP);
        position.equals(getPositionEntity("mercenary", controller));
        controller.tick(Direction.UP);
        position.equals(getPositionEntity("mercenary", controller));
        controller.tick(Direction.RIGHT);
        position.equals(getPositionEntity("mercenary", controller));
        controller.tick(Direction.RIGHT);
        position.equals(getPositionEntity("mercenary", controller));
        controller.tick(Direction.RIGHT);
        position.equals(getPositionEntity("mercenary", controller));
        controller.tick(Direction.RIGHT);
        position.equals(getPositionEntity("mercenary", controller));
        controller.tick(Direction.DOWN);
        position.equals(getPositionEntity("mercenary", controller));
        controller.tick(Direction.DOWN);
        position.equals(getPositionEntity("mercenary", controller));
        controller.tick(Direction.DOWN);
        position.equals(getPositionEntity("mercenary", controller));
        controller.tick(Direction.DOWN);
        position.equals(getPositionEntity("mercenary", controller));
        controller.tick(Direction.LEFT);
        position.equals(getPositionEntity("mercenary", controller));
        controller.tick(Direction.LEFT);
        position.equals(getPositionEntity("mercenary", controller));
        controller.tick(Direction.LEFT);
        position.equals(getPositionEntity("mercenary", controller));
        controller.tick(Direction.LEFT);
        position.equals(getPositionEntity("mercenary", controller));
        controller.tick(Direction.UP);
        position.equals(getPositionEntity("mercenary", controller));
        controller.tick(Direction.UP);
        position.equals(getPositionEntity("mercenary", controller));
    }

    @Test
    @DisplayName("Test mercenaries can't pass through doors")
    public void testMercenaryMovementNoPassDoor() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("enemyMovementTestMercenaryNoPassDoor", "enemyMovementTest");

        // get current mercenary position 
        Position position = getPositionEntity("mercenary", controller);

        controller.tick(Direction.RIGHT);
        assertTrue(getPositionEntity("mercenary", controller).getX() == position.getX() - 1 &&
            getPositionEntity("mercenary", controller).getY() == position.getY());
        
        controller.tick(Direction.RIGHT);
        assertTrue(getPositionEntity("mercenary", controller).getX() == position.getX() - 1 &&
            getPositionEntity("mercenary", controller).getY() == position.getY() - 1);
    }

    @Test
    @DisplayName("Test mercenaries move around walls")
    public void testMercenaryMovementAroundWalls() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("enemyMovementTestMercenaryMoveAroundWall", "enemyMovementTest");

        controller.tick(Direction.RIGHT);
        assertTrue(getPositionEntity("mercenary", controller).getX() == 4 &&
            getPositionEntity("mercenary", controller).getY() == 3);
        
        controller.tick(Direction.RIGHT);
        assertTrue(getPositionEntity("mercenary", controller).getX() == 4 &&
            getPositionEntity("mercenary", controller).getY() == 4);

        controller.tick(Direction.RIGHT);
        assertTrue(getPositionEntity("mercenary", controller).getX() == 3 &&
            getPositionEntity("mercenary", controller).getY() == 4);

        controller.tick(Direction.RIGHT);
        assertTrue(getPositionEntity("mercenary", controller).getX() == 2 &&
            getPositionEntity("mercenary", controller).getY() == 4);

        controller.tick(Direction.RIGHT);
        assertTrue(getPositionEntity("mercenary", controller).getX() == 2 &&
            getPositionEntity("mercenary", controller).getY() == 3);
    }

    @Test
    @DisplayName("Test mercenaries move randomly when player is invisible")
    public void testMercenaryMovementRandomInvisible() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("enemyMovementTestMercenaryRandomInvisible", "enemyMovementTest");

        controller.tick(Direction.RIGHT);
        DungeonResponse dungeonResponse = controller.getDungeonResponseModel();
        String itemId = dungeonResponse.getInventory().get(0).getId();

        Position currentPosition = getPositionEntity("mercenary", controller);
        controller.tick(itemId);

        assertTrue(
            (getPositionEntity("mercenary", controller).getX() == currentPosition.getX() + 1 && getPositionEntity("mercenary", controller).getY() == currentPosition.getY() || 
            getPositionEntity("mercenary", controller).getX() == currentPosition.getX() - 1 && getPositionEntity("mercenary", controller).getY() == currentPosition.getY() ||
            getPositionEntity("mercenary", controller).getX() == currentPosition.getX() && getPositionEntity("mercenary", controller).getY() == currentPosition.getY() + 1 ||
            getPositionEntity("mercenary", controller).getX() == currentPosition.getX() && getPositionEntity("mercenary", controller).getY() == currentPosition.getY() - 1 ||
            currentPosition.equals(getPositionEntity("mercenary", controller)))
        );
    }

    @Test
    @DisplayName("Test mercenaries run away when player is invincible")
    public void testMercenaryMovementRunAwayInvincible() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("enemyMovementTestMercenaryRunAwayInvincible", "enemyMovementTest");

        Position currentPosition = getPositionEntity("mercenary", controller);

        controller.tick(Direction.RIGHT);
        DungeonResponse dungeonResponse = controller.getDungeonResponseModel();
        String itemId = dungeonResponse.getInventory().get(0).getId();
        controller.tick(itemId);

        assertTrue(
            (getPositionEntity("mercenary", controller).getX() == currentPosition.getX() && 
            getPositionEntity("mercenary", controller).getY() == currentPosition.getY())
        );
    }

    // Hydra movement
    @Test
    @DisplayName("Test hydras can't pass through walls and doors")
    public void testHydraNoPassWallAndDoor() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("enemyMovementTestHydraNoPassWall&Door", "enemyMovementTest");

        // get current hydra position 
        Position currentPosition = getPositionEntity("hydra", controller);

        controller.tick(Direction.UP);
        assertTrue(currentPosition.equals(getPositionEntity("hydra", controller)));
    }

    @Test
    @DisplayName("Test hydra move randomly in an open area")
    public void testHydraMovementInOpenArea() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("enemyMovementTestHydraOpenArea", "enemyMovementTest");

        // get current hydra position 
        Position currentPosition = getPositionEntity("hydra", controller);

        controller.tick(Direction.UP);
        assertTrue(
                    (getPositionEntity("hydra", controller).getX() == currentPosition.getX() + 1 && getPositionEntity("hydra", controller).getY() == currentPosition.getY() || 
                    getPositionEntity("hydra", controller).getX() == currentPosition.getX() - 1 && getPositionEntity("hydra", controller).getY() == currentPosition.getY() ||
                    getPositionEntity("hydra", controller).getX() == currentPosition.getX() && getPositionEntity("hydra", controller).getY() == currentPosition.getY() + 1 ||
                    getPositionEntity("hydra", controller).getX() == currentPosition.getX() && getPositionEntity("hydra", controller).getY() == currentPosition.getY() - 1 ||
                    currentPosition.equals(getPositionEntity("hydra", controller)))
                );
    }

    @Test
    @DisplayName("Test hydras move randomly when next to a wall")
    public void testHydraMovementNextToWall() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("enemyMovementTestHydraAdjacentWall", "enemyMovementTest");

        // get current hydra position 
        Position currentPosition = getPositionEntity("hydra", controller);

        controller.tick(Direction.UP);
        assertTrue(
            (getPositionEntity("hydra", controller).getX() == currentPosition.getX() + 1 && getPositionEntity("hydra", controller).getY() == currentPosition.getY() ||
            getPositionEntity("hydra", controller).getX() == currentPosition.getX() && getPositionEntity("hydra", controller).getY() == currentPosition.getY() + 1 ||
            getPositionEntity("hydra", controller).getX() == currentPosition.getX() && getPositionEntity("hydra", controller).getY() == currentPosition.getY() - 1 ||
            getPositionEntity("hydra", controller).getX() == currentPosition.getX() && getPositionEntity("hydra", controller).getY() == currentPosition.getY())
        );
    }

    // Assassin movement
    @Test
    @DisplayName("Test assassins move towards player when mercenary is in line with player")
    public void testAssassinMovementInLine() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("enemyMovementTestAssassinInLine", "enemyMovementTest");

        // get current assassin position 
        Position position = getPositionEntity("assassin", controller);

        for (int i = 1; i < 4; i++) {
            controller.tick(Direction.LEFT);
            assertTrue(getPositionEntity("assassin", controller).getX() == position.getX() - i &&
            getPositionEntity("assassin", controller).getY() == position.getY());
        }
    }

    @Test
    @DisplayName("Test assassins can no longer follow the player if it cannot move any closer to the player")
    public void testAssassinMovementStopsNoLongerCloseToPlayer() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("enemyMovementTestAssassinUnableToFollow", "enemyMovementTest");

        // get current assassin position 
        Position position = getPositionEntity("assassin", controller);


        controller.tick(Direction.UP);
        position.equals(getPositionEntity("assassin", controller));
        controller.tick(Direction.UP);
        position.equals(getPositionEntity("assassin", controller));
        controller.tick(Direction.RIGHT);
        position.equals(getPositionEntity("assassin", controller));
        controller.tick(Direction.RIGHT);
        position.equals(getPositionEntity("assassin", controller));
        controller.tick(Direction.RIGHT);
        position.equals(getPositionEntity("assassin", controller));
        controller.tick(Direction.RIGHT);
        position.equals(getPositionEntity("assassin", controller));
        controller.tick(Direction.DOWN);
        position.equals(getPositionEntity("assassin", controller));
        controller.tick(Direction.DOWN);
        position.equals(getPositionEntity("assassin", controller));
        controller.tick(Direction.DOWN);
        position.equals(getPositionEntity("assassin", controller));
        controller.tick(Direction.DOWN);
        position.equals(getPositionEntity("assassin", controller));
        controller.tick(Direction.LEFT);
        position.equals(getPositionEntity("assassin", controller));
        controller.tick(Direction.LEFT);
        position.equals(getPositionEntity("assassin", controller));
        controller.tick(Direction.LEFT);
        position.equals(getPositionEntity("assassin", controller));
        controller.tick(Direction.LEFT);
        position.equals(getPositionEntity("assassin", controller));
        controller.tick(Direction.UP);
        position.equals(getPositionEntity("assassin", controller));
        controller.tick(Direction.UP);
        position.equals(getPositionEntity("assassin", controller));
    }

    @Test
    @DisplayName("Test assassins can't pass through doors")
    public void testAssassinMovementNoPassDoor() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("enemyMovementTestAssassinNoPassDoor", "enemyMovementTest");

        // get current assassin position 
        Position position = getPositionEntity("assassin", controller);

        controller.tick(Direction.RIGHT);
        assertTrue(getPositionEntity("assassin", controller).getX() == position.getX() - 1 &&
            getPositionEntity("assassin", controller).getY() == position.getY());
        
        controller.tick(Direction.RIGHT);
        assertTrue(getPositionEntity("assassin", controller).getX() == position.getX() - 1 &&
            getPositionEntity("assassin", controller).getY() == position.getY() - 1);
    }

    @Test
    @DisplayName("Test assassins move around walls")
    public void testAssassinMovementAroundWalls() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("enemyMovementTestAssassinMoveAroundWall", "enemyMovementTest");

        controller.tick(Direction.RIGHT);
        assertTrue(getPositionEntity("assassin", controller).getX() == 4 &&
            getPositionEntity("assassin", controller).getY() == 3);
        
        controller.tick(Direction.RIGHT);
        assertTrue(getPositionEntity("assassin", controller).getX() == 4 &&
            getPositionEntity("assassin", controller).getY() == 4);

        controller.tick(Direction.RIGHT);
        assertTrue(getPositionEntity("assassin", controller).getX() == 3 &&
            getPositionEntity("assassin", controller).getY() == 4);

        controller.tick(Direction.RIGHT);
        assertTrue(getPositionEntity("assassin", controller).getX() == 2 &&
            getPositionEntity("assassin", controller).getY() == 4);

        controller.tick(Direction.RIGHT);
        assertTrue(getPositionEntity("assassin", controller).getX() == 2 &&
            getPositionEntity("assassin", controller).getY() == 3);
    }

    @Test
    @DisplayName("Test assassins move towards player when player is invisible and is in recon radius")
    public void testAssassinMovementTowardsPlayerInvisibleRecon() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("enemyMovementTestAssassinReconInvisible", "enemyMovementTest");

        Position currentPosition = getPositionEntity("assassin", controller);

        controller.tick(Direction.RIGHT);

        DungeonResponse dungeonResponse = controller.getDungeonResponseModel();
        String itemId = dungeonResponse.getInventory().get(0).getId();
        controller.tick(itemId);

        assertTrue(
            (getPositionEntity("assassin", controller).getX() == currentPosition.getX() - 2 && getPositionEntity("assassin", controller).getY() == currentPosition.getY())
        );
    }

    @Test
    @DisplayName("Test assassins run away when player is invincible")
    public void testAssassinMovementRunAwayInvincible() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("enemyMovementTestAssassinRunAwayInvincible", "enemyMovementTest");

        Position currentPosition = getPositionEntity("assassin", controller);

        controller.tick(Direction.RIGHT);
        DungeonResponse dungeonResponse = controller.getDungeonResponseModel();
        String itemId = dungeonResponse.getInventory().get(0).getId();
        controller.tick(itemId);

        assertTrue(
            (getPositionEntity("assassin", controller).getX() == currentPosition.getX() && 
            getPositionEntity("assassin", controller).getY() == currentPosition.getY())
        );
    }

    
}
