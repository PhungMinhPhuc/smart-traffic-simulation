package sound;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.media.AudioClip;
import model.utility.TrafficPoint;

public class SoundManager {
    
    private Map<SoundEvent, AudioClip> audioClips = new HashMap<>();
    
    // Assumed camera position at the center of the viewport
    private TrafficPoint cameraPosition = new TrafficPoint(400, 300);
    private final double MAX_AUDIBLE_DISTANCE = 800.0;

    public SoundManager() {
        loadClips();
    }

    private void loadClips() {
        try {
            // Examples of loading clips (would require actual files in /assets/)
            // audioClips.put(SoundEvent.SIREN, new AudioClip(getClass().getResource("/assets/sounds/ambulance_siren_loop.wav").toExternalForm()));
            // audioClips.put(SoundEvent.COLLISION_THUD, new AudioClip(getClass().getResource("/assets/sounds/metal_crash_thud.wav").toExternalForm()));
            // audioClips.put(SoundEvent.HORN, new AudioClip(getClass().getResource("/assets/sounds/car_horn_beep.wav").toExternalForm()));
            // audioClips.put(SoundEvent.TRAFFIC_LIGHT_CHANGE, new AudioClip(getClass().getResource("/assets/sounds/traffic_light_blip.wav").toExternalForm()));
            System.out.println("SoundManager: Audio clips simulated load.");
        } catch (Exception e) {
            System.err.println("Could not load audio clips. Ensure JavaFX is initialized and files exist.");
        }
    }

    public void setCameraPosition(TrafficPoint cameraPosition) {
        this.cameraPosition = cameraPosition;
    }

    /**
     * Plays a sound from a specific location, calculating volume based on distance.
     */
    public void play(SoundEvent event, TrafficPoint sourcePosition) {
        double dist = sourcePosition.distanceTo(cameraPosition);
        double volumeScale = Math.max(0, 1.0 - (dist / MAX_AUDIBLE_DISTANCE));
        
        play(event, volumeScale);
    }

    /**
     * Plays a sound with a specific volume scale.
     */
    public void play(SoundEvent event, double volumeScale) {
        if (volumeScale <= 0) return;
        
        AudioClip clip = audioClips.get(event);
        if (clip != null) {
            clip.play(volumeScale);
        } else {
            // Console log fallback for missing files
            System.out.printf("PLAY SOUND (simulated): %s at volume %.2f\n", event.name(), volumeScale);
        }
    }
}
