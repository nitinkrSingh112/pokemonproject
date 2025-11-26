package com.example.pokedex.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {
  @Bean
  public CacheManager cacheManager() {
    var detailCache = new CaffeineCache("pokemonDetails",
        Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build());

    var indexCache = new CaffeineCache("pokemonIndex",
        Caffeine.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build());

    var abilityCache = new CaffeineCache("abilityEffects",
        Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build());

    var mgr = new SimpleCacheManager();
    mgr.setCaches(List.of(detailCache, indexCache, abilityCache));
    return mgr;
  }
}
