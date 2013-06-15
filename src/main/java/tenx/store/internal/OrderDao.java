package tenx.store.internal;

import java.sql.Connection;

import tenx.store.model.Order;


public interface OrderDao {
	
	Long createOrder(Connection connection, Order order);

}
