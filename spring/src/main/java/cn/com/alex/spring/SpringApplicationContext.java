package cn.com.alex.spring;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.util.Objects;

@Slf4j
public class SpringApplicationContext {

    private Class<?> clazz;

    public SpringApplicationContext(Class<?> clazz) {
        this.clazz = clazz;
        if (clazz.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan annotation = clazz.getAnnotation(ComponentScan.class);
            String path = annotation.value();
            path = path.replace(".", "/");
            log.info(path);
            ClassLoader classLoader = SpringApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            File file = new File(resource.getFile());
            scanFiles(file, classLoader);
            System.out.println("tst");
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
        System.out.println(absolutePath);
        try {
            Class<?> aClass = classLoader.loadClass(absolutePath);
            if (aClass.isAnnotationPresent(ComponentScan.class)) {
                System.out.println(aClass);
                //判断是单例才去创建
                if (aClass.isAnnotationPresent(Scope.class)) {

                } else {

                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getBean(String beanName) {
        return null;
    }
}
