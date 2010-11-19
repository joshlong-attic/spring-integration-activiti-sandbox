package com.joshlong.activiti.coordinator.aop;

import com.joshlong.activiti.coordinator.annotations.ActivitiState;
import org.aopalliance.aop.Advice;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This class handles advising beans that have the {@link com.joshlong.activiti.coordinator.annotations.ActivitiState} annotation
 *
 * @author Josh Long
 * @see org.springframework.aop.support.AbstractPointcutAdvisor
 * @since 1.0
 */
public class ActivitiStateAnnotationAdvisor extends AbstractPointcutAdvisor /*implements BeanFactoryAware*/ {
    private volatile Pointcut pointcut;

    private volatile Set<Class<? extends Annotation>> stateAnnotationTypes;

    private volatile ActivitiStateDispatchingInterceptor interceptor;

    public ActivitiStateAnnotationAdvisor(Class<? extends Annotation>... publisherAnnotationTypes) {
        this.stateAnnotationTypes = new HashSet<Class<? extends Annotation>>(Arrays.asList(publisherAnnotationTypes));
        this.interceptor = new ActivitiStateDispatchingInterceptor(/* metadata source */);
        this.pointcut = buildPointcut();
    }

    @SuppressWarnings("unchecked")
    public ActivitiStateAnnotationAdvisor() {
        this(ActivitiState.class);
    }

    public Advice getAdvice() {
        return this.interceptor;
    }

    public Pointcut getPointcut() {
        return pointcut;
    }

    private Pointcut buildPointcut() {
        ComposablePointcut result = null;
        for (Class<? extends Annotation> at : this.stateAnnotationTypes) {
            Pointcut cpc = new MetaAnnotationMatchingPointcut(at, true);
            Pointcut mpc = new MetaAnnotationMatchingPointcut(null, at);
            if (result == null) {
                result = new ComposablePointcut(cpc).union(mpc);
            } else {
                result.union(cpc).union(mpc);
            }
        }
        return result;
    }


}

class MetaAnnotationMethodMatcher extends AnnotationMethodMatcher {

    private final Class<? extends Annotation> annotationType;

    public MetaAnnotationMethodMatcher(Class<? extends Annotation> annotationType) {
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

class MetaAnnotationMatchingPointcut implements Pointcut {

    private final ClassFilter classFilter;

    private final MethodMatcher methodMatcher;


    public MetaAnnotationMatchingPointcut(Class<? extends Annotation> classAnnotationType, boolean checkInherited) {
        this.classFilter = new AnnotationClassFilter(classAnnotationType, checkInherited);
        this.methodMatcher = MethodMatcher.TRUE;
    }


    public MetaAnnotationMatchingPointcut(Class<? extends Annotation> classAnnotationType, Class<? extends Annotation> methodAnnotationType) {

        Assert.isTrue(classAnnotationType != null || methodAnnotationType != null,
                "Either class annotation type or method annotation type needs to be specified (or both!)");

        if (classAnnotationType != null) {
            this.classFilter = new AnnotationClassFilter(classAnnotationType);
        } else {
            this.classFilter = ClassFilter.TRUE;
        }

        if (methodAnnotationType != null) {
            this.methodMatcher = new MetaAnnotationMethodMatcher(methodAnnotationType);
        } else {
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
