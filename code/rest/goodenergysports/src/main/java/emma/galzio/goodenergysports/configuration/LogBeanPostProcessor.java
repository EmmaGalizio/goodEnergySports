package emma.galzio.goodenergysports.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

//@Component
public class LogBeanPostProcessor implements BeanPostProcessor {

    private Logger logger;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        if(logger == null) logger = LoggerFactory.getLogger(this.getClass());
        logger.info(String.format("Bean instantiated with name %s and class %s", beanName, bean.getClass().getSimpleName()));
        return bean;
    }
}
