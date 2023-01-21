package dungeonmania.components.state;

import dungeonmania.Entity;
import dungeonmania.components.Stats;

public class MercenaryStateAlly extends MercenaryState{


    public MercenaryStateAlly(Entity mercenary) {
        super(mercenary);
    }

    // Nothing happens if already allied and tries to bribe with treasure
    @Override
    public boolean bribeTreasure() {
        return false;
    }

    // Reset ticks
    @Override
    public void bribeSceptre(int ticks) {
        setTicks(ticks);
    }

    // Move mercenary according to ally movement pattern and increment ticks. Needs refactoring along with MoveSystem.java but cbs at this point
    @Override
    public void move() {
        setTicks(getTicks() - 1);
        // TODO move mercenary/assassin
        if (getTicks() == 0) {
            getMercenary().removeComponent(MercenaryState.class);
            getMercenary().addComponent(new MercenaryStateEnemy(getMercenary()));
            getMercenary().getComponent(Stats.class).setBribed(false);
        }
    }
    
}
