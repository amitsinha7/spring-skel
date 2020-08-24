package com.cepheid.cloud.skel.controller;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.cepheid.cloud.skel.constant.ItemConstants;
import com.cepheid.cloud.skel.exception.ResourceNotFoundException;
import com.cepheid.cloud.skel.helper.Validator;
import com.cepheid.cloud.skel.model.Item;
import com.cepheid.cloud.skel.model.ItemRequest;
import com.cepheid.cloud.skel.response.ErrorInfo;
import com.cepheid.cloud.skel.response.ItemResponseDTO;
import com.cepheid.cloud.skel.service.ItemService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

// curl http:/localhost:9443/app/api/1.0/items

@Component
@Path("/api/1.0/items")
@Api(value = "Item Controller")
public class ItemController {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private final ItemService mItemService;

	private final Validator mValidator;

	@Autowired
	public ItemController(ItemService itemService, Validator validator) {
		mItemService = itemService;
		mValidator = validator;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get All Item From The Item Table ")
	public ItemResponseDTO getAllItems() {
		LOGGER.info("getAllItems ()");
		ItemResponseDTO response = new ItemResponseDTO();
		try {
			List<Item> items = mItemService.findAll();
			if (items != null) {
				response.setItems(items);
			} else {
				response.setErrorInfo(getErrorInfo("110003"));
				return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.OK).getBody();
			}
		} catch (Exception e) {
			LOGGER.error("Exception Message As : " + e.getMessage());
			response.setErrorInfo(getErrorInfo("110000"));
			return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.INTERNAL_SERVER_ERROR).getBody();
		}
		return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.OK).getBody();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get Item By ID ")
	public ItemResponseDTO getItemById(@PathParam(value = "id") Long itemId) throws ResourceNotFoundException {

		LOGGER.info("getItemById() " + itemId);
		ItemResponseDTO response = new ItemResponseDTO();
		try {
			response.setItem(mItemService.getItem(itemId));
		} catch (ResourceNotFoundException resourceNotFoundException) {
			LOGGER.info("Item not found :: " + itemId);
			response.setErrorInfo(getErrorInfo("110003"));
			return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.OK).getBody();
		} catch (Exception e) {
			LOGGER.error("Exception Message As : " + e.getMessage());
			response.setErrorInfo(getErrorInfo("110000"));
			return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.INTERNAL_SERVER_ERROR).getBody();
		}
		return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.OK).getBody();
	}

	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create New Item ")
	public ItemResponseDTO createItem(@RequestBody ItemRequest itemRequest) {
		LOGGER.info("createItem() ");
		ItemResponseDTO response = new ItemResponseDTO();
		try {
			if (mValidator.validateItemRequest(itemRequest)) {
				LOGGER.error("Error At Validating the Request : " + itemRequest.toString());
				response.setErrorInfo(getErrorInfo("110001"));
				return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.BAD_REQUEST).getBody();
			} else {
				Item item = mItemService.createNewItem(itemRequest);
				response.setItem(item);
			}
		} catch (DataIntegrityViolationException dve) {
			LOGGER.error("Exception Message As : " + dve.getMessage());
			response.setErrorInfo(getErrorInfo("110004"));
			return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.OK).getBody();
		} catch (Exception e) {
			LOGGER.error("Exception Message As : " + e.getMessage());
			response.setErrorInfo(getErrorInfo("110000"));
			return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.INTERNAL_SERVER_ERROR).getBody();
		}
		return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.OK).getBody();
	}

	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update Existing Item And Also Add New Description Rows(With Auto Increment ID) In Case ConstraintViolationException Not There ")
	public ItemResponseDTO updateItem(@RequestBody Item item) {
		LOGGER.info("updateItem() ");
		ItemResponseDTO response = new ItemResponseDTO();
		try {
			if (mValidator.validateItem(item)) {
				LOGGER.error("Error At Validating the Request : " + item.toString());
				response.setErrorInfo(getErrorInfo("110001"));
				return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.BAD_REQUEST).getBody();
			} else {
				Item updatedItem = mItemService.update(item);
				response.setItem(updatedItem);
			}
		} catch (DataIntegrityViolationException dve) {
			LOGGER.error("Exception Message As : " + dve.getMessage());
			response.setErrorInfo(getErrorInfo("110004"));
			return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.OK).getBody();
		} catch (ResourceNotFoundException resourceNotFoundException) {
			LOGGER.info("Item not found :: " + item.getId());
			response.setErrorInfo(getErrorInfo("110003"));
			return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.OK).getBody();
		} catch (Exception e) {
			LOGGER.error("Exception Message As : " + e.getMessage());
			response.setErrorInfo(getErrorInfo("110000"));
			return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.INTERNAL_SERVER_ERROR).getBody();

		}
		return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.OK).getBody();
	}

	@GET
	@Path("/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete Item For id Provided ")
	public ItemResponseDTO deleteItemById(@PathParam(value = "id") Long itemId) throws ResourceNotFoundException {

		LOGGER.info("deleteItemById() " + itemId);
		ItemResponseDTO response = new ItemResponseDTO();
		try {
			mItemService.deleteItem(itemId);
			response.setErrorInfo(getErrorInfo("110005"));
		} catch (ResourceNotFoundException resourceNotFoundException) {
			LOGGER.info("Item not found :: " + itemId);
			response.setErrorInfo(getErrorInfo("110003"));
			return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.OK).getBody();
		} catch (Exception e) {
			LOGGER.error("Exception Message As : " + e.getMessage());
			response.setErrorInfo(getErrorInfo("110000"));
			return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.INTERNAL_SERVER_ERROR).getBody();
		}
		return new ResponseEntity<ItemResponseDTO>(response, HttpStatus.OK).getBody();
	}

	private ErrorInfo getErrorInfo(String errorCode) {
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorCode(errorCode);
		errorInfo.setErrorMessage(ItemConstants.errorStore.get(errorCode));
		return errorInfo;
	}

}
