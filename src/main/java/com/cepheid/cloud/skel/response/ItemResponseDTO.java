package com.cepheid.cloud.skel.response;

import java.util.Set;

import com.cepheid.cloud.skel.model.Description;
import com.cepheid.cloud.skel.model.Item;

public class ItemResponseDTO {

	private ErrorInfoDTO errorInfoDTO;

	private Item item;
	
	private Set<Description> descriptions;

	public ErrorInfoDTO getErrorInfoDTO() {
		return errorInfoDTO;
	}

	public void setErrorInfoDTO(ErrorInfoDTO errorInfoDTO) {
		this.errorInfoDTO = errorInfoDTO;
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
