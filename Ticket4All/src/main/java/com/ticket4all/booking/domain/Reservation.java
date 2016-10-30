package com.ticket4all.booking.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Reservation {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private long seatId;
	private long eventId;
	private long customerId;
	private ReservationStatusEnum status;
	private LocalDateTime createdDate;
	
	public Reservation(long seatId, long eventId, long customerId, ReservationStatusEnum status) {
		super();
		this.seatId = seatId;
		this.eventId = eventId;
		this.customerId = customerId;
		this.status = status;
		createdDate = LocalDateTime.now();
	}

	public long getId() {
		return id;
	}

	public long getSeatId() {
		return seatId;
	}

	public long getEventId() {
		return eventId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public ReservationStatusEnum getStatus() {
		return status;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + (int) (customerId ^ (customerId >>> 32));
		result = prime * result + (int) (eventId ^ (eventId >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (int) (seatId ^ (seatId >>> 32));
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		Reservation other = (Reservation) obj;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (customerId != other.customerId)
			return false;
		if (eventId != other.eventId)
			return false;
		if (id != other.id)
			return false;
		if (seatId != other.seatId)
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", seatId=" + seatId + ", eventId=" + eventId + ", customerId=" + customerId
				+ ", status=" + status + ", createdDate=" + createdDate + "]";
	}
}
