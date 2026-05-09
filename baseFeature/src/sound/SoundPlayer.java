package sound;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
 
public class SoundPlayer {
    public static void playSound(String fileName) {
        try {
            String path = "/baseFeature/resources/" + fileName;
            
            InputStream audioSrc = SoundPlayer.class.getResourceAsStream(path);
            
            if (audioSrc == null) {
                System.err.println("Error: Cannot find file at " + path);
                return;
            }
            
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start(); 
            
            // In ra thông báo để xác nhận đã tìm thấy và đang phát
            System.out.println("Playing sound: " + fileName);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
