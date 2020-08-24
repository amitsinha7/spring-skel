package com.cepheid.cloud.skel.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cepheid.cloud.skel.exception.ResourceNotFoundException;
import com.cepheid.cloud.skel.model.Description;
import com.cepheid.cloud.skel.model.DescriptionRequest;
import com.cepheid.cloud.skel.model.Item;
import com.cepheid.cloud.skel.model.ItemRequest;
import com.cepheid.cloud.skel.repository.ItemRepository;
import com.cepheid.cloud.skel.service.DescriptionService;
import com.cepheid.cloud.skel.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private DescriptionService descriptionService;

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Item> findAll() {
		return itemRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Item getItem(Long itemId) throws ResourceNotFoundException {
		return itemRepository.findById(itemId)
				.orElseThrow(() -> new ResourceNotFoundException("Item not found :: " + itemId));
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public Item createNewItem(ItemRequest itemRequest) {
		LOGGER.info("genertaeItemFromItemRequest() ");
		Item item = new Item();
		List<Description> descriptions = new ArrayList<Description>();
		item.setName(itemRequest.getName());
		item.setState(itemRequest.getState());
		Item newItem = itemRepository.save(item);
		itemRequest.getDescriptionRequests().forEach((DescriptionRequest descriptionRequest) -> {
			Description description = new Description();
			description.setType(descriptionRequest.getType());
			description.setDetails(descriptionRequest.getDetails());
			description.setItem(newItem);
			Description newDescription = descriptionService.createDescription(description);
			descriptions.add(newDescription);
		});
		newItem.setDescriptions(descriptions);
		return newItem;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public Item update(Item itemFromRequest) throws ResourceNotFoundException {
		itemRepository.findById(itemFromRequest.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Item not found :: " + itemFromRequest.getId()));
		return itemRepository.save(itemFromRequest);
	}

	@Override
	@Transactional
	public void deleteItem(Long itemId) throws NotFoundException {
		itemRepository.deleteById(itemId);
	}
}
