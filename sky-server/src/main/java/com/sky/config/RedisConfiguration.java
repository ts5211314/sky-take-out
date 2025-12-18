package com.sky.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Configuration
@Slf4j
public class RedisConfiguration {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始初始化RedisTemplate...");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // 1. 设置Redis连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 2. 配置String序列化器（Key/HashKey）
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);

        // 3. 配置JSON序列化器（Value/HashValue）
        Jackson2JsonRedisSerializer<Object> jsonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = buildObjectMapper();
        jsonSerializer.setObjectMapper(objectMapper);

        redisTemplate.setValueSerializer(jsonSerializer);
        redisTemplate.setHashValueSerializer(jsonSerializer);

        // 4. 初始化RedisTemplate
        redisTemplate.afterPropertiesSet();
        log.info("RedisTemplate初始化完成");
        return redisTemplate;
    }

    /**
     * 构建ObjectMapper（解决时间序列化、中文、类型标记问题）
     */
    private ObjectMapper buildObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 基础配置：开启所有字段序列化
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        // 时区配置（解决时间差问题）
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        // 关闭冗余配置：去掉类型标记（如["java.math.BigDecimal",72.00]）
        // 注意：关闭后反序列化需指定类型，如redisTemplate.opsForValue().get(key, List.class)
        // objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        // 忽略未知字段（避免反序列化时因字段不一致报错）
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 时间序列化配置（Java 8 LocalDateTime）
        JavaTimeModule timeModule = new JavaTimeModule();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        // 序列化：LocalDateTime → 字符串（yyyy-MM-dd HH:mm:ss）
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        // 反序列化：字符串 → LocalDateTime
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
        objectMapper.registerModule(timeModule);

        // 关闭时间戳序列化（避免LocalDateTime转成时间戳）
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }
}