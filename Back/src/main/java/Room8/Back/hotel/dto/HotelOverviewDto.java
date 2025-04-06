package Room8.Back.hotel.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HotelOverviewDto {
    private String name;
    private String city;
    private int nbRoom;

    public HotelOverviewDto(String name, String city, int nbRoom) {
        this.name = name;
        this.city = city;
        this.nbRoom = nbRoom;
    }
}
