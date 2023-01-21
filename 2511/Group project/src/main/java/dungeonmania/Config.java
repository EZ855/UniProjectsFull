package dungeonmania;

import com.google.gson.JsonObject;

public class Config {
    public double ally_attack;
    public double ally_defence;
    public int bomb_radius;
    public int bow_durability;
    public int bribe_amount;
    public int bribe_radius;
    public int enemy_goal;
    public int invincibility_potion_duration;
    public int invisibility_potion_duration;
    public double mercenary_attack;
    public double mercenary_health;
    public double player_attack;
    public double player_health;
    public double shield_defence;
    public int shield_durability;
    public double spider_attack;
    public double spider_health;
    public int spider_spawn_rate;
    public double sword_attack;
    public int sword_durability;
    public int treasure_goal;
    public double zombie_attack;
    public double zombie_health;
    public int zombie_spawn_rate;
    public double assassin_attack;
    public int assassin_bribe_amount;
    public double assassin_bribe_fail_rate;
    public double assassin_health;
    public int assassin_recon_radius;
    public double hydra_attack;
    public double hydra_health;
    public double hydra_health_increase_rate;
    public double hydra_health_increase_amount;
    public int mind_control_duration;
    public double midnight_armour_attack;
    public double midnight_armour_defence;

    public Config(JsonObject config) {
        ally_attack = config.get("ally_attack").getAsDouble();
        ally_defence = config.get("ally_defence").getAsDouble();
        bomb_radius = config.get("bomb_radius").getAsInt();
        bow_durability = config.get("bow_durability").getAsInt();
        bribe_amount = config.get("bribe_amount").getAsInt();
        bribe_radius = config.get("bribe_radius").getAsInt();
        enemy_goal = config.get("enemy_goal").getAsInt();
        invincibility_potion_duration = config.get("invincibility_potion_duration").getAsInt();
        invisibility_potion_duration = config.get("invisibility_potion_duration").getAsInt();
        mercenary_attack = config.get("mercenary_attack").getAsDouble();
        mercenary_health = config.get("mercenary_health").getAsDouble();
        player_attack = config.get("player_attack").getAsDouble();
        player_health = config.get("player_health").getAsDouble();
        shield_defence = config.get("shield_defence").getAsDouble();
        shield_durability = config.get("shield_durability").getAsInt();
        spider_attack = config.get("spider_attack").getAsDouble();
        spider_health = config.get("spider_health").getAsDouble();
        spider_spawn_rate = config.get("spider_spawn_rate").getAsInt();
        sword_attack = config.get("sword_attack").getAsDouble();
        sword_durability = config.get("sword_durability").getAsInt();
        treasure_goal = config.get("treasure_goal").getAsInt();
        zombie_attack = config.get("zombie_attack").getAsDouble();
        zombie_health = config.get("zombie_health").getAsDouble();
        zombie_spawn_rate = config.get("zombie_spawn_rate").getAsInt();
        
        // backwards compatible with milestone 2
        if (hasField("assassin_attack", config)) {
            assassin_attack = config.get("assassin_attack").getAsDouble();
        }
        if (hasField("assassin_bribe_amount", config)) {
            assassin_bribe_amount = config.get("assassin_bribe_amount").getAsInt();
        }
        if (hasField("assassin_bribe_fail_rate", config)) {
            assassin_bribe_fail_rate = config.get("assassin_bribe_fail_rate").getAsDouble();
        }
        if (hasField("assassin_health", config)) {
            assassin_health = config.get("assassin_health").getAsDouble();
        }
        if (hasField("assassin_recon_radius", config)) {
            assassin_recon_radius = config.get("assassin_recon_radius").getAsInt();
        }
        if (hasField("hydra_attack", config)) {
            hydra_attack = config.get("hydra_attack").getAsDouble();
        }
        if (hasField("hydra_health", config)) {
            hydra_health = config.get("hydra_health").getAsDouble();
        }
        if (hasField("hydra_health_increase_rate", config)) {
            hydra_health_increase_rate = config.get("hydra_health_increase_rate").getAsDouble();
        }
        if (hasField("hydra_health_increase_amount", config)) {
            hydra_health_increase_amount = config.get("hydra_health_increase_amount").getAsDouble();
        }
        if (hasField("mind_control_duration", config)) {
            mind_control_duration = config.get("mind_control_duration").getAsInt();
        }
        if (hasField("midnight_armour_attack", config)) {
            midnight_armour_attack = config.get("midnight_armour_attack").getAsDouble();
        }
        if (hasField("midnight_armour_defence", config)) {
            midnight_armour_defence = config.get("midnight_armour_defence").getAsDouble();
        }
    }
    
    public boolean hasField(String field, JsonObject config) {
        if (config.has(field)) {
            return true;
        }
        return false;
    }
}
