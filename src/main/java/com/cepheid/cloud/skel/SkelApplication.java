package com.cepheid.cloud.skel;

import java.util.stream.Stream;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.cepheid.cloud.skel.controller.ItemController;
import com.cepheid.cloud.skel.model.Description;
import com.cepheid.cloud.skel.model.Item;
import com.cepheid.cloud.skel.model.State;
import com.cepheid.cloud.skel.repository.DescriptionRepository;
import com.cepheid.cloud.skel.repository.ItemRepository;


@SpringBootApplication(scanBasePackageClasses = { ItemController.class, SkelApplication.class })
@EnableJpaRepositories(basePackageClasses = { ItemRepository.class, DescriptionRepository.class })
public class SkelApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkelApplication.class, args);
	}

	@Bean
	ApplicationRunner initItems(ItemRepository repository, DescriptionRepository descriptionRepository) {
		return args -> {
			Stream.of("Lord of the rings", "Hobbit", "Silmarillion", "Unfinished Tales and The History of Middle-earth")
					.forEach(name -> {
						Item item = new Item(name, State.valid);
						repository.save(item);
						descriptionRepository.save(new Description("Author", "J. R. R. Tolkien", item));
					});
			for (Item c : repository.findAll()) {
				System.out.println(c.toString());
			}
		};
	}

}
