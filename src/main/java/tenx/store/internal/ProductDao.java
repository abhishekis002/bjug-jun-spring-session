package tenx.store.internal;

import java.sql.Connection;

import tenx.store.model.Product;

public interface ProductDao {
	Product findById(Connection connection, Long id);
	void update(Connection connection, Product product);
}
