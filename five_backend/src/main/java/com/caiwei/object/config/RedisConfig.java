package com.caiwei.object.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
 
@Configuration
public class RedisConfig {
  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
      // 创建 RedisTemplate 对象
      RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
      // 设置连接工厂
      redisTemplate.setConnectionFactory(connectionFactory);
      // 创建 JSON 序列化工具
      GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
      // 设置 key 的序列化
      redisTemplate.setKeySerializer(RedisSerializer.string());
      redisTemplate.setHashKeySerializer(RedisSerializer.string());
      // 设置 value 的序列化
      redisTemplate.setValueSerializer(jsonRedisSerializer);
      redisTemplate.setHashValueSerializer(jsonRedisSerializer);
      // 返回
      return redisTemplate;
  }
}