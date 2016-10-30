package com.ticket4all.booking.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ticket4all.booking.domain.CategoryEnum;
import com.ticket4all.booking.domain.Event;
import com.ticket4all.booking.domain.EventTypeEnum;
import com.ticket4all.booking.domain.Seat;
import com.ticket4all.booking.domain.Venue;
import com.ticket4all.booking.repository.EventRepository;
import com.ticket4all.booking.repository.SeatsRepository;
import com.ticket4all.booking.repository.VenueRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseTest {

	@Autowired
	protected EventRepository eventRepo;
	@Autowired
	protected VenueRepository venueRepo;
	@Autowired
	protected SeatsRepository seatRepo;

	@Before
	public void setUp() {
		if (eventRepo.count() == 0) {
			List<Seat> seats = new ArrayList<>();
			// create 1000 seats, 4 sections, 25 rows, 10 seats in each
			// first 2 rows are VIP, next 3 rows are PREMIUM, next 15 are
			// STANDARD, then last 5 are RESTRICTED_VIEW
			long row = 0;
			long section = 0;
			CategoryEnum cat;
			for (int i = 0; i < 1000; i++) {
				if (i % 40 == 0) {
					row++;
					section = 1;
				} else if (i % 10 == 0) {
					section++;
				}
				if (row == 1) {
					cat = CategoryEnum.VIP;
				} else if (row > 1 && row < 4) {
					cat = CategoryEnum.PREMIUM;
				} else if (row > 3 && row < 16) {
					cat = CategoryEnum.STANDARD;
				} else {
					cat = CategoryEnum.RESTRICTED_VIEW;
				}

				Seat s = new Seat(i, "Section:" + section + "|Row:" + row + "|Seat:" + i, section, row, cat);
				seats.add(s);
			}
			seatRepo.save(seats);
			seatRepo.flush();
			Venue v = new Venue("O2", seats);
			venueRepo.saveAndFlush(v);
			Event e = new Event(100, v, LocalDateTime.now(), EventTypeEnum.MUSIC);
			eventRepo.saveAndFlush(e);
		}
	}

	@After
	public void tearDown() {
		// eventRepo.deleteAll();
		// eventRepo.flush();
		// venueRepo.deleteAll();
		// venueRepo.flush();
		// seatRepo.deleteAll();
		// seatRepo.flush();
	}

	@Test
	public void contextLoads() {
	}

}
