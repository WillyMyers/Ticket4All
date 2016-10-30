package com.ticket4all.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticket4all.booking.domain.Event;


public interface EventRepository extends JpaRepository<Event, Long> {

}
