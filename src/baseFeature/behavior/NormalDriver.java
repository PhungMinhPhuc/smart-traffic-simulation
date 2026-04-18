package baseFeature.behavior;

import baseFeature.vehicle.Vehicle;

public class NormalDriver implements DriverBehavior {

     //Xử lý logic chuyển làn cho tài xế bình thường.
    @Override
    public void changeLane(Vehicle v) {
        // Logic xử lý chuyển làn an toàn
    }

    //Điều chỉnh tốc độ theo lưu lượng giao thông thông thường.
    @Override
    public void changeSpeed(Vehicle v) {
        // Ví dụ: v.setSpeed(v.getSpeed() + 5);
    }

     // Phản ứng khi có tình huống khẩn cấp xảy ra.
    @Override
    public void onEmergency(Vehicle v) {
        // Ví dụ: Phanh gấp hoặc tấp vào lề
    }

    // Xử lý tình huống khi có phương tiện khác xin vượt.
    @Override
    public void onPassingRequest(Vehicle v) {
        // Cho vượt hoặc giữ nguyên tốc độ tùy điều kiện
    }

     //Thực hiện dừng xe khi gặp tín hiệu đèn đỏ.
    @Override
    public void onRedLight(Vehicle v) {
        // v.setSpeed(0);
    }
}