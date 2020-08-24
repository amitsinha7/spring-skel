package com.cepheid.cloud.skel.response;

import java.util.List;
import java.util.Set;

import com.cepheid.cloud.skel.model.Description;
import com.cepheid.cloud.skel.model.Item;

public class ItemResponseDTO {

	private ErrorInfo errorInfo;

	private Item item;

	private List<Item> items;

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	private Set<Description> descriptions;

	public ErrorInfo getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(ErrorInfo errorInfo) {
		this.errorInfo = errorInfo;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Set<Description> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Set<Description> descriptions) {
		this.descriptions = descriptions;
	}
}
