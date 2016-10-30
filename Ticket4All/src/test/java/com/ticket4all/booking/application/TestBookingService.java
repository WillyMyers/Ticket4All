package com.ticket4all.booking.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ticket4all.booking.domain.CategoryEnum;
import com.ticket4all.booking.domain.Reservation;
import com.ticket4all.booking.domain.Seat;

public class TestBookingService extends BaseTest {

	@Autowired
	BookingService service;

	@Test
	public void testBookSpecifiedSeatsForNonExistentEvent() throws BookingServiceException {
		try {
			service.bookSeats(1234, 200l, Arrays.asList(30l, 31l, 32l, 33l));
		} catch (Exception e) {
			assertThat(e).isInstanceOf(BookingServiceException.class).hasMessage("No event found with id 200");
		}
	}

	@Test
	public void testBookSpecifiedSeats() throws BookingServiceException {
		List<Reservation> seats = service.bookSeats(1234, 100l, Arrays.asList(30l, 31l, 32l, 33l));
		assertNotNull(seats);
		assertEquals(4, seats.size());
	}

	@Test
	public void testBookSpecifiedSameSeats() throws BookingServiceException {
		List<Reservation> seats = service.bookSeats(1234, 100l, Arrays.asList(400l, 411l, 412l, 431l));
		assertNotNull(seats);
		assertEquals(4, seats.size());
		try {
			seats = service.bookSeats(123, 100l, Arrays.asList(401l, 402l, 403l, 431l));
		} catch (Exception e) {
			assertThat(e).isInstanceOf(BookingServiceException.class)
					.hasMessage("One or all of the requested seats are already booked");
		}
	}

	@Test
	public void testBookSpecifiedSameSeatsThenTryAgainShouldSucceed() throws BookingServiceException {
		List<Reservation> seats = service.bookSeats(1234, 100l, Arrays.asList(500l, 511l, 512l, 531l));
		assertNotNull(seats);
		assertEquals(4, seats.size());
		try {
			seats = service.bookSeats(123, 100l, Arrays.asList(501l, 502l, 503l, 531l));
		} catch (Exception e) {
			assertThat(e).isInstanceOf(BookingServiceException.class)
					.hasMessage("One or all of the requested seats are already booked");
		}
		seats = service.bookSeats(123, 100l, Arrays.asList(501l, 502l, 503l));
		assertNotNull(seats);
		assertEquals(3, seats.size());

	}

	@Test
	public void testBookNumberOfSeatsForNonExistentEvent() throws BookingServiceException {
		try {
			service.bookSeats(1234, 200l, 4);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(BookingServiceException.class).hasMessage("No event found with id 200");
		}
	}

	@Test
	public void testBook2000SeatsThrowsException() throws BookingServiceException {
		try {
			service.bookSeats(1234, 100l, 2000);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(BookingServiceException.class).hasMessageContaining("Attempted to book 2000 seats but only");
		}
	}

	@Test
	public void testBookNumberOfSeatsReturnsVIPSeats() throws BookingServiceException {
		List<Reservation> seats = service.bookSeats(1234, 100l, 4);
		assertNotNull(seats);
		assertEquals(4, seats.size());
		for (Reservation r : seats) {
			Seat seat = seatRepo.findOne(r.getSeatId());
			assertEquals(CategoryEnum.VIP, seat.getCategory());
		}
	}

}
