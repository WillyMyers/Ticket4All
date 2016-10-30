package com.ticket4all.booking.application;

import java.util.List;

import com.ticket4all.booking.domain.Reservation;

public interface BookingService {

	List<Reservation> bookSeats(long customerId, long eventId, List<Long> seatIds) throws BookingServiceException;

	List<Reservation> bookSeats(long customerId, long eventId, int numOfSeats) throws BookingServiceException;

}
