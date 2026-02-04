package ec.edu.espe.carrilloluisexamenp3.dto;

public class RoomReservationResponse {

    private final String status;
    private String reservationId;
    private String confirmationCode;

    public RoomReservationResponse(String reservationId, String status) {
        this.reservationId = reservationId;
        this.status = "CONFIRMED";
    }

    public String getReservationId() {
        return reservationId;
    }


    public String getStatus() {
        return status;
    }
}
