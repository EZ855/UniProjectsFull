package dungeonmania;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dungeonmania.response.models.ItemResponse;

public class Inventory {
    List<Entity> inventory;

    public Inventory() {
        inventory = new ArrayList<>();
    }

    public List<Entity> getInventory() {
        return inventory;
    }

    public void addItem(Entity entity) {
        inventory.add(entity);
    }

    public void removeItem(Entity entity) {
        if (inventory.contains(entity)) {
            inventory.remove(entity);
        }
    }

    public int countItem(String type) {
        int count = 0;
        for (Entity item : inventory) {
            if (item.getType().equals(type)) {
                count++;
            }
        }
        return count;
    }

    public void removeItemCount(String type, int count) {
        Iterator<Entity> itr = inventory.listIterator();
        int i = 0;
        while (itr.hasNext() && i < count) {
            Entity check = itr.next();
            if (check.getType().equals(type)) {
                itr.remove();
                i++;
            }
        }
    }

    public Entity containsType(String string) {
        for (Entity item : inventory) {
            if (item.getType().equals(string)) {
                return item;
            }
        }
        return null;
    }

    public ArrayList<ItemResponse> getInventoryResponse () {
        ArrayList<ItemResponse> output = new ArrayList<>();
        for (Entity entity : inventory) {
            output.add(new ItemResponse(entity.getId(), entity.getType()));
        }
        return output;
    }

}
