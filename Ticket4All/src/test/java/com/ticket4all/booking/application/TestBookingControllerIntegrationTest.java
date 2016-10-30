package com.ticket4all.booking.application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
@WebAppConfiguration
public class TestBookingControllerIntegrationTest {

	@Autowired
	private WebApplicationContext webContext;
	
	@Autowired
	protected EventRepository eventRepo;
	@Autowired
	protected VenueRepository venueRepo;
	@Autowired
	protected SeatsRepository seatRepo;
	

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
		
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

	@Test
	public void bookSpecifiedSeats() throws Exception {
		mockMvc.perform(post("/tickets4all/bookseats/100/1234/").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("seatIds", "100,101,102"))
				.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void book5Seats() throws Exception {
		mockMvc.perform(post("/tickets4all/book/100/1234/").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("numOfSeats", "5"))
				.andExpect(status().is2xxSuccessful());
	}
}
