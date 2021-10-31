package com.dynamic.property.reader.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

public class DynamicDbPropertiestProcessor implements EnvironmentPostProcessor {
	/**
	 * Name of the custom property source added by this post processor class
	 */
	private static final String PROPERTY_SOURCE_NAME = "databaseProperties";

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

		Map<String, Object> propertySource = new HashMap<>();

		try {

			DataSource ds = DataSourceBuilder.create().username(environment.getProperty("spring.datasource.username"))
					.password(environment.getProperty("spring.datasource.password"))
					.url(environment.getProperty("spring.datasource.url"))

					.driverClassName(environment.getProperty("drivar.class.name")).build();

			Connection connection = ds.getConnection();

			String query = "insert into config (id,key, value, component)" + " values (?,?, ?, ?)";

			setDynamicProperty(connection, query);

			PreparedStatement preparedStatement = connection.prepareStatement("SELECT key,value FROM config");

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				propertySource.put(rs.getString("key"), rs.getString("value"));
				StrSubstitutor strSubstitutor = new StrSubstitutor(propertySource);
				strSubstitutor.replace(rs.getString("value"));
			}

			rs.close();
			preparedStatement.clearParameters();

			preparedStatement.close();
			connection.close();

			environment.getPropertySources().addFirst(new MapPropertySource(PROPERTY_SOURCE_NAME, propertySource));

		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private void setDynamicProperty(Connection connection, String query) throws SQLException {
		PreparedStatement preparedStmt = connection.prepareStatement(query);
		preparedStmt.setInt(1, 1);
		preparedStmt.setString(2, "propertyValues");
		preparedStmt.setString(3,
				"${spring.mail.username}/${spring.mail.password}/${spring.mail.host}/${spring.mail.port}/${mail.transport.protocol}");
		preparedStmt.setString(4, "testing");

		preparedStmt.executeUpdate();

		preparedStmt.setInt(1, 2);
		preparedStmt.setString(2, "spring.mail.username");
		preparedStmt.setString(3, "user1");
		preparedStmt.setString(4, "testing");

		preparedStmt.executeUpdate();
		preparedStmt.setInt(1, 3);
		preparedStmt.setString(2, "spring.mail.password");
		preparedStmt.setString(3, "password");
		preparedStmt.setString(4, "testing");

		preparedStmt.executeUpdate();
		preparedStmt.setInt(1, 4);
		preparedStmt.setString(2, "spring.mail.host");
		preparedStmt.setString(3, "localhost");
		preparedStmt.setString(4, "testing");

		preparedStmt.executeUpdate();
		preparedStmt.setInt(1, 5);
		preparedStmt.setString(2, "spring.mail.port");
		preparedStmt.setString(3, "473");
		preparedStmt.setString(4, "testing");

		preparedStmt.executeUpdate();
		preparedStmt.setInt(1, 6);
		preparedStmt.setString(2, "mail.transport.protocol");
		preparedStmt.setString(3, "smtp");
		preparedStmt.setString(4, "testing");
		preparedStmt.executeUpdate();
	}

}