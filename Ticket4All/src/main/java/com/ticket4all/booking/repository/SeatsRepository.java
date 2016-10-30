package com.ticket4all.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticket4all.booking.domain.Seat;

public interface SeatsRepository extends JpaRepository<Seat, Long> {

}
