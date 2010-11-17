package com.joshlong.activiti.coordinator.aop;

import com.joshlong.activiti.coordinator.annotations.ActivitiState;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.integration.aop.MessagePublishingInterceptor;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * This class handles advising beans that have the AnnnotationState annotation
 *
 * @see org.springframework.aop.support.AbstractPointcutAdvisor
 * @since 1.0
 * @author Josh Long
 *
 */
public class ActivitiStateAnnotationAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

	//we need to tell it which annotation to searc for
	private final Set<Class<? extends Annotation>> stateAnnotationTypes ;

	private   MessagePublishingInterceptor interceptor;


	public ActivitiStateAnnotationAdvisor(Class<? extends Annotation> ... publisherAnnotationTypes) {
		this.stateAnnotationTypes = new HashSet<Class<? extends Annotation>>(
				Arrays.asList( publisherAnnotationTypes));

		/* todo PublisherMetadataSource metadataSource = new MethodAnnotationPublisherMetadataSource(
				this.publisherAnnotationTypes);*/


 //todo		this.interceptor = new MessagePublishingInterceptor(metadataSource);
	}

	@SuppressWarnings("unchecked")
	public ActivitiStateAnnotationAdvisor() {
		this(ActivitiState.class);
	}




	public Advice getAdvice() {
		return null;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

	}

	public Pointcut getPointcut() {
		return null;
	}

	/*

 private final Set<Class<? extends Annotation>> publisherAnnotationTypes;

 private final MessagePublishingInterceptor interceptor;


 public PublisherAnnotationAdvisor(Class<? extends Annotation> ... publisherAnnotationTypes) {
	 this.publisherAnnotationTypes = new HashSet<Class<? extends Annotation>>(Arrays.asList(publisherAnnotationTypes));
	 PublisherMetadataSource metadataSource = new MethodAnnotationPublisherMetadataSource(this.publisherAnnotationTypes);
	 this.interceptor = new MessagePublishingInterceptor(metadataSource);
 }

 @SuppressWarnings("unchecked")
 public PublisherAnnotationAdvisor() {
	 this(Publisher.class);
 }


 public void setDefaultChannel(MessageChannel defaultChannel) {
	 this.interceptor.setDefaultChannel(defaultChannel);
 }

 public void setBeanFactory(BeanFactory beanFactory) {
	 this.interceptor.setChannelResolver(new BeanFactoryChannelResolver(beanFactory));
 }

 public Advice getAdvice() {
	 return this.interceptor;
 }

 public Pointcut getPointcut() {
	 return this.buildPointcut();
 }

 private Pointcut buildPointcut() {
	 ComposablePointcut result = null;
	 for (Class<? extends Annotation> publisherAnnotationType : this.publisherAnnotationTypes) {
		 Pointcut cpc = new MetaAnnotationMatchingPointcut(publisherAnnotationType, true);
		 Pointcut mpc = new MetaAnnotationMatchingPointcut(null, publisherAnnotationType);
		 if (result == null) {
			 result = new ComposablePointcut(cpc).union(mpc);
		 }
		 else {
			 result.union(cpc).union(mpc);
		 }
	 }
	 return result;
 }


 private static class MetaAnnotationMatchingPointcut implements Pointcut {

	 private final ClassFilter classFilter;

	 private final MethodMatcher methodMatcher;


	 */
/**
		 * Create a new MetaAnnotationMatchingPointcut for the given annotation type.
		 * @param classAnnotationType the annotation type to look for at the class level
		 * @param checkInherited whether to explicitly check the superclasses and
		 * interfaces for the annotation type as well (even if the annotation type
		 * is not marked as inherited itself)
		 *//*

		private MetaAnnotationMatchingPointcut(Class<? extends Annotation> classAnnotationType, boolean checkInherited) {
			this.classFilter = new AnnotationClassFilter(classAnnotationType, checkInherited);
			this.methodMatcher = MethodMatcher.TRUE;
		}

		*/
/**
		 * Create a new MetaAnnotationMatchingPointcut for the given annotation type.
		 * @param classAnnotationType the annotation type to look for at the class level
		 * (can be <code>null</code>)
		 * @param methodAnnotationType the annotation type to look for at the method level
		 * (can be <code>null</code>)
		 *//*

		private MetaAnnotationMatchingPointcut(
				Class<? extends Annotation> classAnnotationType, Class<? extends Annotation> methodAnnotationType) {

			Assert.isTrue((classAnnotationType != null || methodAnnotationType != null),
					"Either Class annotation type or Method annotation type needs to be specified (or both)");

			if (classAnnotationType != null) {
				this.classFilter = new AnnotationClassFilter(classAnnotationType);
			}
			else {
				this.classFilter = ClassFilter.TRUE;
			}

			if (methodAnnotationType != null) {
				this.methodMatcher = new MetaAnnotationMethodMatcher(methodAnnotationType);
			}
			else {
				this.methodMatcher = MethodMatcher.TRUE;
			}
		}


		public ClassFilter getClassFilter() {
			return this.classFilter;
		}

		public MethodMatcher getMethodMatcher() {
			return this.methodMatcher;
		}
	}


	private static class MetaAnnotationMethodMatcher extends AnnotationMethodMatcher {

		private final Class<? extends Annotation> annotationType;


		*/
/**
		 * Create a new AnnotationClassFilter for the given annotation type.
		 * @param annotationType the annotation type to look for
		 *//*

		private MetaAnnotationMethodMatcher(Class<? extends Annotation> annotationType) {
			super(annotationType);
			this.annotationType = annotationType;
		}


		@Override
		@SuppressWarnings("rawtypes")
		public boolean matches(Method method, Class targetClass) {
			if (AnnotationUtils.getAnnotation(method, this.annotationType) != null) {
				return true;
			}
			// The method may be on an interface, so let's check on the target class as well.
			Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
			return (specificMethod != method &&
					(AnnotationUtils.getAnnotation(specificMethod, this.annotationType) != null));
		}
	}
*/

}
