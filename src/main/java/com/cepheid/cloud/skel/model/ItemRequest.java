package com.cepheid.cloud.skel.model;

import java.util.Set;

public class ItemRequest {

	private String name;
	private State state;
	private Set<DescriptionRequest> descriptionRequests;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Set<DescriptionRequest> getDescriptionRequests() {
		return descriptionRequests;
	}

	public void setDescriptionRequests(Set<DescriptionRequest> descriptionRequests) {
		this.descriptionRequests = descriptionRequests;
	}

	@Override
	public String toString() {
		return "ItemRequest [name=" + name + ", state=" + state + ", descriptionRequests=" + descriptionRequests + "]";
	}

}
