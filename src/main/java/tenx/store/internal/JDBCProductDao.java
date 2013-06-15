package tenx.store.internal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import tenx.store.model.Product;

@Repository
public class JDBCProductDao implements ProductDao {

	@Override
	public Product findById(Connection connection, Long id) {
		try {
			PreparedStatement ps = connection.prepareStatement("select * from products where id = ?");
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Product p = new Product();
				p.setId(rs.getLong("id"));
				p.setName(rs.getString("name"));
				p.setPrice(rs.getBigDecimal("price", 2));
				p.setAvailableQuantity(rs.getInt("available_quantity"));
				return p;
			} else {
				return null;
			}
		} catch (SQLException sqe) {
			throw new RuntimeException("Error fetching product", sqe);
		}
	}

	@Override
	public void update(Connection connection , Product product) {
		try {
			PreparedStatement ps = connection.prepareStatement("update products set name=?, price=?, available_quantity=? where id = ?");
			ps.setString(1, product.getName());
			ps.setBigDecimal(2, product.getPrice());
			ps.setLong(3, product.getAvailableQuantity());
			ps.setLong(4, product.getId());
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException("Error updating product", sqe);
		}
	}

}
