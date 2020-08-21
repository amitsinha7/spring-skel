package com.cepheid.cloud.skel.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Description extends AbstractEntity {

	private String type;

	private String details;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private Item item;

	public Description() {

	}

	public Description(String type, String details, Item item) {
		this.type = type;
		this.details = details;
		this.item = item;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "Description [type=" + type + ", details=" + details + "]";
	}
}
