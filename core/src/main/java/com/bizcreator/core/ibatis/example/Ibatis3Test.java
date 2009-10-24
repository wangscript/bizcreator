package com.bizcreator.core.ibatis.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Ibatis3Test {
    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext(
                "com/example/spring/beans.xml");

        System.out.println(java.util.Arrays.toString(ac.getBeanDefinitionNames()));

        TestFinder finder = (TestFinder) ac.getBean("TestFinder");

        for (int i = 1; i <= 10; i++) {
            String test = finder.findTest(i);
            System.out.println(test);
        }
    }
}
