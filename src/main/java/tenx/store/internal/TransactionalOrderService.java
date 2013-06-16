package tenx.store.internal;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import tenx.store.OrderService;
import tenx.store.infra.DataSourceUtils;
import tenx.store.model.LineItem;
import tenx.store.model.Order;
import tenx.store.model.Product;

public class TransactionalOrderService implements OrderService {
	
	private OrderService delegate;
	private DataSource dataSource;

	TransactionalOrderService(OrderService orderService, DataSource dataSource) {
		this.delegate = orderService;
		this.dataSource = dataSource;
	}

	@Override
	public Long processOrder(Order order) {
		Connection connection = null;
		try {
			
			DataSourceUtils.open(dataSource);
			
			connection = DataSourceUtils.get();
			connection.setAutoCommit(false);
			
			Long orderId = delegate.processOrder(order);

			connection.commit();

			return orderId;
		} catch (Throwable t) {
			try {
				connection.rollback();
			} catch (Exception e) {
			}
			throw new RuntimeException("Error creating order", t);
		} finally {
			if (connection != null) {
				try {
					DataSourceUtils.close();
				} catch (Exception e) {
				}
			}
		}

	}

	@Override
	public List<Long> bulkOrder(List<Order> orders) {
		return delegate.bulkOrder(orders);
	}

}
