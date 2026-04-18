package baseFeature.behavior;

import baseFeature.vehicle.Vehicle;

public interface DriverBehavior {

    //Thay đổi vận tốc của phương tiện.
    void changeSpeed(Vehicle vehicle);

    //Thực hiện việc chuyển làn đường.
    void changeLane(Vehicle vehicle);

    //Xử lý hành vi khi gặp đèn đỏ.
    void onRedLight(Vehicle vehicle);

    //Phản ứng khi có phương tiện khác xin vượt.
    void onPassingRequest(Vehicle vehicle);

    //Hành động khẩn cấp khi xảy ra sự cố bất ngờ.
    void onEmergency(Vehicle vehicle);
}