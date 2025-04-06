package Room8.Back.hotel.repository;

import Room8.Back.hotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    // find by hotel city
    List<Room> findByHotelCity(String city);

}
