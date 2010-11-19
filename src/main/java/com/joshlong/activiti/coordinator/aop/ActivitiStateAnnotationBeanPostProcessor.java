package com.joshlong.activiti.coordinator.aop;

import com.joshlong.activiti.coordinator.annotations.*;
import com.joshlong.activiti.coordinator.aop.registry.ActivitiStateHandlerRegistration;
import com.joshlong.activiti.coordinator.aop.registry.ActivitiStateHandlerRegistry;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * the idea is that this bean post processor is responsible for registering all beans
 * that have the {@link com.joshlong.activiti.coordinator.annotations.ActivitiState} annotation.
 *
 * @author Josh Long
 * @see org.springframework.integration.aop.PublisherAnnotationBeanPostProcessor
 */
public class ActivitiStateAnnotationBeanPostProcessor implements BeanPostProcessor, BeanClassLoaderAware, BeanFactoryAware, InitializingBean, Ordered {

    // registration
    private volatile ActivitiStateHandlerRegistry registry;

    private volatile int order = Ordered.LOWEST_PRECEDENCE;

    private volatile BeanFactory beanFactory;

    private volatile ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    public int getOrder() {
        return this.order;
    }

    public void afterPropertiesSet() {
        Assert.notNull(this.beanClassLoader, "beanClassLoader must not be null");
        Assert.notNull(this.beanFactory, "beanFactory must not be null");
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }


    public void setRegistry(ActivitiStateHandlerRegistry registry) {
        this.registry = registry;
    }

    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {

        // first sift through and get all the methods
        // then get all the annotations
        // then build the metadata and register the metadata

        final Class<?> targetClass = AopUtils.getTargetClass(bean);
        final ActivitiHandler handler = targetClass.getAnnotation(ActivitiHandler.class);


        ReflectionUtils.doWithMethods(targetClass, new ReflectionUtils.MethodCallback() {
            @SuppressWarnings("unchecked")
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {

                ActivitiState activitiState = AnnotationUtils.getAnnotation(method, ActivitiState.class);

                String processName = handler.processName();

                if (StringUtils.hasText(activitiState.processName()))
                    processName = activitiState.processName();

                String stateName = activitiState.stateName();
                if (!StringUtils.hasText(stateName))
                    stateName = activitiState.value();

                Assert.notNull(stateName, "You must provide a stateName!");
                Map<Integer, String> vars = new HashMap<Integer, String>();
                Annotation[][] paramAnnotationsArray = method.getParameterAnnotations();

                int ctr = 0;
                int pvMapIndex = -1;
                int procIdIndex = -1;
                for (Annotation[] paramAnnotations : paramAnnotationsArray) {
                    ctr += 1;
                    for (Annotation pa : paramAnnotations) {
                        if (pa instanceof ProcessVariable) {
                            ProcessVariable pv = (ProcessVariable) pa;
                            String pvName = pv.value();
                            vars.put(ctr, pvName);
                        } else if (pa instanceof ProcessVariables) {
                            pvMapIndex = ctr;
                        } else if (pa instanceof ProcessId) {
                            procIdIndex = ctr;
                        }
                    }
                }

                ActivitiStateHandlerRegistration registration = new ActivitiStateHandlerRegistration(
                        vars, method, bean, stateName, beanName, pvMapIndex, procIdIndex, processName);
                System.out.println(registration + "");
                registry.registerActivitiStateHandler(registration);


            }
        }, new ReflectionUtils.MethodFilter() {
            public boolean matches(Method method) {
                return null != AnnotationUtils.getAnnotation(method, ActivitiState.class);
            }
        });


        return bean;
    }

}
