package model.traffic;

import javafx.scene.paint.Color;

public enum LightState {
    RED(Color.RED),
    YELLOW(Color.YELLOW),
    GREEN(Color.LIGHTGREEN);

    private final Color color;

    LightState(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
