package com.ticket4all.booking.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ticket4all.booking.domain.Reservation;

@Controller
@RequestMapping("/tickets4all/")
public class BookingController {

	@Autowired
	BookingService service;

	@RequestMapping(value = "/bookseats/{event}/{customer}", method = RequestMethod.POST)
	public ResponseEntity<?> bookSeats(@PathVariable("customer") long customerId, @PathVariable("event") long eventId,
			@RequestParam("seatIds") List<Long> seatIds) {
		try {
			List<Reservation> seats = service.bookSeats(customerId, eventId, seatIds);
			return new ResponseEntity<>(seats, HttpStatus.OK);
		} catch (BookingServiceException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/book/{event}/{customer}", method = RequestMethod.POST)
	public ResponseEntity<?> bookNumberOfSeats(@PathVariable("customer") long customerId,
			@PathVariable("event") long eventId, @RequestParam("numOfSeats") int numOfSeats) {
		try {
			List<Reservation> seats = service.bookSeats(customerId, eventId, numOfSeats);
			return new ResponseEntity<>(seats, HttpStatus.OK);
		} catch (BookingServiceException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
