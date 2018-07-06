# funda
Funda 是一个以 SpringBoot 为基础搭建一个 Restful 服务框架

内容：
1. 数据交互规范定义
2. Profile 配置管理、配置分离、配置加密
3. 统一的日志处理
4. 统一的异常处理
5. 数据库集成、Mybatis 集成、JPA 集成
5. 常规单元测试、MVC 单元测试、Mock 单元测试

## Docker 镜像部署

1. 镜像构建
```shell
./gradlew build -x test docker
```

2. 镜像启动

```shell
docker-compose up -d
```

# log-back-spring 日志管理配置
https://blog.csdn.net/wsywb111/article/details/79655532
# auth认证
https://www.cnblogs.com/minsons/p/7058837.html
#
"redirect:/api/liu";  controller中重定向

# springboot 注解大全
https://www.cnblogs.com/tanwei81/p/6814022.html
