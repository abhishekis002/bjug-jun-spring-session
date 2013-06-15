package tenx.store.internal;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tenx.store.OrderService;
import tenx.store.model.LineItem;
import tenx.store.model.Order;
import tenx.store.model.Product;

@Service
public class SimpleOrderService implements OrderService {
	private DataSource dataSource;
	private ProductDao productDao;
	private OrderDao orderDao;

	@Autowired
	public SimpleOrderService(DataSource dataSource, ProductDao productDao,
			OrderDao orderDao) {
		super();
		this.dataSource = dataSource;
		this.productDao = productDao;
		this.orderDao = orderDao;
	}

	public BigDecimal calculateCost(Order order) {
		Connection connection = null;
		try {
			BigDecimal cost = new BigDecimal("0");
			for (LineItem lineItem : order.getItems()) {
				Product p = productDao.findById(connection,
						lineItem.getProductId());
				cost = cost.add(p.getPrice().multiply(
						new BigDecimal(lineItem.getQuantity())));
			}
			return cost;
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception e) {
				}
			}
		}
	}


	public Long processOrder(Order order) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);

			for (LineItem lineItem : order.getItems()) {
				Product p = productDao.findById(connection,
						lineItem.getProductId());
				
				if (p.getAvailableQuantity() < lineItem.getQuantity()) {
					throw new RuntimeException("Insufficient quantity");
				}
				
				p.setAvailableQuantity(p.getAvailableQuantity()
						- lineItem.getQuantity());
				productDao.update(connection, p);
			}
			Long orderId = orderDao.createOrder(connection, order);

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
					connection.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public List<Long> bulkOrder(List<Order> orders) {
		// TODO - this is not transactional, who to fix????
		for (Order o : orders) {
			processOrder(o);
		}
		return new ArrayList<Long>();
	}

}
