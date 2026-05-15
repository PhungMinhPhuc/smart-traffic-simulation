package main.java.handler;
import javafx.scene.media.AudioClip;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static final Map<String, AudioClip> cache = new HashMap<>();

    public static void play(String soundFile) {
        if (soundFile == null || soundFile.isBlank()) {
            return;
        }

        try {
            AudioClip clip = cache.get(soundFile);

            if (clip == null) {
                URL url = SoundManager.class.getResource(
                        "/assets/sounds/" + soundFile
                );

                if (url == null) {
                    System.out.println("Cannot find sound file: " + soundFile);
                    return;
                }

                clip = new AudioClip(url.toExternalForm());
                cache.put(soundFile, clip);
            }

            clip.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}