package tenx.store.infra;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

import javax.sql.DataSource;

public class TransactionalWrapper {
	
	@SuppressWarnings("unchecked")
	public static <T> T wrap(final T delegate,final DataSource dataSource) {
		return (T) Proxy.newProxyInstance(delegate.getClass().getClassLoader(),
				delegate.getClass().getInterfaces(), 
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method m, Object[] args)
							throws Throwable {
						
						Connection connection = null;
						try {
							
							DataSourceUtils.open(dataSource);
							
							connection = DataSourceUtils.get();
							connection.setAutoCommit(false);
							
							System.out.println("method "  + m.getName() + " called");
							Object retValue = m.invoke(delegate, args);

							connection.commit();
							
							return retValue;
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
				});
		
	}

}
