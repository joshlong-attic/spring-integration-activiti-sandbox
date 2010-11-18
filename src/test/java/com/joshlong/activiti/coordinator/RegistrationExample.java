package com.joshlong.activiti.coordinator;

import com.joshlong.activiti.coordinator.registration1.MyStartHandler;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Josh Long
 *
 */
public class RegistrationExample {

	public static void main (String[] args) throws Throwable {

		ClassPathXmlApplicationContext cax = new ClassPathXmlApplicationContext(
					"com/joshlong/activiti/coordinator/registration1.xml") ;

        MyStartHandler myStartHandler =cax.getBean(MyStartHandler.class);
        myStartHandler.confirm();

	}
}

