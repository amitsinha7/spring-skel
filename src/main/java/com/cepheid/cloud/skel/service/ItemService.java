package com.cepheid.cloud.skel.service;

import java.util.List;

import com.cepheid.cloud.skel.exception.ResourceNotFoundException;
import com.cepheid.cloud.skel.model.Item;
import com.cepheid.cloud.skel.model.ItemRequest;

public interface ItemService {

	public Item getItem(Long id) throws ResourceNotFoundException;

	public Item createNewItem(ItemRequest itemRequest);

	public Item update(Item item) throws ResourceNotFoundException;

	public void deleteItem(Long itemId) throws ResourceNotFoundException;

	public List<Item> findAll();
}
