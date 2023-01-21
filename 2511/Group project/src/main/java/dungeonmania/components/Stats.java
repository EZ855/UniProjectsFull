package dungeonmania.components;

import java.util.ArrayList;

import dungeonmania.Component;
import dungeonmania.Config;
import dungeonmania.Entity;

public class Stats extends Component{
    private double health;
    private double attackDamage;
    private String playerState;
    private int statusDuration;
    private Entity potionInUse;
    private int bribe_amount;
    private int bribe_radius;
    private int recon_radius;
    private boolean isBribed;
    private double health_increase_rate;
    private double health_increase_amount;
    private double bribe_fail_rate;
    private ArrayList<Entity> statusQueue = new ArrayList<Entity>();

    public Stats(String type, Config config) {
        if (type.equals("player")) {
            this.health = config.player_health;
            this.attackDamage = config.player_attack;
            this.statusDuration = 0;
            this.playerState = "vulnerable";
        }
        if (type.equals("spider")) {
            this.health = config.spider_health;
            this.attackDamage = config.spider_attack;
            this.playerState = "";
        }
        if (type.equals("zombie_toast")) {
            this.health = config.zombie_health;
            this.attackDamage = config.zombie_attack;
            this.playerState = "";
        }
        if (type.equals("mercenary")) {
            this.health = config.mercenary_health;
            this.attackDamage = config.mercenary_attack;
            this.bribe_amount = config.bribe_amount;
            this.bribe_radius = config.bribe_radius;
            this.isBribed = false;
            this.playerState = "";
        }
        if (type.equals("hydra")) {
            this.health = config.hydra_health;
            this.attackDamage = config.hydra_attack;
            this.health_increase_rate = config.hydra_health_increase_rate;
            this.health_increase_amount = config.hydra_health_increase_amount;
            this.playerState = "";
        }
        if (type.equals("assassin")) {
            this.health = config.assassin_health;
            this.attackDamage = config.assassin_attack;
            this.bribe_amount = config.assassin_bribe_amount;
            this.bribe_radius = config.bribe_radius;
            this.recon_radius = config.assassin_recon_radius;
            this.isBribed = false;
            this.bribe_fail_rate = config.assassin_bribe_fail_rate;
            this.playerState = "";
        }
        
    }

    public double getHealth() {
        return health;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public String getPlayerState() {
        return playerState;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setPlayerState(String playerState) {
        this.playerState = playerState;
    }

    public int getStatusDuration() {
        return statusDuration;
    }

    public void setStatusDuration(int statusDuration) {
        this.statusDuration = statusDuration;
    }

    public ArrayList<Entity> getStatusQueue() {
        return statusQueue;
    }

    public void setPotionInUse(Entity potionInUse) {
        this.potionInUse = potionInUse;
    }

    public Entity getPotionInUse() {
        return potionInUse;
    }

    public void setAttackDamage(double attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getBribe_amount() {
        return bribe_amount;
    }

    public void setBribe_amount(int bribe_amount) {
        this.bribe_amount = bribe_amount;
    }

    public int getBribe_radius() {
        return bribe_radius;
    }

    public void setBribe_radius(int bribe_radius) {
        this.bribe_radius = bribe_radius;
    }

    public int getRecon_radius() {
        return recon_radius;
    }

    public void setRecon_radius(int recon_radius) {
        this.recon_radius = recon_radius;
    }

    public boolean isBribed() {
        return isBribed;
    }

    public void setBribed(boolean isBribed) {
        this.isBribed = isBribed;
    }

    public double getHealth_increase_rate() {
        return health_increase_rate;
    }

    public void setHealth_increase_rate(double health_increase_rate) {
        this.health_increase_rate = health_increase_rate;
    }

    public double getHealth_increase_amount() {
        return health_increase_amount;
    }

    public void setHealth_increase_amount(double health_increase_amount) {
        this.health_increase_amount = health_increase_amount;
    }

    public double getBribe_fail_rate() {
        return bribe_fail_rate;
    }

    public void setBribe_fail_rate(double bribe_fail_rate) {
        this.bribe_fail_rate = bribe_fail_rate;
    }

    public void setStatusQueue(ArrayList<Entity> statusQueue) {
        this.statusQueue = statusQueue;
    }
}
