package tenx.store.internal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import tenx.store.infra.DataSourceUtils;
import tenx.store.model.Order;

@Repository
public class JDBCOrderDao implements OrderDao {

	@Override
	public Long createOrder(Order order) {
		try {
			Connection connection = DataSourceUtils.get();
			PreparedStatement ps = connection
					.prepareStatement("insert into orders (id, cost) values(null, ?)");

			ps.setBigDecimal(1, order.getCost());
			ps.executeUpdate();
			
			CallableStatement cs = connection.prepareCall("CALL IDENTITY()");
			ResultSet rs = cs.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
			return 0L;

		} catch (SQLException sqe) {
			throw new RuntimeException("Error inserting order", sqe);
		}
	}

}
