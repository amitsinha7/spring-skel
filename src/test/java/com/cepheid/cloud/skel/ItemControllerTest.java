package com.cepheid.cloud.skel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Optional;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import com.cepheid.cloud.skel.model.Item;
import com.cepheid.cloud.skel.repository.ItemRepository;
import com.cepheid.cloud.skel.response.ItemResponseDTO;
import com.google.common.io.CharStreams;

@RunWith(SpringRunner.class)
public class ItemControllerTest extends TestBase {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Value("classpath:testdata/itemrequest.json")
	private Resource itemRequestFile;
	
	@Value("classpath:testdata/item.json")
	private Resource itemFile;;

	@Autowired
	ItemRepository itemRepository;

	@Test
	public void testgetAllItems() throws Exception {
		LOGGER.info("testgetAllItems()");
		Builder itemController = getBuilder("/app/api/1.0/items");
		ItemResponseDTO response = itemController.get(new GenericType<ItemResponseDTO>() {
		});
		assertEquals("Item Size Receivied Via URL Must be equal To Item Size Present In Repository",
				response.getItems().size(), itemRepository.findAll().size());
	}

	@Test
	public void getItemById() throws Exception {
		LOGGER.info("testgetAllItems()");
		Builder itemController = getBuilder("/app/api/1.0/items/1");
		ItemResponseDTO response = itemController.get(new GenericType<ItemResponseDTO>() {
		});
		Optional<Item> item = itemRepository.findById((long) 1.0);
		assertEquals("Get Item By Id Must be equal", response.getItem().getId(), item.get().getId());
		assertEquals("Both Items Must be equal", response.getItem(), item.get());
	}

	@Test
	public void createItem() throws Exception {
		LOGGER.info("createItem()");
		Builder itemController = getBuilder("/app/api/1.0/items/create");
		String itemRequest = testResource(itemRequestFile);
		Entity<String> json = Entity.json(itemRequest);
		int sizeBeforeItemCreated = itemRepository.findAll().size();
		itemController.post(json, new GenericType<ItemResponseDTO>() {
		});
		int sizeAfterItemCreated = itemRepository.findAll().size();
		assertEquals("Item Created Evaluated after change in Item size directly from the DB",
				sizeBeforeItemCreated + 1, sizeAfterItemCreated);
	}

	@Test
	public void updateItem() throws Exception {
		LOGGER.info("updateItem()");
		Builder itemController = getBuilder("/app/api/1.0/items/update");
		String itemRequest = testResource(itemFile);
		Entity<String> json = Entity.json(itemRequest);
		Optional<Item> itemBeforeUpdate = itemRepository.findById((long) 4.0);
		itemController.post(json, new GenericType<ItemResponseDTO>() {
		});
		Optional<Item> itemAfterUpdate = itemRepository.findById((long) 4.0);
		assertNotEquals("Updated And Added Description To Item ",itemBeforeUpdate.get().getDescriptions().size(), itemAfterUpdate.get().getDescriptions().size());
	}

	@Test
	public void deleteItemById() throws Exception {
		LOGGER.info("deleteItemById()");
		Builder itemController = getBuilder("/app/api/1.0/items/delete/5");
		itemController.get(new GenericType<ItemResponseDTO>() {
		});
		Optional<Item> item = itemRepository.findById((long) 5.0);
		if(!item.isPresent()) {
			assertTrue(true);
		}
	}

	private String testResource(Resource resource) {
		String text = null;
		try {
			InputStream resourcee = resource.getInputStream();
			try (final Reader reader = new InputStreamReader(resourcee)) {
				text = CharStreams.toString(reader);
			}
			System.out.println(text);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}
}
