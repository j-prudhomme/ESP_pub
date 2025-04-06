package Room8.Back.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {

    private String city;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int nbPerson;
    private int nbRoom;
}
