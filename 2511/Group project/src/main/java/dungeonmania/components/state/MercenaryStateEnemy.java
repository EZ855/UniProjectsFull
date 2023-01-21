package dungeonmania.components.state;

import dungeonmania.Entity;
import dungeonmania.components.Stats;

public class MercenaryStateEnemy extends MercenaryState{


    public MercenaryStateEnemy(Entity mercenary) {
        super(mercenary);
    }

    // Sets current state to MercenaryStateAlly and isBribed to true
    @Override
    public boolean bribeTreasure() {
        getMercenary().removeComponent(MercenaryState.class);
        getMercenary().addComponent(new MercenaryStateAlly(getMercenary()));
        getMercenary().getComponent(Stats.class).setBribed(true);
        return true;
    }

    // Sets current state to MercenaryStateAlly and isBribed to true
    @Override
    public void bribeSceptre(int ticks) {
        getMercenary().removeComponent(MercenaryState.class);
        getMercenary().addComponent(new MercenaryStateAlly(getMercenary()));
        getMercenary().getComponent(Stats.class).setBribed(true);
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
