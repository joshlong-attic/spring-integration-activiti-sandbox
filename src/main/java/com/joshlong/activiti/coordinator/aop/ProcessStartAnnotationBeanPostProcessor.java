package com.joshlong.activiti.coordinator.aop;

import org.activiti.engine.ProcessEngine;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ClassUtils;

/**
 * Proxies beans with methods annotated with {@link com.joshlong.activiti.coordinator.annotations.StartProcess}.
 * If the method is invoked successfully, the process described by the annotaton is created.
 * Parameters passed to the method annotated with {@link com.joshlong.activiti.coordinator.annotations.ProcessVariable}
 * are passed to the business process.
 *
 * @author Josh Long
 * @since 1.0
 */
public class ProcessStartAnnotationBeanPostProcessor extends ProxyConfig implements BeanPostProcessor, InitializingBean {

	private ProcessStartingPointcutAdvisor advisor;

	private volatile ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

	/**
	 * the process engine as created by a {@link org.activiti.spring.ProcessEngineFactoryBean}
	 */
	private ProcessEngine processEngine;

	public void setProcessEngine(ProcessEngine processEngine) {
		this.processEngine = processEngine;
	}

	public void afterPropertiesSet() throws Exception {
		this.advisor = new ProcessStartingPointcutAdvisor(this.processEngine);
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		Class<?> targetClass = AopUtils.getTargetClass(bean);
		if (targetClass == null) {
			return bean;
		}

		if (AopUtils.canApply(this.advisor, targetClass)) {
			if (bean instanceof Advised) {
				((Advised) bean).addAdvisor(this.advisor);
				return bean;
			} else {
				ProxyFactory proxyFactory = new ProxyFactory(bean);
				// Copy our properties (proxyTargetClass etc) inherited from ProxyConfig.
				proxyFactory.copyFrom(this);
				proxyFactory.addAdvisor(this.advisor);
				return proxyFactory.getProxy(this.beanClassLoader);
			}
		} else {
			// cannot apply advisor
			return bean;
		}
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
}
