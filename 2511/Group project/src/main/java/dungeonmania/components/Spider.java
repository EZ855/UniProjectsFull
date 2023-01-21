package dungeonmania.components;

import dungeonmania.Component;
import dungeonmania.util.Position;

public class Spider extends Component {
    private int direction = 1;
    private int arrayLocation = -1;
    private Position spawn;

    public Spider(Position pos) {
        spawn = pos;
    }

    public Position getSpawn() {
        return spawn;
    }
    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getArrayLocation() {
        return arrayLocation;
    }

    public void setArrayLocation(int arrayLocation) {
        this.arrayLocation = arrayLocation;
    }

    public int prediction() {
        int output = arrayLocation;
        if (arrayLocation == -1) {
            if (direction == 1) {
                return 1;
            }
            else if (direction == -1) {
                return 5;
            }
            else {
                return -1;
            }
        }
        output += direction;
        if (output == -1) {
            output = 7;
        }
        if (output == 8) {
            output = 0;
        }
        return output;

    }


}
