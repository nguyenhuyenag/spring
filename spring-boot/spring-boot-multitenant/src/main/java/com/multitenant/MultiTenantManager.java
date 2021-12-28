package com.multitenant;

import static java.lang.String.format;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.exception.InvalidDbPropertiesException;
import com.exception.InvalidTenantIdExeption;

@Configuration
public class MultiTenantManager {

	public static Logger LOG = LoggerFactory.getLogger(MultiTenantManager.class);

	@Value("${spring.datasource.url}")
	private String DATASOURCE_URL;

	@Value("${spring.datasource.username}")
	private String DATASOURCE_USERNAME;

	@Value("${spring.datasource.password}")
	private String DATASOURCE_PASSWORD;

	@Value("${spring.datasource.driver-class-name}")
	private String DATASOURCE_DRIVER_CLASSNAME;

	private final static ThreadLocal<String> currentTenant = new ThreadLocal<>();
	private final static Map<Object, Object> tenantDataSources = new ConcurrentHashMap<>();

	private static DataSourceProperties properties;
	private static AbstractRoutingDataSource multiTenantDataSource;
	private static Function<String, DataSourceProperties> tenantResolver;

	private static final String MSG_RESOLVING_TENANT_ID = "[!] Could not resolve tenant ID '{}'!";
	private static final String MSG_INVALID_TENANT_ID = "[!] DataSource not found for given tenant Id '{}'!";
	private static final String MSG_INVALID_DB_PROPERTIES_ID = "[!] DataSource properties related to the given tenant ('{}') is invalid!";

	@Autowired
	public MultiTenantManager(DataSourceProperties properties) {
		MultiTenantManager.properties = properties;
	}

	private DriverManagerDataSource defaultDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(DATASOURCE_URL);
		dataSource.setDriverClassName(DATASOURCE_DRIVER_CLASSNAME);
		dataSource.setUsername(DATASOURCE_USERNAME);
		dataSource.setPassword(DATASOURCE_PASSWORD);
		return dataSource;
	}

	@Bean
	public DataSource dataSource() {
		multiTenantDataSource = new AbstractRoutingDataSource() {
			@Override
			protected Object determineCurrentLookupKey() {
				return currentTenant.get();
			}
		};
		multiTenantDataSource.setTargetDataSources(tenantDataSources);
		multiTenantDataSource.setDefaultTargetDataSource(defaultDataSource());
		multiTenantDataSource.afterPropertiesSet();
		return multiTenantDataSource;
	}

	private static void setCurrentTenant(String tenantId)
			throws SQLException, TenantNotFoundException, TenantResolvingException {
		if (tenantIsAbsent(tenantId)) {
			if (tenantResolver != null) {
				DataSourceProperties properties;
				try {
					properties = tenantResolver.apply(tenantId);
					LOG.debug("[d] Datasource properties resolved for tenant ID '{}'", tenantId);
				} catch (Exception e) {
					throw new TenantResolvingException(e, "Could not resolve the tenant!");
				}
				String url = properties.getUrl();
				System.out.println("URL:" + url);
				String username = properties.getUsername();
				String password = properties.getPassword();
				handleAddTenant(tenantId, username, password);
			} else {
				throw new TenantNotFoundException(format("Tenant %s not found!", tenantId));
			}
		}
		currentTenant.set(tenantId);
		LOG.debug("[d] Tenant '{}' set as current.", tenantId);
	}

	private static void handleAddTenant(String databasename, String username, String password)
			throws SQLException {
		String url = "jdbc:mysql://localhost:3306/" + databasename + "?useUnicode=true&characterEncoding=utf-8";
		DataSource dataSource = DataSourceBuilder.create() //
				.driverClassName(properties.getDriverClassName()) //
				.url(url) //
				.username(username) //
				.password(password) //
				.build();

		// Check that new connection is 'live'. If not - throw exception
		try (Connection c = dataSource.getConnection()) {
			tenantDataSources.put(databasename, dataSource);
			multiTenantDataSource.afterPropertiesSet();
			LOG.debug("[d] Tenant '{}' added.", databasename);
		}
	}

	private static boolean tenantIsAbsent(String databasename) {
		return !tenantDataSources.containsKey(databasename);
	}

	public static void setTenant(String databasename) {
		try {
			MultiTenantManager.setCurrentTenant(databasename);
		} catch (SQLException e) {
			LOG.error(MSG_INVALID_DB_PROPERTIES_ID, databasename);
			throw new InvalidDbPropertiesException();
		} catch (TenantNotFoundException e) {
			LOG.error(MSG_INVALID_TENANT_ID, databasename);
			throw new InvalidTenantIdExeption();
		} catch (TenantResolvingException e) {
			LOG.error(MSG_RESOLVING_TENANT_ID, databasename);
			throw new InvalidTenantIdExeption();
		}
	}

	public static void addTenant(String databasename, String username, String password) {
		try {
			MultiTenantManager.handleAddTenant(databasename, username, password);
		} catch (SQLException e) {
			LOG.error(MSG_INVALID_DB_PROPERTIES_ID, databasename);
			throw new InvalidDbPropertiesException();
		}
	}

	// private DataSource removeTenant(String tenantId) {
	// Object removedDataSource = tenantDataSources.remove(tenantId);
	// multiTenantDataSource.afterPropertiesSet();
	// return (DataSource) removedDataSource;
	// }

	// private Collection<Object> getTenantList() {
	// return tenantDataSources.keySet();
	// }

}