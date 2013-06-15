package tenx.store;

import java.util.List;

import tenx.store.model.Order;

public interface OrderService {
	
	Long processOrder(Order order);
	
	List<Long> bulkOrder(List<Order> orders);
}
