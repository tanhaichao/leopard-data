package io.leopard.jdbc.test;

import org.junit.Test;

public class H2DataSourceTest {

	// final String maxPoolSize = element.getAttribute("maxPoolSize");
	// final String port = element.getAttribute("port");
	//
	// BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(H2DataSource.class);
	//
	// builder.addPropertyValue("jdbcId", jdbcId);
	// builder.addPropertyValue("user", "${" + name + ".jdbc.username}");
	// builder.addPropertyValue("password", "${" + name + ".jdbc.password}");
	// builder.addPropertyValue("driverClass", "${" + name + ".jdbc.driverClass}");
	//
	// if (StringUtils.isNotEmpty(port)) {
	// builder.addPropertyValue("port", port);
	// }
	// if (StringUtils.isNotEmpty(maxPoolSize)) {
	// builder.addPropertyValue("maxPoolSize", maxPoolSize);
	// }
	//
	// builder.setScope(BeanDefinition.SCOPE_SINGLETON);
	// builder.setLazyInit(true);
	// builder.setInitMethodName("init");
	// builder.setDestroyMethodName("destroy");

	@Test
	public void rsyncServerDataToLocal() {
		H2DataSource dataSource = new H2DataSource();
		dataSource.setJdbcId("jdbc");
		dataSource.setUser("example");
		dataSource.setPassword("leopard");
		dataSource.setUrl("jdbc:mysql://jdbc.h2.test:3306/example?useUnicode=true&characterEncoding=UTF8");
		dataSource.setDriverClass("org.gjt.mm.mysql.Driver");
		dataSource.init();

		RsyncData.registerH2DataSource(dataSource);
		RsyncData.rsyncServerDataToLocal();

		dataSource.destroy();
	}

}