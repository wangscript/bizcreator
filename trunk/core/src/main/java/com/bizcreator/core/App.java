package com.bizcreator.core;

import com.bizcreator.core.session.impl.BizModelSession;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("spring-context-core.xml");

        System.out.println(java.util.Arrays.toString(ac.getBeanDefinitionNames()));

        BizModelSession dao = (BizModelSession) ac.getBean("bizModelDao");

        Map map = dao.findById("10000");
        
    }
}
