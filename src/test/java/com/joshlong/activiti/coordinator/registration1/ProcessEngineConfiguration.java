package com.joshlong.activiti.coordinator.registration1;

import org.activiti.engine.DbSchemaStrategy;
import org.activiti.engine.impl.cfg.spring.ProcessEngineFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @author Josh Long
 * @since 1.0
 */
@Configuration
public class ProcessEngineConfiguration {

	protected Resource[] processResources() {
		return new Resource[]{new ClassPathResource("processes/registration.bpmn20.xml")};
	}

	protected String databaseUrl() {
		return "jdbc:h2:tcp://localhost/~/activiti_example4";
	}

	@Bean
	public DataSource activitiDataSource() {
		return new SimpleDriverDataSource(
				new org.h2.Driver(), databaseUrl(), "sa", "");
	}

	@Bean
	public DataSource dataSource() {
		return new TransactionAwareDataSourceProxy(activitiDataSource());
	}

	@Bean
	public PlatformTransactionManager platformTransactionManager() {
		return new DataSourceTransactionManager(this.dataSource());
	}

	@Bean
	public ProcessEngineFactoryBean processEngineFactoryBean() {
		
		ProcessEngineFactoryBean pe = new ProcessEngineFactoryBean();
		pe.setDataSource(this.dataSource());
		pe.setTransactionManager(this.platformTransactionManager());
		pe.setDbSchemaStrategy(DbSchemaStrategy.CHECK_VERSION);
		Resource [] resources = processResources();

		for(Resource resource :resources) System.out.println( resource + "");

		pe.setDeploymentResources( resources );
		return pe;
	}
}
