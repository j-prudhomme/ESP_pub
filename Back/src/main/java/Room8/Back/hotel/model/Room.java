package Room8.Back.hotel.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room")
@Data
@NoArgsConstructor
public class Room {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_type", nullable = false, length = 30)
    private String roomType;

    @Column(name = "room_number", nullable = false, length = 30)
    private String roomNumber;

    @Column(name = "view", nullable = false, length = 30)
    private String view;

    @Column(name = "bed_type", nullable = false, length = 30)
    private String bedType;

    @Column(name = "accessibility", nullable = false, length = 30)
    private String accessibility;

    @Column(name = "price_per_night", nullable = false, length = 30)
    private BigDecimal pricePerNight;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "check_in")
    private LocalDate checkIn;

    @Column(name = "check_out")
    private LocalDate checkOut;

    @ManyToOne(fetch = FetchType.EAGER)
    private Hotel hotel;
}
