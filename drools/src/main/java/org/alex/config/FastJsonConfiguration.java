package org.alex.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class FastJsonConfiguration implements WebMvcConfigurer {

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        // 创建FastJson消息转换器
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

        // 创建FastJson配置
        FastJsonConfig config = new FastJsonConfig();
        // 设置序列化特性，启用writerMapNullValue
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty);

        // 将配置应用到转换器
        converter.setFastJsonConfig(config);

        // 创建HttpMessageConverter列表
        HttpMessageConverter<?> messageConverter = converter;
        // 返回HttpMessageConverters对象
        return new HttpMessageConverters(messageConverter);
    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 清除所有默认的转换器
        converters.clear();
        // 添加FastJson转换器
        converters.add(fastJsonHttpMessageConverters().getConverters().get(0));
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        String s = null;
        map.put("key",s);
        /*String s2 = JSON.toJSONString(map,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullStringAsEmpty);
*/
/*        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key",s);
        String s1 = JSON.toJSONString(jsonObject,SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty);

        //System.out.println(s1);
        String s3 = JSON.toJSONString(s,SerializerFeature.WriteNullStringAsEmpty);
        //System.out.println(s3);

        String s4 = null;*/
       /* if (s4 == null) {
            s4 = ""; // 或者其他非空字符串
        }*/
        String s35 = JSON.toJSONString(s,SerializerFeature.WriteNullStringAsEmpty);
        System.out.println(s35); // 输出为 ""
    }
}
