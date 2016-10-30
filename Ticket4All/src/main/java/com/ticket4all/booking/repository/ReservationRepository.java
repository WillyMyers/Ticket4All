package com.ticket4all.booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticket4all.booking.domain.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	Reservation findBySeatIdAndEventId(Long seatId, Long eventId);
}
