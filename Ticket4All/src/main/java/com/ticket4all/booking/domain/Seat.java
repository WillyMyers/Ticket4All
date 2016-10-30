package com.ticket4all.booking.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Seat {
	@Id
	private long id;
	private String name;
	private long section;
	private long row;
	private CategoryEnum category;
	
	public Seat(){}
	
	public Seat(long id, String name, long section, long row, CategoryEnum category) {
		super();
		this.id = id;
		this.name = name;
		this.section = section;
		this.row = row;
		this.category = category;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getSection() {
		return section;
	}

	public long getRow() {
		return row;
	}

	public CategoryEnum getCategory() {
		return category;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (row ^ (row >>> 32));
		result = prime * result + (int) (section ^ (section >>> 32));
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
		Seat other = (Seat) obj;
		if (category != other.category)
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (row != other.row)
			return false;
		if (section != other.section)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Seat [id=" + id + ", name=" + name + ", section=" + section + ", row=" + row + ", category=" + category
				+ "]";
	}

	
}