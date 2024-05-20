package cn.com.alex.service;

import org.springframework.beans.SimpleTypeConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.io.IOException;
import java.util.Set;

/**
 * @description 类型转化v
 * @author wangzf
 * @date 2024/5/19
 */
public class ConvertService implements ConditionalGenericConverter {
    @Override
    public boolean matches(TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor1) {
        return false;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return null;
    }

    @Override
    public Object convert(Object o, TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor1) {
        return null;
    }

    public static void main(String[] args) throws IOException {
        DefaultConversionService conversionService = new DefaultConversionService();
        SimpleTypeConverter simpleTypeConverter = new SimpleTypeConverter();
        SimpleMetadataReaderFactory simpleMetadataReaderFactory = new SimpleMetadataReaderFactory();
        MetadataReader metadataReader = simpleMetadataReaderFactory.getMetadataReader("");
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        //获取实现接口的名字
        String[] interfaceNames = classMetadata.getInterfaceNames();
        //获取内部类
        String[] memberClassNames = classMetadata.getMemberClassNames();


        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        //获取@Bean修饰的方法
        //ASM:字节码解析，不用jvm的加载之后再解析，直接解析字节码
        Set<MethodMetadata> bean = annotationMetadata.getAnnotatedMethods(Bean.class.getName());
        //不能判断复合注解中被包含的注解，只能判断最外层的@Service包含@Component，下面返回false
        boolean bean1 = annotationMetadata.hasAnnotation("Component");
        //可以判断复合注解中被包含的注解，只能判断最  外层的，下面返回true
        annotationMetadata.hasMetaAnnotation("Component");
    }
}
