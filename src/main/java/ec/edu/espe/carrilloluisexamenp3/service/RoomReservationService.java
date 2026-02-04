package ec.edu.espe.carrilloluisexamenp3.service;


import ec.edu.espe.carrilloluisexamenp3.dto.RoomReservationResponse;
import ec.edu.espe.carrilloluisexamenp3.model.RoomReservation;
import ec.edu.espe.carrilloluisexamenp3.repository.RoomReservationRepository;


public class RoomReservationService {
    private final RoomReservationRepository roomReservationRepository;
    private final UserPolicyClient userPolicyClient;

    public RoomReservationService(RoomReservationRepository roomReservationRepository, UserPolicyClient userPolicyClient) {
        this.roomReservationRepository = roomReservationRepository;
        this.userPolicyClient = userPolicyClient;
    }

    //Crear una reserva
    public RoomReservationResponse createReservation(String roomCode, String email, int hours){

        //roomCode no puede ser nulo ni vacío.
        if (roomCode == null || roomCode.isEmpty()){
            throw new IllegalArgumentException("Room code cannot be null or empty");
        }
        //email debe tener un formato válido.
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")){
            throw new IllegalArgumentException("Invalid email format");
        }
        //hours debe ser mayor a 0 y menor o igual a 8.
        if (hours <=0 || hours >8){
            throw new IllegalArgumentException("Hours must be >0 and <=8");
        }

        //No se permite crear reservas para usuarios bloqueados por políticas institucionales.
        if (userPolicyClient.isBlocked(email)){
            throw new IllegalStateException("User is blocked by institutional policies");
        }

        //No se puede crear una reserva si la sala ya se encuentra reservada.
        if (roomReservationRepository.isRoomReserved(roomCode)){
            throw new IllegalStateException("Room is already reserved");
        }


        RoomReservation roomReservation = new RoomReservation(roomCode, email, hours);
        RoomReservation save= roomReservationRepository.save(roomReservation);

        return new RoomReservationResponse(save.getId(), "CONFIRMED");
    }

}
