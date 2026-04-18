package baseFeature.test;

import baseFeature.supportClass.SoundPlayer;

public class TestSoundPlayer {

    public static void main(String[] args) {

        // Gọi tên file chính xác (đảm bảo file tồn tại trong thư mục resources)
        SoundPlayer.playSound("car_honk.wav");

         //quan trọng:duy trì chương trình trong 5 giây. 
         //nếu main kết thúc ngay, âm thanh sẽ bị ngắt đột ngột.
         
        try {
            System.out.println("Please wait for a moment...");
            Thread.sleep(5000); 
        } catch (InterruptedException e) {
            System.err.println("Testing process was interrupted.");
            e.printStackTrace();
        }

        System.out.println("Sound test completed.");
    }
}