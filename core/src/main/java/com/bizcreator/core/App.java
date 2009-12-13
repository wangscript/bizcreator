package com.bizcreator.core;

import com.bizcreator.core.entity.User;
import com.bizcreator.core.session.BizModelManager;
import com.bizcreator.core.session.UserManager;
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
        ApplicationContext ac = new ClassPathXmlApplicationContext("spring-context-core-ibatis.xml");

        System.out.println(java.util.Arrays.toString(ac.getBeanDefinitionNames()));

        BizModelSession dao = (BizModelSession) ac.getBean(BizModelManager.NAME);
        
        //查询
        Map map = dao.findById("10000");
        
        UserManager userMgr = (UserManager) ac.getBean(UserManager.NAME);
        User user = userMgr.findById("000000000001@rhino");
        
        System.out.println(user.toJson());
    }
}
