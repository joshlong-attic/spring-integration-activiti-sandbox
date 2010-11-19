package com.joshlong.activiti.coordinator.annotations;


import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProcessId {
}
