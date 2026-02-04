package ec.edu.espe.carrilloluisexamenp3.model;
import java.util.UUID;

public class RoomReservation {
    private final String id;
    private final String roomCode;
    private final String reservedByEmail;
    private final int hours;
    private String status;

    public RoomReservation (String roomCode, String reservedByEmail, int hours) {
        this.id = UUID.randomUUID().toString();
        this.roomCode = roomCode;
        this.reservedByEmail = reservedByEmail;
        this.hours = hours;
        this.status = "CREATED";
    }

    public String getId() {
        return id;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public String getReservedByEmail() {
        return reservedByEmail;
    }

    public int getHours() {
        return hours;
    }

    public String getStatus() {
        return status;
    }
}
