package dungeonmania.components;

import dungeonmania.Component;

public class Key extends Component{
    private int key;
    private String keyString;

    public Key(int key) {
        this.key = key;
    }

    public Key(String key) {
        keyString = key;
    }

    public int getKey() {
        return key;
    }

    public String getKeyString() {
        return keyString;
    }
}

