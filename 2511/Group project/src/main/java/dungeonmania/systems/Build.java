package dungeonmania.systems;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Factory;
import dungeonmania.Inventory;

public class Build {
    Inventory inventory;
    Dungeon dungeon;

    public Build(Dungeon dungeon) {
        this.dungeon = dungeon;
        this.inventory = dungeon.getInventory();
    }

    public List<String> update() {
        List<String> out = new ArrayList<>();
        if (inventory.countItem("wood") >= 1 && inventory.countItem("arrow") >= 3) {
            out.add("bow");
        }
        if (inventory.countItem("wood") >= 2 && ((inventory.countItem("treasure") >= 1) || (inventory.countItem("key") >= 1) || (inventory.countItem("sun_stone") >= 1))) {
            out.add("shield");
        }
        if (((inventory.countItem("wood") >= 1) || (inventory.countItem("arrow") >= 2)) && ((inventory.countItem("treasure") >= 1) || (inventory.countItem("key") >= 1)) && (inventory.countItem("sun_stone") >= 1)) {
            out.add("sceptre");
        }
        if (inventory.countItem("sword") >= 1 && inventory.countItem("sun_stone") >= 1) {
            out.add("midnight_armour");
        }
        return out;
    }

    public void build(String item) {
        Factory factory = new Factory(dungeon.getConfig());
        if (item.equals("bow")) {
            inventory.removeItemCount("wood", 1);
            // +
            inventory.removeItemCount("arrow", 3);
            // =
            inventory.addItem(factory.createEntity(item));
        }
        if (item.equals("shield")) {
            inventory.removeItemCount("wood", 2);
            // +
            if (inventory.countItem("sun_stone") >= 1) {

            }
            else if (inventory.countItem("treasure") >= 1) {
                inventory.removeItemCount("treasure", 1);
            }
            else if (inventory.countItem("key") >= 1) {
                inventory.removeItemCount("key", 1);
            }
            // =
            inventory.addItem(factory.createEntity(item));
        }
        if (item.equals("sceptre")) {
            if (inventory.countItem("wood") >= 1) {
                inventory.removeItemCount("wood", 1);
            }
            else if (inventory.countItem("arrow") >= 2) {
                inventory.removeItemCount("arrow", 2);
            }
            // +
            if (inventory.countItem("sun_stone") >= 2) {

            }
            else if (inventory.countItem("treasure") >= 1) {
                inventory.removeItemCount("treasure", 1);
            }
            else if (inventory.countItem("key") >= 1) {
                inventory.removeItemCount("key", 1);
            }
            // +
            if (inventory.countItem("sun_stone") >= 1) {
                inventory.removeItemCount("sun_stone", 1);;
            }
            // =
            inventory.addItem(factory.createEntity(item));
        }
        if (item.equals("midnight_armour")) {
            if (inventory.countItem("sword") >= 1) {
                inventory.removeItemCount("sword", 1);
            }
            // +
            if (inventory.countItem("sun_stone") >= 1) {
                inventory.removeItemCount("sword", 1);
            }
            // =
            inventory.addItem(factory.createEntity(item));
        }
    }
}
