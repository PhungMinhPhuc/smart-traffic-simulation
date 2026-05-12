package main.java.handler;

import config.Constants;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;

public class ZoomHandler {
    private double currentZoom = 1.0;

    // UI references
    private ScrollPane trafficMapWrapper;
    private Group trafficMapContainer;

    public void initialize(ScrollPane trafficMapWrapper, Group trafficMapContainer) {
        this.trafficMapWrapper = trafficMapWrapper;
        this.trafficMapContainer = trafficMapContainer;
    }

    public void zoomIn(ActionEvent event) {
        double centerX = trafficMapWrapper.getViewportBounds().getWidth() / 2;
        double centerY = trafficMapWrapper.getViewportBounds().getHeight() / 2;
        zoomToPoint(1 + Constants.ZOOM_STEP, centerX, centerY);
    }

    public void zoomOut(ActionEvent event) {
        double centerX = trafficMapWrapper.getViewportBounds().getWidth() / 2;
        double centerY = trafficMapWrapper.getViewportBounds().getHeight() / 2;
        zoomToPoint(1 / (1 + Constants.ZOOM_STEP), centerX, centerY);
    }

    public void resetZoom(ActionEvent event) {
        currentZoom = 1.0;
        applyZoom();
    }

    private void applyZoom() {
        trafficMapContainer.setScaleX(currentZoom);
        trafficMapContainer.setScaleY(currentZoom);
    }

    public void zoomToPoint(double factor, double mouseX, double mouseY) {
        double oldZoom = currentZoom;
        double newZoom = Math.max(Constants.MIN_ZOOM, Math.min(Constants.MAX_ZOOM, currentZoom * factor));

        if (newZoom == oldZoom)
            return;

        // Get mouse position relative to the Group before scaling
        Point2D mouseInContent = trafficMapContainer.sceneToLocal(trafficMapWrapper.localToScene(mouseX, mouseY));

        currentZoom = newZoom;

        // Apply scale to the Group
        trafficMapContainer.setScaleX(currentZoom);
        trafficMapContainer.setScaleY(currentZoom);

        // Force update of ScrollPane layout
        trafficMapWrapper.layout();

        javafx.geometry.Bounds viewportBounds = trafficMapWrapper.getViewportBounds();
        javafx.scene.layout.Region content = (javafx.scene.layout.Region) trafficMapWrapper.getContent();

        double viewportWidth = viewportBounds.getWidth();
        double viewportHeight = viewportBounds.getHeight();
        double contentWidth = content.getWidth();
        double contentHeight = content.getHeight();

        // Get the bounds of the Group relative to the StackPane
        javafx.geometry.Bounds groupBounds = trafficMapContainer.getBoundsInParent();

        // Calculate the point we want to keep fixed in StackPane coordinates
        double targetXInStack = groupBounds.getMinX() + mouseInContent.getX() * currentZoom;
        double targetYInStack = groupBounds.getMinY() + mouseInContent.getY() * currentZoom;

        // Calculate mouse position relative to the viewport's top-left corner
        double mouseXInViewport = mouseX - viewportBounds.getMinX();
        double mouseYInViewport = mouseY - viewportBounds.getMinY();

        if (contentWidth > viewportWidth) {
            double newHValue = (targetXInStack - mouseXInViewport) / (contentWidth - viewportWidth);
            trafficMapWrapper.setHvalue(clamp(newHValue));
        }

        if (contentHeight > viewportHeight) {
            double newVValue = (targetYInStack - mouseYInViewport) / (contentHeight - viewportHeight);
            trafficMapWrapper.setVvalue(clamp(newVValue));
        }
    }

    // Clamp function for auto scrolling when dragging near the edge of the viewport
    private double clamp(double value) {
        if (value < 0.0)
            return 0.0;
        if (value > 1.0)
            return 1.0;
        return value;
    }
}
