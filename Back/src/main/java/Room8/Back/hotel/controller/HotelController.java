package Room8.Back.hotel.controller;

import Room8.Back.hotel.dto.HotelOverviewDto;
import Room8.Back.hotel.dto.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Room8.Back.hotel.service.HotelService;

import java.util.List;

@RestController
@RequestMapping("/hotel")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping("/overview")
    public ResponseEntity<List<HotelOverviewDto>> getAllHotelPreviews() {
        return ResponseEntity.ok(hotelService.getAllHotelPreviews());
    }

    @PostMapping("/search/reservation")
    public ResponseEntity<List<HotelOverviewDto>> getHotelBySearchReservation(@RequestBody ReservationDTO reservationDTO) {
        return ResponseEntity.ok(hotelService.getHotelBySearchReservation(reservationDTO));
    }

}
