package com.cepheid.cloud.skel.helper;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cepheid.cloud.skel.model.Item;
import com.cepheid.cloud.skel.model.ItemRequest;

@Component
public class Validator {

	public boolean validateItemRequest(ItemRequest itemRequest) {

		if (itemRequest == null || StringUtils.isEmpty(itemRequest.getName())
				|| StringUtils.isEmpty(itemRequest.getDescriptionRequests()) || itemRequest.getState() == null
				|| !(itemRequest.getState().getCode() != null)) {

			return true;
		}
		return false;

	}

	public boolean validateItem(Item item) {
		if (item == null || StringUtils.isEmpty(item.getName())
				|| StringUtils.isEmpty(item.getDescriptions()) || item.getState() == null
				|| !(item.getState().getCode() != null) || item.getId() == null) {

			return true;
		}
		return false;

	
	}

}
