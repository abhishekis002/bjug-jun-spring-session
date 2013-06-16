package tenx.store.internal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import tenx.store.infra.DataSourceUtils;
import tenx.store.model.Product;

@Repository
public class JDBCProductDao implements ProductDao {
	
	JDBCTemplate template = new JDBCTemplate();
	
	private final class ProductRowMapper implements Rowmapper<Product> {
		@Override
		public Product mapRow(ResultSet rs) throws SQLException {
			Product p = new Product();
			p.setId(rs.getLong("id"));
			p.setName(rs.getString("name"));
			p.setPrice(rs.getBigDecimal("price"));
			p.setAvailableQuantity(rs.getInt("available_quantity"));
			return p;
		}
	}

	interface Rowmapper<T> {
		T mapRow(ResultSet rs) throws SQLException;
	}

	@Override
	public Product findById(Long id) {
		return template.query(id, "select * from products where id = ?",
				new ProductRowMapper(), id);
	}

	

	@Override
	public void update(Product product) {
		try {
			Connection connection = DataSourceUtils.get();
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
