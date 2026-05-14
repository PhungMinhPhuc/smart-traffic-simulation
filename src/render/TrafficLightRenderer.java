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
        return render(light, light.getPosition().getX(), light.getPosition().getY());
    }

    public Group render(TrafficLight light, double x, double y) {
        Group group = new Group();

        double w = Constants.HOUSING_WIDTH;
        double h = Constants.HOUSING_HEIGHT;

        // Background housing: starts at x and extends backwards by w
        Rectangle housing = new Rectangle(x - w, y, w, h);
        housing.setFill(Color.BLACK);

        // Lights: centered horizontally in the housing
        double centerX = x - w / 2;
        Circle redLight = createLight(centerX, y + 1 * h / 4 - Constants.SPACING / 2, Color.RED,
                light.getCurrentState() == LightState.RED);
        Circle yellowLight = createLight(centerX, y + 2 * h / 4 - Constants.SPACING / 2, Color.YELLOW,
                light.getCurrentState() == LightState.YELLOW);
        Circle greenLight = createLight(centerX, y + 3 * h / 4 - Constants.SPACING / 2, Color.GREEN,
                light.getCurrentState() == LightState.GREEN);

        group.getChildren().addAll(housing, redLight, yellowLight, greenLight);

        // Timer text
        String timerTextValue = light.getDisplayText();
        if (!timerTextValue.isEmpty()) {
            Text timerText = new Text(timerTextValue);
            try {
                // Load font from the classpath root (src/main/resources)
                InputStream is = getClass().getResourceAsStream("/assets/fonts/Seven Segment.ttf");
                if (is != null) {
                    Font segmentFont = Font.loadFont(is, 13f);
                    timerText.setFont(segmentFont);
                } else {
                    System.err.println("Font file not found: /assets/fonts/Seven Segment.ttf");
                    timerText.setFont(new Font(13));
                }
            } catch (Exception e) {
                timerText.setFont(new Font(13));
                e.printStackTrace();
            }
            timerText.setFill(light.getCurrentState().getColor());

            // Center horizontally within the wrapping width
            timerText.setWrappingWidth(w);
            timerText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

            // Set vertical origin to center for better positioning
            timerText.setTextOrigin(VPos.CENTER);

            // Position centered in the housing
            timerText.setX(x - w);
            timerText.setY(y + 4 * h / 4 - Constants.SPACING / 2);
            timerText.setRotate(90);

            group.getChildren().add(timerText);
        }

        // Apply rotation around the stop point (x, y)
        group.getTransforms().add(new Rotate(light.getRotation(), x, y));

        return group;
    }

    private Circle createLight(double x, double y, Color color, boolean isActive) {
        Circle circle = new Circle(x, y, Constants.LIGHT_RADIUS);
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
        return circle;
    }
}
