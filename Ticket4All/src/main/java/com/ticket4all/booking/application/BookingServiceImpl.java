package com.ticket4all.booking.application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ticket4all.booking.domain.CategoryEnum;
import com.ticket4all.booking.domain.Event;
import com.ticket4all.booking.domain.Reservation;
import com.ticket4all.booking.domain.ReservationStatusEnum;
import com.ticket4all.booking.domain.Seat;
import com.ticket4all.booking.repository.EventRepository;
import com.ticket4all.booking.repository.ReservationRepository;

@Component
@Transactional
public class BookingServiceImpl implements BookingService {

	@Autowired
	EventRepository eventRepo;
	@Autowired
	ReservationRepository reservationRepo;

	// a map to hold the seat reservations for each event - <eventId<seatId,
	// Reservation>>
	private static ConcurrentMap<Long, ConcurrentHashMap<Long, Reservation>> seats = new ConcurrentHashMap<>();
	// a map to hold queues for the best seats for each event - <eventId,
	// <seatId>>
	private static ConcurrentMap<Long, ConcurrentLinkedQueue<Long>> bestSeats = new ConcurrentHashMap<>();
	private static Reservation dummyReservation = new Reservation(0, 0, 0, null);

	@Override
	public List<Reservation> bookSeats(long customerId, long eventId, List<Long> seatIds) throws BookingServiceException {
		if (!seats.containsKey(eventId)) {
			populateCaches(eventId);
		}

		List<Reservation> reservations = new ArrayList();
		if (seats.containsKey(eventId)) {
			for (Long id : seatIds) {
				Reservation r = new Reservation(id, eventId, customerId, ReservationStatusEnum.BOOKED);
				boolean success = seats.get(eventId).replace(id, dummyReservation, r);
				if (!success) {
					// roll back all the previously booked seats and throw an
					// exception
					for (Reservation res : reservations) {
						seats.get(eventId).replace(res.getSeatId(), dummyReservation);
					}
					throw new BookingServiceException("One or all of the requested seats are already booked");
				} else {
					reservations.add(r);
				}

			}
			reservationRepo.save(reservations);
		} else {
			throw new BookingServiceException("No event found with id " + eventId);
		}
		return reservations;
	}

	/**
	 * Book the best n seats
	 */
	@Override
	public List<Reservation> bookSeats(long customerId, long eventId, int numOfSeats) throws BookingServiceException {
		if (!seats.containsKey(eventId)) {
			populateCaches(eventId);
		}

		// not enough seats on the queue so throw an exception
		if (bestSeats.get(eventId).size() < numOfSeats) {
			throw new BookingServiceException(
					"Attempted to book " + numOfSeats + " seats but only " + bestSeats.get(eventId).size() + " left");
		}

		List<Reservation> reservations = new ArrayList();
		int count = 0;
		if (seats.containsKey(eventId)) {			
			while (count < numOfSeats) {
				Long id = bestSeats.get(eventId).poll();
				if (id == null) {
					throw new BookingServiceException("No seats left!");
				} else {
					Reservation r = new Reservation(id, eventId, customerId, ReservationStatusEnum.BOOKED);
					boolean replaced = seats.get(eventId).replace(id, dummyReservation, r);
					if (replaced) {
						reservations.add(r);
						count++;
					}
				}
				reservationRepo.save(reservations);
			}
		} else {
			throw new BookingServiceException("No event found with id " + eventId);
		}
		return reservations;
	}

	private void populateCaches(long eventId) throws BookingServiceException {
		//we only want to do this once so make sure no 2 threads can do it at the same time
		synchronized (this) {
			if (!seats.containsKey(eventId)) {
				if (eventRepo.exists(eventId)) {
					Event event = eventRepo.findOne(eventId);
					ConcurrentHashMap<Long, Reservation> seatMap = new ConcurrentHashMap<>();
					for (Seat seat : event.getVenue().getSeats()) {
						Reservation r = reservationRepo.findBySeatIdAndEventId(seat.getId(), eventId);
						seatMap.put(seat.getId(), r ==null? dummyReservation: r);
					}
					seats.put(eventId, seatMap);

					ConcurrentLinkedQueue<Long> bestSeatsQ = new ConcurrentLinkedQueue<>();
					for (CategoryEnum e : CategoryEnum.values()) {
						bestSeatsQ.addAll(event.getVenue().getSeats().stream().filter(s -> s.getCategory().equals(e))
								.map(n -> n.getId()).collect(Collectors.toList()));
					}
					bestSeats.put(eventId, bestSeatsQ);
				} else {
					throw new BookingServiceException("No event found with id " + eventId);
				}
			}
		}
	}

}
