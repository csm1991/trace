# Trace 分布式链路追踪框架

## 概述
提供完整的微服务链路追踪能力，支持日志采集、调用链分析、性能监控等功能。

## 快速入门
```bash
git clone https://github.com/csm1991/trace.git
mvn clean install
```

```bash
通过上述命令构建成功后，再实际项目中通过maven方式引入相关trace组件即可。
如果有使用maven仓库私服，则通过mvn clean deploy命令导入到私服后，再引入即可。
```

## 配置说明
### 日志配置（trace-log模块）
```yaml
trace.log:
  controller:
    threshold: 1000               #执行超时时长，单位ms
    input-level: INFO             #入参日志的日志级别
    execution-level: INFO         #执行时长&出参日志的日志级别
    timeout-level: INFO           #执行超时的日志级别
    enable-input-level: true      #是否启用入参日志
    enable-execution-level: true  #是否启用执行时长&出参日志
    enable-timeout-level: true    #是否启用执行超时的日志
    input-collection-mask: false   #是否对入参集合类型进行屏蔽（true-屏蔽集合内容，只保留类型信息）
    output-collection-mask: false  #是否对出参集合类型进行屏蔽（true-屏蔽集合内容，只保留类型信息）
  service:
    threshold: 1000               #执行超时时长，单位ms
    input-level: INFO             #入参日志的日志级别
    execution-level: INFO         #执行时长&出参日志的日志级别
    timeout-level: INFO           #执行超时的日志级别
    enable-input-level: true      #是否启用入参日志
    enable-execution-level: true  #是否启用执行时长&出参日志
    enable-timeout-level: true    #是否启用执行超时的日志
    input-collection-mask: false   #是否对入参集合类型进行屏蔽（true-屏蔽集合内容，只保留类型信息）
    output-collection-mask: false  #是否对出参集合类型进行屏蔽（true-屏蔽集合内容，只保留类型信息）
  mapper:
    threshold: 1000               #执行超时时长，单位ms
    input-level: INFO             #入参日志的日志级别
    execution-level: INFO         #执行时长&出参日志的日志级别
    timeout-level: INFO           #执行超时的日志级别
    enable-input-level: true      #是否启用入参日志
    enable-execution-level: true  #是否启用执行时长&出参日志
    enable-timeout-level: true    #是否启用执行超时的日志
    input-collection-mask: false   #是否对入参集合类型进行屏蔽（true-屏蔽集合内容，只保留类型信息）
    output-collection-mask: false  #是否对出参集合类型进行屏蔽（true-屏蔽集合内容，只保留类型信息）
```

## 模块结构
```
├── trace-core          # 核心抽象层（SPI扩展点 & 基础注解）
├── trace-feign         # Feign客户端调用追踪实现
├── trace-gateway       # SpringCloud网关请求标识透传
├── trace-kafka         # Kafka消息链路追踪支持
├── trace-rocketmq      # RocketMQ消息轨迹追踪
├── trace-springboot    # SpringBoot自动配置支持
├── trace-log           # 方法级日志追踪实现
└── trace-full          # 全组件集成依赖包（生产环境推荐）
```
