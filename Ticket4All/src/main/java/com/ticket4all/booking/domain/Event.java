package com.ticket4all.booking.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Event {
	@Id
	private long id;
	@ManyToOne
	private Venue venue;
	private LocalDateTime date;
	private EventTypeEnum type;
	
	public Event(){}
	public Event(long id, Venue venue, LocalDateTime date, EventTypeEnum type) {
		super();
		this.id = id;
		this.venue = venue;
		this.date = date;
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public Venue getVenue() {
		return venue;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public EventTypeEnum getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((venue == null) ? 0 : venue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id != other.id)
			return false;
		if (type != other.type)
			return false;
		if (venue == null) {
			if (other.venue != null)
				return false;
		} else if (!venue.equals(other.venue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", venue=" + venue + ", date=" + date + ", type=" + type + "]";
	}

	
}
