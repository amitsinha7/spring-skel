package com.cepheid.cloud.skel;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.cepheid.cloud.skel.model.Item;
import com.cepheid.cloud.skel.repository.ItemRepository;

@RunWith(SpringRunner.class)
public class ItemControllerTest extends TestBase {

	@Autowired
	ItemRepository itemRepository;

	@Test
	public void testGetItems() throws Exception {
		Builder itemController = getBuilder("/app/api/1.0/items");
		Collection<Item> items = itemController.get(new GenericType<Collection<Item>>() {
		});
		assertEquals("Item Size Receivied Via URL Must be equal To Item Size Present In Repository", items.size(), itemRepository.findAll().size());
	}
}
