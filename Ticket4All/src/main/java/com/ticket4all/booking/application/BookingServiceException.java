package com.ticket4all.booking.application;

public class BookingServiceException extends Exception {

	public BookingServiceException(String s){
		super(s);
	}
	
	public BookingServiceException(String s, Throwable t){
		super(s, t);
	}
	
	public BookingServiceException(Throwable t){
		super(t);
	}
}
