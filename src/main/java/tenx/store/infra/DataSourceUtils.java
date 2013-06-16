package tenx.store.infra;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class DataSourceUtils {
	
	private static ThreadLocal<Connection> connection = new ThreadLocal<Connection>();
	
	public static void open(DataSource dataSource) throws SQLException {
		connection.set(dataSource.getConnection());
	}
	
	public static Connection get() {
		return connection.get();
	}
	
	public static void close() throws SQLException{
		connection.get().close();
		connection.set(null);
	}

}
