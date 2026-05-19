package main.java.handler;

import javafx.scene.media.AudioClip;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    // Cache loaded sounds to avoid loading the same file multiple times
    private static final Map<String, AudioClip> cache = new HashMap<>();

    // Global sound switch
    private static boolean muted = false;

    public static void play(String soundFile) {

        // Do not play sound if muted
        if (muted) {
            return;
        }

        // Ignore invalid file names
        if (soundFile == null || soundFile.isBlank()) {
            return;
        }

        try {
            // Get sound from cache if it was loaded before
            AudioClip clip = cache.get(soundFile);

            // Load sound file if not in cache
            if (clip == null) {
                URL url = SoundManager.class.getResource("/assets/sounds/" + soundFile);

                // Stop if the file does not exist
                if (url == null) {
                    System.out.println("Cannot find sound file: " + soundFile);
                    return;
                }

                // Create AudioClip from resource
                clip = new AudioClip(url.toExternalForm());

                // Save to cache for future use
                cache.put(soundFile, clip);
            }

            // Play the sound
            clip.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mute() {
        muted = true;

        // Stop all currently playing sounds immediately
        for (AudioClip clip : cache.values()) {
            clip.stop();
        }
    }

    public static void unmute() {
        muted = false;
    }

    public static boolean isMuted() {
        return muted;
    }
}