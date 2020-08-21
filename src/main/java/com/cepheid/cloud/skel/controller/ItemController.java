package com.cepheid.cloud.skel.controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cepheid.cloud.skel.constant.ItemConstants;
import com.cepheid.cloud.skel.model.Description;
import com.cepheid.cloud.skel.model.Item;
import com.cepheid.cloud.skel.repository.DescriptionRepository;
import com.cepheid.cloud.skel.repository.ItemRepository;
import com.cepheid.cloud.skel.response.ErrorInfoDTO;
import com.cepheid.cloud.skel.response.ItemResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

// curl http:/localhost:9443/app/api/1.0/items

@Component
@Path("/api/1.0/items")
@Api()
public class ItemController {

	private final ItemRepository mItemRepository;
	private final DescriptionRepository mDescriptionRepository;

	@Autowired
	public ItemController(ItemRepository itemRepository, DescriptionRepository descriptionRepository) {
		mItemRepository = itemRepository;
		mDescriptionRepository = descriptionRepository;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@ApiOperation(value = "List Down All Items")
	public Collection<Item> getItems() {
		return mItemRepository.findAll();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ItemResponseDTO getItemById(@PathParam(value = "id") Long itemId) {
		ItemResponseDTO response = new ItemResponseDTO();
		Optional<Item> item = mItemRepository.findById(itemId);
		if (item.isPresent()) {
			response.setItem(item.get());
		} else {
			response.setErrorInfoDTO(getErrorInfoDTO("110001"));
			return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.BAD_REQUEST).getBody();
		}
		return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.OK).getBody();
	}

	@GET
	@Path("/descriptions/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ItemResponseDTO getDescriptionById(@PathParam("id") Long descriptionId) {
		ItemResponseDTO response = new ItemResponseDTO();
		Optional<Description> description = mDescriptionRepository.findById(descriptionId);
		if (description.isPresent()) {
			Set<Description> descriptions = new HashSet<Description>();
			descriptions.add(description.get());
			response.setDescriptions(descriptions);
		} else {
			response.setErrorInfoDTO(getErrorInfoDTO("110001"));
			return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.BAD_REQUEST).getBody();
		}
		return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.OK).getBody();

	}

	private ErrorInfoDTO getErrorInfoDTO(String errorCode) {
		ErrorInfoDTO errorInfo = new ErrorInfoDTO();
		errorInfo.setErrorCode(errorCode);
		errorInfo.setErrorMessage(ItemConstants.errorStore.get(errorCode));
		return errorInfo;
	}

}
