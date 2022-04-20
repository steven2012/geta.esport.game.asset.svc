package co.io.geta.platform.infrastructure.configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import co.io.geta.platform.crosscutting.constants.GetaConstants;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = GetaConstants.ENTITY_MANAGER_FACTORY, basePackages = {
		GetaConstants.BASE_PACKAGES })
public class DataSourceConfiguration {

	@Value(GetaConstants.DATASOURCE_GAME)
	private String conecction;

	@Primary
	@Bean(name = GetaConstants.NAME_BEAN_DATA_SOURCE)
	public DataSource dataSource() {
		HikariDataSource ds = (HikariDataSource) DataSourceBuilder.create().url(conecction).build();
		ds.setConnectionTestQuery("SELECT 1");
		return ds;
	}

	@Primary
	@Bean(name = GetaConstants.ENTITY_MANAGER_FACTORY)
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier(GetaConstants.NAME_BEAN_DATA_SOURCE) DataSource dataSource) {
		return builder.dataSource(dataSource).packages(GetaConstants.ENTITY_PACKAGES)
				.persistenceUnit(GetaConstants.PERSISTENCE_UNIT).build();
	}

	@Primary
	@Bean(name = GetaConstants.TRANSACTION_MANAGER)
	public PlatformTransactionManager transactionManager(
			@Qualifier(GetaConstants.ENTITY_MANAGER_FACTORY) EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
