package com.mango.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

@Configuration
public class HttpConverterConfig {
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        // 1.定义一个converters转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        // 2.添加fastJson的配置信息，比如: 是否需要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        // 3.过滤器设置 防止JSON数据XSS
        fastJsonConfig.setSerializeFilters((ValueFilter) (object, name, value) -> {
            if(value instanceof String){
                return StringEscapeUtils.escapeHtml4((String) value);
            }
            return value;
        });
        // 4.在converter中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        // 5.将converter赋值给HttpMessageConverter
        // 6.返回HttpMessageConverters对象
        return new HttpMessageConverters((HttpMessageConverter<?>) fastConverter);
    }

}
