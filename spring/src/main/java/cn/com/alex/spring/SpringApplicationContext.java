package cn.com.alex.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SpringApplicationContext {

    private Class<?> clazz;

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private Map<String,Object> singletonObjects = new HashMap<>();

    private List<BeanPostProcessor> beanPostProcessorList = new LinkedList<>();

    public SpringApplicationContext(Class<?> clazz) throws Exception {
        this.clazz = clazz;
        //扫描
        scan(clazz);

        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if ("singleton".equals(beanDefinition.getScope())) {
                //创建bean并属性赋值
                Object bean = createBean(beanName,beanDefinition);

                //缓存单例池
                singletonObjects.put(beanName,bean);
            }
        }
    }

    private Object createBean(String beanName, BeanDefinition beanDefinition) throws Exception {
        Class clazz = beanDefinition.getType();
        Object bean = null;
        bean = clazz.getConstructor().newInstance();
        Field[] fields = bean.getClass().getFields();
        //依赖注入
        for (Field field : fields) {
            //属性赋值
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                field.set(bean,getBean(field.getName()));
            }
        }

        //BeanPostProcessor
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            beanPostProcessor.postProcessBeforeInitialization(bean,beanName);
        }


        //InitializingBean
        if (bean instanceof InitializingBean) {
           ((InitializingBean) bean).afterPropertiesSet();
        }

        //beanNameAware，beanFactoryAware,ApplicationContextAware


        //BeanPostProcessor
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            beanPostProcessor.postProcessAfterInitialization(bean,beanName);
        }

        return bean;
    }

    private void scan(Class<?> clazz) {
        if (clazz.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan annotation = clazz.getAnnotation(ComponentScan.class);
            String path = annotation.value();
            path = path.replace(".", "/");
            log.info(path);
            ClassLoader classLoader = SpringApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            File file = new File(resource.getFile());
            scanFiles(file, classLoader);
        }
    }

    public void scanFiles(File file, ClassLoader classLoader) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                String absolutePath = f.getAbsolutePath();
                if (f.isDirectory()) {
                    // 递归调用
                    scanFiles(f, classLoader);
                    continue;
                }
                // 处理文件逻辑
                processFile(absolutePath, classLoader);
            }
        }
    }

    private void processFile(String absolutePath, ClassLoader classLoader) {
        absolutePath = absolutePath.substring(absolutePath.indexOf("cn"), absolutePath.indexOf(".class"));
        absolutePath = absolutePath.replace("/", ".");
        try {
            Class<?> aClass = classLoader.loadClass(absolutePath);
            if (aClass.isAnnotationPresent(Component.class)) {

                //如果实现了BeanPostProcessor
                if (BeanPostProcessor.class.isAssignableFrom(aClass)) {
                    BeanPostProcessor beanPostProcessor =(BeanPostProcessor) clazz.getConstructor().newInstance();
                    beanPostProcessorList.add(beanPostProcessor);
                }

                BeanDefinition beanDefinition = new BeanDefinition();
                Component annotation1 = aClass.getAnnotation(Component.class);
                String beanName = annotation1.value();
                beanDefinition.setType(aClass);
                //判断是单例才去创建
                if (aClass.isAnnotationPresent(Scope.class)) {
                    Scope annotation = aClass.getAnnotation(Scope.class);
                    String value = annotation.value();
                    beanDefinition.setScope(value);
                } else {
                    beanDefinition.setScope("singleton");
                }
                beanDefinitionMap.put(beanName, beanDefinition);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getBean(String beanName) throws Exception {
        if (!beanDefinitionMap.containsKey(beanName)) {
            throw new NoSuchBeanDefinitionException("没有找到bean");
        }
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if ("singleton".equals(beanDefinition.getScope())) {
            Object singletonBean = singletonObjects.get(beanName);
            if (singletonBean == null) {
                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName,bean);
            }
            return singletonBean;
        } else {
            Object bean = createBean(beanName, beanDefinition);
            return bean;
        }
    }
}
