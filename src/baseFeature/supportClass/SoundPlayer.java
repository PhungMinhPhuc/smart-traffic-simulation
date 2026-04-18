package baseFeature.supportClass;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class SoundPlayer {

	public static void playSound(String fileName) {
        try {
            // Thiết lập đường dẫn tương đối đến thư mục tài nguyên
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
            
            // Thông báo trạng thái để phục vụ việc debug
            System.out.println("Playing sound: " + fileName);
            
        } catch (Exception e) {
            System.err.println("Error: Failed to play audio file.");
            e.printStackTrace();
        }
    }
}