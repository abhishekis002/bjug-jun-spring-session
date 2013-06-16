package tenx.store.internal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tenx.store.infra.DataSourceUtils;
import tenx.store.internal.JDBCProductDao.Rowmapper;

public class JDBCTemplate {
	public <T> T query(Long id, String sql, Rowmapper<T> mapper, Object... params) {
		try {
			Connection connection = DataSourceUtils.get();
			
			PreparedStatement ps = connection.prepareStatement(sql);
			
			for(int i=0;i<params.length;i++) {
				ps.setObject(i+1, params[i]);
			}
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return (T) mapper.mapRow(rs);
			} else {
				return null;
			}
		} catch (SQLException sqe) {
			throw new RuntimeException("Error fetching product", sqe);
		}
	}
	
}
