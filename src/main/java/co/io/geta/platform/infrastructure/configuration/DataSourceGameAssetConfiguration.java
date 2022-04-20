package co.io.geta.platform.infrastructure.configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import co.io.geta.platform.crosscutting.constants.GetaConstants;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = GetaConstants.ENTITY_GAME_ASSET_MANAGER_FACTORY, transactionManagerRef = GetaConstants.TRANSACTION_MANAGER_GAME_ASSET, basePackages = {
		GetaConstants.BASE_PACKAGES_GAME_ASSET })
public class DataSourceGameAssetConfiguration {

	@Value(GetaConstants.DATASOURCE_GAME_ASSET)
	private String conecction;

	@Bean(name = GetaConstants.NAME_BEAN_DATA_SOURCE_GAME_ASSET)
	public DataSource dataSource() {
		HikariDataSource ds = (HikariDataSource) DataSourceBuilder.create().url(conecction).build();
		ds.setConnectionTestQuery("SELECT 1");
		return ds;
	}

	@Bean(name = GetaConstants.ENTITY_GAME_ASSET_MANAGER_FACTORY)
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier(GetaConstants.NAME_BEAN_DATA_SOURCE_GAME_ASSET) DataSource dataSource) {
		return builder.dataSource(dataSource).packages(GetaConstants.ENTITY_PACKAGES_GAME_ASSET)
				.persistenceUnit(GetaConstants.PERSISTENCE_UNIT_GAME_ASSET).build();
	}

	@Bean(name = GetaConstants.TRANSACTION_MANAGER_GAME_ASSET)
	public PlatformTransactionManager transactionManager(
			@Qualifier(GetaConstants.ENTITY_GAME_ASSET_MANAGER_FACTORY) EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
