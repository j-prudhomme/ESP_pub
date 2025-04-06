package Room8.Back.hotel.service;

import Room8.Back.hotel.dto.HotelOverviewDto;
import Room8.Back.hotel.dto.ReservationDTO;
import Room8.Back.hotel.dto.RoomOverviewDTO;
import Room8.Back.hotel.model.Hotel;
import Room8.Back.hotel.model.Room;
import Room8.Back.hotel.repository.HotelRepository;
import Room8.Back.hotel.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class HotelService {
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private RoomRepository roomRepository;

    public List<HotelOverviewDto> getAllHotelPreviews() {
        List<Hotel> hotels = hotelRepository.findAll();
        List<HotelOverviewDto> hotelOverviewDtos = new ArrayList<>();
        List<Room> rooms = roomRepository.findAll();
        int numberOfRooms = 0;

        for (Hotel hotel : hotels) {
            for (Room room : rooms) {
                if (Objects.equals(room.getHotel().getId(), hotel.getId())) {
                    numberOfRooms++;
                }
            }
            HotelOverviewDto hotelOverviewDto = new HotelOverviewDto();
            hotelOverviewDto.setName(hotel.getName());
            hotelOverviewDto.setCity(hotel.getCity());
            hotelOverviewDto.setNbRoom(numberOfRooms);
            numberOfRooms = 0;

            hotelOverviewDtos.add(hotelOverviewDto);
        }
        return hotelOverviewDtos;
    }

    public Map<List<HotelOverviewDto>, List<RoomOverviewDTO>> getHotelBySearchReservation(ReservationDTO reservationDTO) {
        List<Hotel> hotels = hotelRepository.findByCity(reservationDTO.getCity());
        List<HotelOverviewDto> hotelOverviewDtos = new ArrayList<>();
        Boolean isAvailable = false;
        Short numberOfRooms = 0;

        for (Hotel hotel : hotels) {
            for (Room room : hotel.getRoom()) {
                // Check if the room is available for the given dates
                if (room.getCheckIn() == null && room.getCheckOut() == null) {
                    isAvailable = true;
                    numberOfRooms++;
                }
                else if (reservationDTO.getCheckOut().isBefore(room.getCheckIn()) || reservationDTO.getCheckIn().isAfter(room.getCheckOut())) {
                    isAvailable = true;
                    numberOfRooms++;
                }
            }
            if (isAvailable) {
                HotelOverviewDto hotelOverviewDto = new HotelOverviewDto();
                hotelOverviewDto.setName(hotel.getName());
                hotelOverviewDto.setCity(hotel.getCity());
                hotelOverviewDto.setNbRoom(numberOfRooms);
                hotelOverviewDtos.add(hotelOverviewDto);
            }
            isAvailable = false;
            numberOfRooms = 0;
        }

        return hotelOverviewDtos;
    }

}
