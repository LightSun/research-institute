package com.qchery.funda.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Validated //jsr-303规范验证
@Configuration
@ConfigurationProperties("app")
public class ApplicationProperties {

    // @valid会去验证Cache对象属性值是否满足要求
    @Valid @NotNull private Cache cache;
    @Valid @NotNull private Cors cors;

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public Cors getCors() {
        return cors;
    }

    public void setCors(Cors cors) {
        this.cors = cors;
    }

    public static class Cache {

        @Max(1000)
        private Integer ttl;

        @Max(3600)
        private Long maxEntries;

        // ... getters and setters

        public Integer getTtl() {
            return ttl;
        }

        public void setTtl(Integer ttl) {
            this.ttl = ttl;
        }

        public Long getMaxEntries() {
            return maxEntries;
        }

        public void setMaxEntries(Long maxEntries) {
            this.maxEntries = maxEntries;
        }

        @Override
        public String toString() {
            return "Cache{" + "ttl=" + ttl + ", maxEntries=" + maxEntries + '}';
        }
    }

    public static class Cors {

        private List<String> allowedOrigins;
        private String[] allowedMethods;
        private List<String> allowedHeaders;
        private Boolean allowCredentials;
        private Integer maxAge;

        // ... getters and setters

        public List<String> getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        public String[] getAllowedMethods() {
            return allowedMethods;
        }

        public void setAllowedMethods(String[] allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        public List<String> getAllowedHeaders() {
            return allowedHeaders;
        }

        public void setAllowedHeaders(List<String> allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }

        public Boolean getAllowCredentials() {
            return allowCredentials;
        }

        public void setAllowCredentials(Boolean allowCredentials) {
            this.allowCredentials = allowCredentials;
        }

        public Integer getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(Integer maxAge) {
            this.maxAge = maxAge;
        }

        @Override
        public String toString() {
            return "Cors{"
                    + "allowedOrigins="
                    + allowedOrigins
                    + ", allowedMethods="
                    + Arrays.toString(allowedMethods)
                    + ", allowedHeaders="
                    + allowedHeaders
                    + ", allowCredentials="
                    + allowCredentials
                    + ", maxAge="
                    + maxAge
                    + '}';
        }
    }

    // ... getters and setters

    @Override
    public String toString() {
        return "ApplicationProperties{" + "cache=" + cache + ", cors=" + cors + '}';
    }
}
