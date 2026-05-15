package render;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.geometry.VPos;
import java.io.InputStream;

import model.traffic.LightState;
import model.traffic.TrafficLight;
import config.Constants;

public class TrafficLightRenderer implements IRender<TrafficLight> {

    @Override
    public Parent render(TrafficLight light) {
        return createNode(light);
    }

    public Group createNode(TrafficLight light) {
        Group group = new Group();

        double w = Constants.HOUSING_WIDTH;
        double h = Constants.HOUSING_HEIGHT;
        double x = light.getPosition().getX();
        double y = light.getPosition().getY();

        // Background housing: starts at x and extends backwards by w
        Rectangle housing = new Rectangle(x - w, y, w, h);
        housing.setFill(Constants.HOUSING_COLOR);

        // Lights: centered horizontally in the housing
        double centerX = x - w / 2;
        Circle redLight = new Circle(centerX, y + 1 * h / 4 - Constants.SPACING / 2, Constants.LIGHT_RADIUS);
        Circle yellowLight = new Circle(centerX, y + 2 * h / 4 - Constants.SPACING / 2, Constants.LIGHT_RADIUS);
        Circle greenLight = new Circle(centerX, y + 3 * h / 4 - Constants.SPACING / 2, Constants.LIGHT_RADIUS);

        // Timer text
        Text timerText = new Text();
        try {
            // Load font from the classpath root (src/main/resources)
            InputStream is = getClass().getResourceAsStream("/assets/fonts/Seven Segment.ttf");
            if (is != null) {
                Font segmentFont = Font.loadFont(is, 13f);
                timerText.setFont(segmentFont);
            } else {
                timerText.setFont(new Font(13));
            }
        } catch (Exception e) {
            timerText.setFont(new Font(13));
        }

        // Center horizontally within the wrapping width
        timerText.setWrappingWidth(w);
        timerText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Set vertical origin to center for better positioning
        timerText.setTextOrigin(VPos.CENTER);

        // Position centered in the housing
        timerText.setX(x - w);
        timerText.setY(y + 4 * h / 4 - Constants.SPACING / 2);
        timerText.setRotate(90);

        group.getChildren().addAll(housing, redLight, yellowLight, greenLight, timerText);

        // Apply rotation around the stop point (x, y)
        group.getTransforms().add(new Rotate(light.getRotation(), x, y));

        updateNode(group, light);
        return group;
    }

    public void updateNode(Group group, TrafficLight light) {
        Rectangle housing = (Rectangle) group.getChildren().get(0);
        Circle redLight = (Circle) group.getChildren().get(1);
        Circle yellowLight = (Circle) group.getChildren().get(2);
        Circle greenLight = (Circle) group.getChildren().get(3);
        Text timerText = (Text) group.getChildren().get(4);

        double w = Constants.HOUSING_WIDTH;
        double h = Constants.HOUSING_HEIGHT;
        double x = light.getPosition().getX();
        double y = light.getPosition().getY();

        housing.setX(x - w);
        housing.setY(y);

        double centerX = x - w / 2;
        redLight.setCenterX(centerX);
        redLight.setCenterY(y + 1 * h / 4 - Constants.SPACING / 2);
        yellowLight.setCenterX(centerX);
        yellowLight.setCenterY(y + 2 * h / 4 - Constants.SPACING / 2);
        greenLight.setCenterX(centerX);
        greenLight.setCenterY(y + 3 * h / 4 - Constants.SPACING / 2);

        // Update lights with active state and glow effect
        updateLight(redLight, Color.RED, light.getCurrentState() == LightState.RED);
        updateLight(yellowLight, Color.YELLOW, light.getCurrentState() == LightState.YELLOW);
        updateLight(greenLight, Color.GREEN, light.getCurrentState() == LightState.GREEN);

        String timerTextValue = light.getDisplayText();
        timerText.setText(timerTextValue);
        timerText.setVisible(!timerTextValue.isEmpty());
        timerText.setFill(light.getCurrentState().getColor());
    }

    private void updateLight(Circle circle, Color color, boolean isActive) {
        if (isActive) {
            circle.setFill(color);
            // Add a glow effect or just make it bright
            circle.setStroke(Color.WHITE);
            circle.setStrokeWidth(1);
        } else {
            // Dimmed color
            circle.setFill(color.deriveColor(0, 1, 0.3, 1));
            circle.setStroke(Color.TRANSPARENT);
        }
    }
}
