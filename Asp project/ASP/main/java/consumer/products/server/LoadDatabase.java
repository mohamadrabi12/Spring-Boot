package consumer.products.server;

import consumer.products.server.order.Order;
import consumer.products.server.order.OrderRepo;
import consumer.products.server.product.Product;
import consumer.products.server.product.ProductDAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner LoadDatabase(ProductDAL productsDB){
        return args -> {
            log.info("logging" +
                    productsDB.save
                            (new Product("samsung", "phone", 699.0,"a")));
            log.info("logging" +
                    productsDB.save
                            (new Product("nokia", "Cellular", 200,"a")));
            log.info("logging" +
                    productsDB.save
                            (new Product("HP", "printer", 420,"a")));
        };
    }

    @Bean
    CommandLineRunner LoadDatabase2 (OrderRepo orderDB){
        return args -> {
            log.info("logging" +
                    orderDB.save
                            (new Order(LocalDate.now(),"Order 1",25.0, List.of())));

        };
    }
}
