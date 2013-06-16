package tenx.store.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tenx.store.OrderService;
import tenx.store.infra.DataSourceUtils;
import tenx.store.infra.TransactionalWrapper;
import tenx.store.model.LineItem;
import tenx.store.model.Order;
import tenx.store.model.Product;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:application-config.xml", "classpath:test-infra-config.xml"})
public class SimpleOrderServiceIntegrationTest {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private DataSource dataSource;
		
	
	@Test
	public void testCreateOrder() throws SQLException {
		
		orderService = TransactionalWrapper.wrap(orderService, dataSource);
		
		Order o = new Order();
		
		LineItem item = new LineItem();
		item.setProductId(1l);
		item.setQuantity(5);
		o.addItem(item);
		
		item = new LineItem();
		item.setProductId(2l);
		item.setQuantity(25);
		o.addItem(item);
		
		try {
			orderService.processOrder(o);
			fail("should result in exception");
		} catch(RuntimeException e) {
			// ignore
		}
		
		DataSourceUtils.open(dataSource);	
		Product p = productDao.findById(1l);
		DataSourceUtils.close();
		
		assertEquals(10, p.getAvailableQuantity());
	}
	

}
