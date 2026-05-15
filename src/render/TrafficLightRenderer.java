package render;

import config.Constants;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import model.traffic.LightState;
import model.traffic.TrafficLight;

import java.io.InputStream;

public class TrafficLightRenderer implements IRender<TrafficLight> {
    private static final Font SEGMENT_FONT = loadSegmentFont();

    private static Font loadSegmentFont() {
        try (InputStream is = TrafficLightRenderer.class.getResourceAsStream("/assets/fonts/Seven Segment.ttf")) {
            if (is != null) {
                return Font.loadFont(is, 13f);
            }
        } catch (Exception ignored) {
        }
        return new Font(13);
    }

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

        Rectangle housing = new Rectangle(x - w, y, w, h);
        housing.setFill(Color.BLACK);

        double centerX = x - w / 2;
        Circle redLight = new Circle(centerX, y + 1 * h / 4 - Constants.SPACING / 2, Constants.LIGHT_RADIUS);
        Circle yellowLight = new Circle(centerX, y + 2 * h / 4 - Constants.SPACING / 2, Constants.LIGHT_RADIUS);
        Circle greenLight = new Circle(centerX, y + 3 * h / 4 - Constants.SPACING / 2, Constants.LIGHT_RADIUS);

        Text timerText = new Text();
        timerText.setFont(SEGMENT_FONT);
        timerText.setWrappingWidth(w);
        timerText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        timerText.setTextOrigin(VPos.CENTER);
        timerText.setX(x - w);
        timerText.setY(y + 4 * h / 4 - Constants.SPACING / 2);
        timerText.setRotate(90);

        group.getChildren().addAll(housing, redLight, yellowLight, greenLight, timerText);
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

        redLight.setFill(light.getCurrentState() == LightState.RED
                ? Color.RED
                : Color.RED.deriveColor(0, 1, 0.3, 1));
        yellowLight.setFill(light.getCurrentState() == LightState.YELLOW
                ? Color.YELLOW
                : Color.YELLOW.deriveColor(0, 1, 0.3, 1));
        greenLight.setFill(light.getCurrentState() == LightState.GREEN
                ? Color.GREEN
                : Color.GREEN.deriveColor(0, 1, 0.3, 1));

        String timerTextValue = light.getDisplayText();
        timerText.setText(timerTextValue);
        timerText.setVisible(!timerTextValue.isEmpty());
        timerText.setFill(light.getCurrentState().getColor());
    }
}
