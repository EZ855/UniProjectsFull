package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dungeonmania.components.Location;
import dungeonmania.components.Stats;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class Entity {

    private String type;
    private String id;
    private List<Component> components = new ArrayList<>();
    private List<String> tags = new ArrayList<>();

    public Entity(String type) {
        this.type = type;
        this.id = UUID.randomUUID().toString();
    }

    public String getType() {
        return type;
    }

    public List<String> getTags() {
        return tags;
    }

    public void addTags(String... tags) {
        for (String tag : tags) {
            this.tags.add(tag);
        }
    }

    public void removeTag(String tag) {
        if (tags.contains(tag)) {
            tags.remove(tag);
        }
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error Casting Component";
                }
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i=0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c) {
        this.components.add(c);
        c.entity = this;
    }

    public EntityResponse getEntityInfoResponse() {
        EntityResponse response = new EntityResponse(id, type, new Position(getComponent(Location.class).getX(),getComponent(Location.class).getY()), isInteractable());
        return response;
    }

    public boolean isInteractable() {
        if (type.equals("mercenary") && !(getComponent(Stats.class).isBribed())) {
            return true;
        } else if (type.equals("assassin") && !(getComponent(Stats.class).isBribed())) {
            return true;
        }
        return false;
    }

}
