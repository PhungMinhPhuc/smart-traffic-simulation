package model.node;

import model.utility.TrafficPoint;

import java.util.ArrayList;

public class Junction extends TrafficNode {

    public Junction(TrafficPoint centerPoint) {
        super(centerPoint);
    }

    public Path getRandomPathFromPoint(TrafficPoint point) {
        ArrayList<Path> connectedPaths = new ArrayList<>();
        for(Path path : this.getPathList()) {
            if(path.getStartPoint().equals(point)) {
                connectedPaths.add(path);
            }
        }
        
        if(connectedPaths.isEmpty()) return null;
        
        int randomIndex = (int)(Math.random() * connectedPaths.size());
        return connectedPaths.get(randomIndex);
    }
}