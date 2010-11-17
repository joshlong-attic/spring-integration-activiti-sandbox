package com.joshlong.activiti.coordinator.aop;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.aop.PublisherAnnotationAdvisor;
import org.springframework.util.ClassUtils;


/**
 *
 * the idea is that this bean post processor is responsible for registering all beans
 * that have the {@link com.joshlong.activiti.coordinator.annotations.ActivitiState} annotation.
 *
 * @author Josh Long
 * @see org.springframework.integration.aop.PublisherAnnotationBeanPostProcessor
 * 
 */
public class ActivitiStateAnnotationBeanPostProcessor extends ProxyConfig
		implements BeanPostProcessor, BeanClassLoaderAware, BeanFactoryAware, InitializingBean, Ordered {

	private volatile MessageChannel defaultChannel;

	private volatile ActivitiStateAnnotationAdvisor advisor;

	private volatile int order = Ordered.LOWEST_PRECEDENCE;

	private volatile BeanFactory beanFactory;

	private volatile ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();


	/**
	 * Set the default channel where Messages should be sent if the annotation
	 * itself does not provide a channel.
	 */
	public void setDefaultChannel(MessageChannel defaultChannel){
		this.defaultChannel = defaultChannel;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = classLoader;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getOrder() {
		return this.order;
	}

	public void afterPropertiesSet(){
		advisor = new ActivitiStateAnnotationAdvisor();
		advisor.setBeanFactory(beanFactory);
// todo 		advisor.setDefaultChannel(defaultChannel);
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
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
			}
			else {
				ProxyFactory proxyFactory = new ProxyFactory(bean);
				// Copy our properties (proxyTargetClass etc) inherited from ProxyConfig.
				proxyFactory.copyFrom(this);
				proxyFactory.addAdvisor(this.advisor);
				return proxyFactory.getProxy(this.beanClassLoader);
			}
		}
		else {
			// cannot apply advisor
			return bean;
		}
	}

}
