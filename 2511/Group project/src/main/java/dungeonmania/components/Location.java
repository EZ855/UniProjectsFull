package dungeonmania.components;

import dungeonmania.Component;
import dungeonmania.util.Position;

public class Location extends Component{
    private int x;
    private int y;
    private int z = 0;
    private int previousX;
    private int previousY;

    public Location(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position getPosition() {
        return new Position(x, y);
    }

    public void setPosition(Position position) {
        x = position.getX();
        y = position.getY();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getPreviousX() {
        return previousX;
    }

    public int getPreviousY() {
        return previousY;
    }

    public void move(Position position) {
        previousX = x;
        previousY = y;
        x = (position.getX());
        y = (position.getY());
    }

    
}
