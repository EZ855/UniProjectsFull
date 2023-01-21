package dungeonmania.systems;

import java.util.Iterator;

import dungeonmania.Battle;
import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Helper;
import dungeonmania.Round;
import dungeonmania.components.ItemStat;
import dungeonmania.components.Location;
import dungeonmania.components.Stats;
import dungeonmania.components.state.MercenaryState;

public class BattleSystem {
    private Dungeon dungeon;

    public BattleSystem(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public void initiateBattle() {
        Entity player  = dungeon.getPlayer();

        Iterator<Entity> iterableEntity = dungeon.getEntities().iterator();
        while (iterableEntity.hasNext()) {
            Entity m = iterableEntity.next();
            if (!((m.getType().equals("mercenary") || (m.getType().equals("assassin"))) && (m.getComponent(Stats.class).isBribed()))) {
                if (Helper.sharePos(player,m) && m.getTags().contains("Enemy") && !(player.getComponent(Stats.class).getPlayerState().equals("invisibility_potion"))) {
                    Battle battle = new Battle(player, m, dungeon);
                    Round round = new Round(player, m, dungeon);
                    
                    if (player.getComponent(Stats.class).getPlayerState().equals("invincibility_potion")) {
                        round.setEnemyDelta(battle.getEnemyHealth() * -1.0);
                        round.setPlayerDelta(0.0);
                        round.setEnemyHealth(0.0);
                        round.setPlayerHealth(battle.getPlayerHealth());
                    } else {
                        double playerDelta = round.calculatePlayerDelta();
                        double enemyDelta = round.calculateEnemyDelta();
                        round.setEnemyDelta(enemyDelta);
                        round.setPlayerDelta(playerDelta);
                    }
                    battle.addRound(round);

                    boolean isBattleOver = false;
                    if (round.getEnemyHealth() <= 0.0 || round.getPlayerHealth() <= 0.0) {
                        isBattleOver = true;
                    }
                    while (!isBattleOver) {
                        round = new Round(player, m, dungeon);
                        double playerDelta = round.calculatePlayerDelta();
                        double enemyDelta = round.calculateEnemyDelta();
                        round.setEnemyDelta(enemyDelta);
                        round.setPlayerDelta(playerDelta);
                        battle.addRound(round);
                        if (round.getEnemyHealth() <= 0.0 || round.getPlayerHealth() <= 0.0) {
                            isBattleOver = true;
                        }
                
                    }

                    Iterator<Entity> iterableItem = dungeon.getInventory().getInventory().iterator();
                    while (iterableItem.hasNext()) {
                        Entity item = iterableItem.next();
                        if (item.getType().equals("sword")) {
                            Entity sword = item;
                            sword.getComponent(ItemStat.class).setDurability(sword.getComponent(ItemStat.class).getDurability() - 1);
                            if (sword.getComponent(ItemStat.class).getDurability() == 0) {
                                iterableItem.remove();
                            }
                        } else if (item.getType().equals("bow")) {
                            Entity bow = item;
                            bow.getComponent(ItemStat.class).setDurability(bow.getComponent(ItemStat.class).getDurability() - 1);
                            if (bow.getComponent(ItemStat.class).getDurability() == 0) {
                                iterableItem.remove();
                            }
                        } else if (item.getType().equals("shield")) {
                            Entity shield = item;
                            shield.getComponent(ItemStat.class).setDurability(shield.getComponent(ItemStat.class).getDurability() - 1);
                            if (shield.getComponent(ItemStat.class).getDurability() == 0) {
                                iterableItem.remove();
                            }
                        }
                    }
                    
                    if (round.getEnemyHealth() <= 0.0 && round.getPlayerHealth() > 0.0) {
                        iterableEntity.remove();
                        dungeon.getBattles().add(battle);
                    } else if (round.getEnemyHealth() <= 0.0 && round.getPlayerHealth() <= 0.0) {
                        iterableEntity.remove();
                        Iterator<Entity> iterableEntityElement = dungeon.getEntities().iterator();
                        while (iterableEntityElement.hasNext()) {
                            Entity element = iterableEntityElement.next();
                            if (element.getType().equals("player")) {
                                iterableEntityElement.remove();
                                dungeon.setPlayer(null);
                                break;
                            }
                        }
                        dungeon.getBattles().add(battle);
                        break;
                    } else if (round.getEnemyHealth() > 0.0 && round.getPlayerHealth() <= 0.0) {
                        Iterator<Entity> iterableEntityElement = dungeon.getEntities().iterator();
                        while (iterableEntityElement.hasNext()) {
                            Entity element = iterableEntityElement.next();
                            if (element.getType().equals("player")) {
                                iterableEntityElement.remove();
                                dungeon.setPlayer(null);
                                break;
                            }
                        }
                        dungeon.getBattles().add(battle);
                        break;
                    }
                }
            }
        }
    }

    public boolean isEnemy(Entity e) {
        if (e.getType().equals("spider") || 
        e.getType().equals("zombie_toast") || 
        e.getType().equals("mercenary") || 
        e.getType().equals("hydra") ||
        e.getType().equals("assassin")) {
            if (e.getType().equals("mercenary") && e.getComponent(Stats.class).isBribed()) {
                return false;
            }
            if (e.getType().equals("assassin") && e.getComponent(Stats.class).isBribed()) {
                return false;
            }
            return true;
        }
        return false;
    }

    public void updateCurrentPotionEffects() {
        Entity player = dungeon.getPlayer();
        player.getComponent(Stats.class).setStatusDuration(player.getComponent(Stats.class).getStatusDuration() - 1);
        if (player.getComponent(Stats.class).getStatusDuration() == 0) {
            if (player.getComponent(Stats.class).getStatusQueue().size() != 0) {
                player.getComponent(Stats.class).setPlayerState(player.getComponent(Stats.class).getStatusQueue().get(0).getType());
                if (player.getComponent(Stats.class).getStatusQueue().get(0).getType().equals("invincibility_potion")) {
                    Entity p = player.getComponent(Stats.class).getStatusQueue().get(0);
                    player.getComponent(Stats.class).setPotionInUse(player.getComponent(Stats.class).getStatusQueue().get(0));
                    player.getComponent(Stats.class).setStatusDuration(p.getComponent(ItemStat.class).getPotion_duration());
                    player.getComponent(Stats.class).setPlayerState("invincibility_potion");
                } else if (player.getComponent(Stats.class).getStatusQueue().get(0).getType().equals("invisibility_potion")) {
                    Entity p = player.getComponent(Stats.class).getStatusQueue().get(0);
                    player.getComponent(Stats.class).setPotionInUse(player.getComponent(Stats.class).getStatusQueue().get(0));
                    player.getComponent(Stats.class).setStatusDuration(p.getComponent(ItemStat.class).getPotion_duration());
                    player.getComponent(Stats.class).setPlayerState("invisibility_potion");
                }
                player.getComponent(Stats.class).getStatusQueue().remove(player.getComponent(Stats.class).getStatusQueue().get(0));
            } else {
                player.getComponent(Stats.class).setPlayerState("vulnerable");
            }
        }
    }

}
