package dungeonmania.systems;

import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.components.ItemStat;
import dungeonmania.components.Location;
import dungeonmania.components.Stats;

public class UseSystem {
    private Dungeon dungeon;
    private Entity entity;

    public UseSystem(Dungeon dungeon, Entity entity) {
        this.dungeon = dungeon;
        this.entity = entity;
    }

    public void Use() {
        if (entity.getTags().contains("Explosive")) {
            useBomb();
        }

        if (entity.getTags().contains("Potion")) {
            usePotion();
        }
        dungeon.getInventory().removeItem(entity);
    }

    public void useBomb() {
        entity.getComponent(Location.class).setPosition(dungeon.getPlayer().getComponent(Location.class).getPosition());
        entity.removeTag("Collectable");
        dungeon.addEntity(entity);
    }

    public void usePotion() {
        Entity player = dungeon.getPlayer();
        if (entity.getType().equals("invincibility_potion")) {
            if (!(player.getComponent(Stats.class).getPlayerState().equals("vulnerable"))) {
                player.getComponent(Stats.class).getStatusQueue().add(entity);
            } else {
                player.getComponent(Stats.class).setPotionInUse(entity);
                player.getComponent(Stats.class).setPlayerState("invincibility_potion");
                player.getComponent(Stats.class).setStatusDuration(entity.getComponent(ItemStat.class).getPotion_duration());
            }
        } else if (entity.getType().equals("invisibility_potion")) {
            if (!(player.getComponent(Stats.class).getPlayerState().equals("vulnerable"))) {
                player.getComponent(Stats.class).getStatusQueue().add(entity);
            } else {
                player.getComponent(Stats.class).setPotionInUse(entity);
                player.getComponent(Stats.class).setPlayerState("invisibility_potion");
                player.getComponent(Stats.class).setStatusDuration(entity.getComponent(ItemStat.class).getPotion_duration());
            }
        }
    }
}
