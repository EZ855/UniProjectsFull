package dungeonmania.components.state;

import dungeonmania.Component;
import dungeonmania.Entity;

public abstract class MercenaryState extends Component{
    private Entity mercenary;
    private int ticks = -1;
    
    public MercenaryState(Entity mercenary) {
        this.mercenary = mercenary;
    }

    public abstract void move();
    public abstract void bribeSceptre(int ticks);
    public abstract boolean bribeTreasure();

    public Entity getMercenary() {
        return mercenary;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }
    
}
