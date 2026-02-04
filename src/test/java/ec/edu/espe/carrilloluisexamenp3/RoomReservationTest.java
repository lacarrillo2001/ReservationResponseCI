package ec.edu.espe.carrilloluisexamenp3;
import ec.edu.espe.carrilloluisexamenp3.dto.RoomReservationResponse;
import ec.edu.espe.carrilloluisexamenp3.model.RoomReservation;
import ec.edu.espe.carrilloluisexamenp3.repository.RoomReservationRepository;
import ec.edu.espe.carrilloluisexamenp3.service.RoomReservationService;
import ec.edu.espe.carrilloluisexamenp3.service.UserPolicyClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.internal.matchers.Null;
import org.mockito.internal.matchers.Or;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/// Creación exitosa de una reserva con datos válidos.
/// Error por correo electrónico inválido.
/// Error por número de horas fuera del rango permitido.
/// Error cuando la sala ya se encuentra reservada.
///
///
/// Requisitos técnicos
///
/// Uso obligatorio de assertThrows para casos negativos.
/// Uso de verify() y verifyNoInteractions() para validar interacciones.
/// Las dependencias externas deberán ser mockeadas.
/// No se debe levantar el contexto de Spring para las pruebas.
///
public class RoomReservationTest {
    private RoomReservationRepository roomReservationRepository;
    private RoomReservationService roomReservationService;
    private UserPolicyClient userPolicyClient;

    @BeforeEach
    public  void setUp(){
        roomReservationRepository = org.mockito.Mockito.mock(RoomReservationRepository.class);
        userPolicyClient = org.mockito.Mockito.mock(UserPolicyClient.class);
        roomReservationService = new RoomReservationService(roomReservationRepository, userPolicyClient);
    }

    /// Creación exitosa de una reserva con datos válidos.
    @Test
    void createReservation_validData_shouldSaveAndReturnResponse() {
        // Arrange
        String roomCode = "A101";
        String email = "luisCarrillo@espe.edu.ec";
        int hours = 4;

        when(roomReservationRepository.isRoomReserved(roomCode)).thenReturn(Boolean.FALSE);
        when(roomReservationRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        // Act
        RoomReservationResponse response = roomReservationService.createReservation(roomCode, email, hours);

        //Assert
        assertNotNull(response.getReservationId());
        assertEquals("CONFIRMED", response.getStatus());

        verify(userPolicyClient).isBlocked(email);
        verify(roomReservationRepository).isRoomReserved(roomCode);
        verify(roomReservationRepository).save(any());

    }
    /// Error por correo electrónico inválido.
    @Test
    void createReservation_invalidEmail_shouldThrow_andNotCallDependencies() {
        // Arrange
        String roomCode = "A101";
        String email = "luisCarrillo-espe.edu.ec";
        int hours = 4;
    // Act & Assert}
         assertThrows(IllegalArgumentException.class, () ->
            roomReservationService.createReservation(roomCode, email, hours));
        // No debe llamar a las dependencias por que falla la validacion
        verifyNoInteractions(roomReservationRepository, userPolicyClient);

    }
    /// Error por número de horas fuera del rango permitido.
    @Test
    void createReservation_invalidHours_shouldThrow_andNotCallDependencies() {
        // Arrange
        String roomCode = "A101";
        String email = "luis@espe.edu.ec";
        int hours = 10;


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,() ->
            roomReservationService.createReservation(roomCode, email, hours));

        assertEquals("Hours must be >0 and <=8", exception.getMessage());

        verifyNoInteractions(roomReservationRepository, userPolicyClient);
    }

    /// Error cuando la sala ya se encuentra reservada.
    @Test
    void createReservation_whenRoomIsAlreadyReserved_shouldThrowException() {
        // Arrange
        String roomCode = "A101";
        String email = "luis@espe.edu.ec";
        int hours = 4;

        when(roomReservationRepository.isRoomReserved(roomCode)).thenReturn(Boolean.TRUE);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                roomReservationService.createReservation(roomCode, email, hours));

        assertEquals("Room is already reserved", exception.getMessage());

        verify(userPolicyClient).isBlocked(email);
        verify(roomReservationRepository).isRoomReserved(roomCode);
        verify(roomReservationRepository, never()).save(any(RoomReservation.class));
    }

    @Test
    void createReservation_whenUserIsBlocked_shouldThrowException() {
        // Arrange
        String roomCode = "A101";
        String email = "luis@espe.edu.ec";
        int hours = 4;

        when(userPolicyClient.isBlocked(email)).thenReturn(Boolean.TRUE);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> roomReservationService.createReservation(roomCode, email, hours));

        assertEquals("User is blocked by institutional policies", exception.getMessage());

        verify(userPolicyClient).isBlocked(email);
        verify(roomReservationRepository, never()).isRoomReserved(anyString());
        verify(roomReservationRepository, never()).save(any(RoomReservation.class));
    }

}
