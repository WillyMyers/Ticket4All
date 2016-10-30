package com.ticket4all.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticket4all.booking.domain.Venue;

public interface VenueRepository extends JpaRepository<Venue, Long> {

}
