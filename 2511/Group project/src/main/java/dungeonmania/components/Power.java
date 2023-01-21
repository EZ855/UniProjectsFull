package dungeonmania.components;

import dungeonmania.Component;

public class Power extends Component{
    private Boolean active = false;

    public Power() {

    }

    public Boolean state() {
        return active;
    }

    public void setState(Boolean b) {
        active = b;
    }
}
