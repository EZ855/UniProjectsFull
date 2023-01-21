package dungeonmania.components;

import dungeonmania.Component;
import dungeonmania.Config;

public class ItemStat extends Component{
    private double attack;
    private double defense;
    private int durability;
    private int potion_duration;

    public ItemStat(String type, Config config) {
        if (type.equals("shield")) {
            this.defense = config.shield_defence;
            this.durability = config.shield_durability;
        }
        if (type.equals("sword")) {
            this.attack = config.sword_attack;
            this.durability = config.sword_durability;
        }
        if (type.equals("bow")) {
            this.durability = config.bow_durability;
        }
        if (type.equals("midnight_armour")) {
            this.defense = config.midnight_armour_defence;
            this.attack = config.midnight_armour_attack;
        }
        if (type.equals("invisibility_potion")) {
            this.potion_duration = config.invisibility_potion_duration;
        }
        if (type.equals("invincibility_potion")) {
            this.potion_duration = config.invincibility_potion_duration;
        }              
    }
    
    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }


    public double getDefense() {
        return defense;
    }

    public void setDefense(double defense) {
        this.defense = defense;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public int getPotion_duration() {
        return potion_duration;
    }

    public void setPotion_duration(int potion_duration) {
        this.potion_duration = potion_duration;
    }
}

