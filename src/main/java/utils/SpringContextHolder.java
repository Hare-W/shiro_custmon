package utils;


import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.xml.validation.Validator;

@Service
public class SpringContextHolder implements ApplicationContextAware {
    public static ApplicationContext applicationContext = null;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }
    public static <T> T getBean(Class<T> tClass){
        Validate.validState(applicationContext != null,"applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder");
        return applicationContext.getBean(tClass);
    }
}
