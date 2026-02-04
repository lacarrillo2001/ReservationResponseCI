package ec.edu.espe.carrilloluisexamenp3.repository;

import ec.edu.espe.carrilloluisexamenp3.model.RoomReservation;

import java.util.Optional;

public interface RoomReservationRepository {

    RoomReservation save(RoomReservation roomReservation);

    Optional <RoomReservation> findById(String id);

    boolean existByGuestEmail(String guestEmail);


    boolean isRoomReserved(String roomCode);


}
