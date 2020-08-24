package com.cepheid.cloud.skel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
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

	@Value("classpath:testdata/itemRequestFile.json")
	private Resource itemRequestFile;

	@Value("classpath:testdata/itemUpdateFile.json")
	private Resource itemUpdateFile;

	@Value("classpath:testdata/itemRequestUniqueKeyViolationFile.json")
	private Resource itemRequestUniqueKeyViolationFile;

	@Value("classpath:testdata/itemUniqueKeyViolationFile.json")
	private Resource itemUniqueKeyViolationFile;

	@Value("classpath:testdata/itemForeignKeyViolationFile.json")
	private Resource itemForeignKeyViolationFile;

	@Value("classpath:testdata/itemRequestBadRequestFile.json")
	private Resource itemRequestBadRequestFile;

	@Value("classpath:testdata/itemBadRequestFile.json")
	private Resource itemBadRequestFile;

	@Autowired
	ItemRepository itemRepository;

	@Test
	public void testgetAllItems() throws Exception {
		LOGGER.info("testgetAllItems()");
		Builder itemController = getBuilder("/app/api/1.0/items");
		ItemResponseDTO response = itemController.get(new GenericType<ItemResponseDTO>() {
		});
		List<Item> items = itemRepository.findAll();
		assertNotNull(response);
		assertEquals("Size of Items from DB and Request must be same", response.getItems().size(), items.size());
		assertEquals("Items  Must be equal from Request and DB ", items, response.getItems());
	}

	@Test
	public void getItemById() throws Exception {
		LOGGER.info("testgetAllItems()");
		Builder itemController = getBuilder("/app/api/1.0/items/1");
		ItemResponseDTO response = itemController.get(new GenericType<ItemResponseDTO>() {
		});
		Optional<Item> item = itemRepository.findById((long) 1);
		assertNotNull(response);
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
		ItemResponseDTO response = itemController.post(json, new GenericType<ItemResponseDTO>() {
		});
		Optional<Item> item = itemRepository.findById(response.getItem().getId());
		int sizeAfterItemCreated = itemRepository.findAll().size();
		assertNotNull(response);
		assertEquals("Item Size will be change after adding the a new item ", sizeBeforeItemCreated + 1,
				sizeAfterItemCreated);
		assertEquals("Created Item By Id Must be equal Response Item Id", response.getItem().getId(),
				item.get().getId());
		assertEquals("Created and Extrated Item Items Must be equal", response.getItem(), item.get());
	}

	@Test
	public void updateItem() throws Exception {
		LOGGER.info("updateItem()");
		Builder itemController = getBuilder("/app/api/1.0/items/update");
		String itemRequest = testResource(itemUpdateFile);
		Entity<String> json = Entity.json(itemRequest);
		ItemResponseDTO response = itemController.post(json, new GenericType<ItemResponseDTO>() {
		});
		Optional<Item> itemAfterUpdate = itemRepository.findById((long) 1);
		assertNotNull(response);
		assertEquals("Updated and Extracted Item Items Must be equal", response.getItem(), itemAfterUpdate.get());
	}

	@Test
	public void deleteItemById() throws Exception {
		LOGGER.info("deleteItemById()");
		Builder itemController = getBuilder("/app/api/1.0/items/delete/3");
		ItemResponseDTO response = itemController.get(new GenericType<ItemResponseDTO>() {
		});
		assertNotNull(response);
		assertTrue("Item Deleted Successfully",
				response.getErrorInfo().getErrorMessage().equals("Deleted Item Successfully .."));
	}

	@Test
	public void createItemUniqueKeyViolation() throws Exception {
		LOGGER.info("createItemUniqueKeyViolation()");
		Builder itemController = getBuilder("/app/api/1.0/items/create");
		String itemRequest = testResource(itemRequestUniqueKeyViolationFile);
		Entity<String> json = Entity.json(itemRequest);
		ItemResponseDTO response = itemController.post(json, new GenericType<ItemResponseDTO>() {
		});
		assertNotNull(response);
		assertTrue("Create Item UniqueKey Violation", response.getErrorInfo().getErrorMessage()
				.equals("Error - Unique Key Violation!! -  Foreign Key Violation"));
	}

	@Test
	public void updateItemUniqueKeyViolation() throws Exception {
		LOGGER.info("updateItemUniqueKeyViolation()");
		Builder itemController = getBuilder("/app/api/1.0/items/update");
		String itemRequest = testResource(itemUniqueKeyViolationFile);
		Entity<String> json = Entity.json(itemRequest);
		ItemResponseDTO response = itemController.post(json, new GenericType<ItemResponseDTO>() {
		});
		assertNotNull(response);
		assertTrue("Update Item Unique Violation", response.getErrorInfo().getErrorMessage()
				.equals("Error - Unique Key Violation!! -  Foreign Key Violation"));
	}

	@Test
	public void updateItemForeignKeyViolation() throws Exception {
		LOGGER.info("updateItemForeignKeyViolation()");
		Builder itemController = getBuilder("/app/api/1.0/items/update");
		String itemRequest = testResource(itemForeignKeyViolationFile);
		Entity<String> json = Entity.json(itemRequest);
		ItemResponseDTO response = itemController.post(json, new GenericType<ItemResponseDTO>() {
		});
		assertNotNull(response);
		assertTrue("Update Item Foreign Violation", response.getErrorInfo().getErrorMessage()
				.equals("Error - Unique Key Violation!! -  Foreign Key Violation"));
	}

	@Test
	public void updateItemBadRequest() throws Exception {
		LOGGER.info("updateItemBadRequest()");
		Builder itemController = getBuilder("/app/api/1.0/items/update");
		String itemRequest = testResource(itemBadRequestFile);
		Entity<String> json = Entity.json(itemRequest);
		ItemResponseDTO response = itemController.post(json, new GenericType<ItemResponseDTO>() {
		});
		assertNotNull(response);
		assertTrue("Bad Request For Update Existing",
				response.getErrorInfo().getErrorMessage().equals("Bad request, please check request."));
	}

	@Test
	public void createItemRequestBadRequest() throws Exception {
		LOGGER.info("createItemRequestBadRequest()");
		Builder itemController = getBuilder("/app/api/1.0/items/update");
		String itemRequest = testResource(itemRequestBadRequestFile);
		Entity<String> json = Entity.json(itemRequest);
		ItemResponseDTO response = itemController.post(json, new GenericType<ItemResponseDTO>() {
		});
		assertNotNull(response);
		assertTrue("Bad Request For Create Existing",
				response.getErrorInfo().getErrorMessage().equals("Bad request, please check request."));
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
