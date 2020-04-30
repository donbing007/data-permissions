
[![pipeline status](http://gitlab.developer.xforcecloud.com/ultraman-xplat-db/data-permissions/badges/develop/pipeline.svg)](http://gitlab.developer.xforcecloud.com/ultraman-xplat-db/data-permissions/commits/develop)  [![coverage report](http://gitlab.developer.xforcecloud.com/ultraman-xplat-db/data-permissions/badges/develop/coverage.svg)](http://gitlab.developer.xforcecloud.com/ultraman-xplat-db/data-permissions/commits/develop)

#引言

基于 SQL 提供通用的数据权限服务.
用以解决实际系统中除了功能权限以外更细粒度的权限控制.

#快速开始

本身是一个基于 spring boot 2.x的项目.可以使用如下方式运行. 

##java 命令行
`java -jar ./xplat-data-permissions-boot/target/xplat-data-permissions.jar`

##helm
helm install ./charts/xplat-data-permissions

##配置说明
服务本身需要一个关系型数据库mysql,和 redids.以下是可调整的配置.

```yaml
server:
  port: 8080
# grpc 配置.
grpc:
  enabled: true
  port: 8206
  maxInboundMetadataBytes: 1048576  # 允许的请求头大小(字节)
  maxInboundMessageBytes: 1048576  # 允许的请求消息大小(字节).
  heartbeatIntervalSeconds: 30 # 两次心跳的间隔秒.
  heartbeatTimeoutSeconds: 30 # 心跳超时秒.
# 线程池.
executor:
  maxTaskSize: 100
  # maxWorkerSize: cpu core

# 使用 j2cache
spring:
  cache:
    type: GENERIC
    cache-names: rule
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/xplat_data_permissions?useUnicode=true&characterEncoding=utf8&socketTimeout=6000&autoReconnect=true&failOverReadOnly=false
    username: root
    password: root
    hikari:
      connection-test-query: "select 1"
      connection-timeout: 10000
      validation-timeout: 3000
      maximum-pool-size: 1

j2cache:
  config-location: /j2cache/j2cache-${spring.profiles.active}.properties
  open-spring-cache: true
  cache-clean-mode: active
  l2-cache-open: false
  redis-client: lettuce
```
由于项目中使用了 j2cache 来实现2级缓存, 所以需要单独配置 [j2cache](https://www.oschina.net/p/j2cache) 配置.

#如何使用

要明确"数据权限"是依赖"用户中心"的授权信息才可以正确工作的组件.

提供了以下的接入方式.
* spring boot starter
* jdbc driver

## starter
需要如下 maven 仓库.
```xml
<repository>
  <id>xplat-releases</id>
  <name>Nexus Release Repository</name>
  <url>http://120.27.140.65:8081/nexus/content/repositories/releases/</url>
  <releases>
     <enabled>true</enabled>
     </releases>
</repository>
<repository>
   <id>xplat-snapshots</id>
   <name>Nexus Snapshot Repository</name>
   <url>http://120.27.140.65:8081/nexus/content/repositories/snapshots</url>
   <snapshots>
      <enabled>true</enabled>
      <updatePolicy>always</updatePolicy>
   </snapshots>
</repository>
```
项目中增加如下依赖.
```xml
<dependency>
  <groupId>com.xforceplus.ultraman</groupId>
  <artifactId>xplat-data-permissions-starter</artifactId>
  <version>0.1.7</version>
</dependency>
```
如果你的 JDBC DataSource 已经托管于 Spring 管理,那么你没有任何代码修改了.
但是你需要额外提供一份 spring 的配置.如下.
```yaml
xplat:
  data:
    permissions:
      # 数据权限服务端地址
      host: "127.0.0.1"
      # 数据权限服务端端口, 默认8206.
      port: 8206
      # 如果你有多个托管于 spring 的 DataSource 实例,但你只想对某那 DataSource 启用数据权限,可以在这里设定 beanName 的匹配正则.
      includeRex: "(.*)"
      # 是否手动管理,如果为 true 那么不会再自动对托管的 DataSoruce 进行处理,需要代码手动指定.默认为 false.
      manual: false
      # 读取超时时间,如果通过公网访问可以适当加大此值.
      readTimeoutMs: 200
      # 授权信息的搜索配置,如果没有这个结点即为 CONTEXT.
      searcher:
        # 搜索器名称,MOCK 可以自己指定,默认为从上下文中查找"用户中心"的授权信息.
        # mock 一般用以开发调试阶段.
        name: "MOCK"
        role: "r1"
        tenant: "t1"
```
###手动管理
如果 manual 设定为 true,那么不会自动启效了.需要手动代码指定.如下.
```java
DataSource targetDataSource = //...
targetDataSource = DataSourceWrapper.wrap(targetDataSource);
```
但是实际上,即使你将 manual 设置为 true,你仍然可以通过上述代码手动指定一个.

manual 只是控制是否自动处理托管的数据源.

##jdbc driver
comming soon...

#环境
在开发阶段可以直接使用如下配置进行调试.
```yaml
xplat:
  data:
    permissions:
      host: "120.55.249.44"
      port: 27206
      debug: true
      includeRex: "(.*)"
      searcherConfig:
        name: "MOCK"
        role: "r1"
        tenant: "t1"
```
这里假定使用的授权信息是"r1,t1".你可以根据自己的需要修改,这样在开发阶段可以不需要关注用户中心的授权.

开发环境提供了一个[界面](http://ultraman.paas-t.xforceplus.com/?id=206)进行权限配置.
```text
用户名:  beibei2@qq.com
密码:      Ab123456
```

##生产环境
生产环境请使用如下配置.
```yaml
xplat:
  data:
    permissions:
      host: "xplat-data-permissions-grpc"
      port: 8206
      debug: true
      includeRex: "(.*)"
```
#集成后
集成后你的项目会有以下可能的行为改变.

有些 SQL 不能使用
数据权限对于某些 SQL 语法规则有洁癖.会拒绝掉不合规的语句,规则如下.


* 查询语句不可以使用"*". 
```sql
-- 返回值不可以用*.
SELECT * FROM table
```
* 除 select 外不允许子查询.
```sql
-- insert/update/delete 不可以有子查询.
INSERT INTO table2 SELECT * FROM table1
```
* From子查询必须有别名. 
```sql
`` 最后一个 t 别名是必须的.
SELECT t.id from (SELECT t.id FROM table1 t) t
```
* 如果是非简单字段必须设置别名,比如函数,表达式.
```sql
-- SUM 必须设置一个别名.这里是 id.
SELECT SUM(t.c1 + t.C2) id FROM table t
```
* 必须设置来源表名或者表别名. ref.字段名.
```sql
-- 这里的 ref 是必须的.
SELECT ref.c1, ref.c2 FROM table ref;
```



##查询缓存
如果你原先对数据库的查询做了查询缓存,那么现在将不再正确了.

因为实际执行的 SQL 将根据当前的授权信息和其相应的权限规则来决定了.

例如: 有一个授权角色为 "r1",租户为"t1". 并且设定了对于 test 表有如下权限规则.

* 数据权限  c1 > 200
* 字段权限 c1,c2

原 SQL:  `select t.c1,t.c2,t.c3 from test t`

实际 SQL: `select t.c1,t.c2 from test t where t.c1 > 200`

数据权限将在之后的迭代中提供一种机制来让业务感知到这种变化.

##对象缓存
对象缓存仍旧是有效的,不过建议现在缓存的 Key 请增加相应的授权信息.

例如,原缓存对于一份数据的缓存如下.

```text
key: data.test.entity
value: {"c1":100, "c2": 200, "c3": 300}
```

但是像上述查询缓存那节一样,r1角色没有"c3"列的权限,所以查询结果如下.

 `{"c1":100, "c2": 200, "c3": 0}`


但是换另一个角色查询结果又可能完全不一样(和规则配置有关).如下.

 `{"c1":100, "c2": 0, "c3": 200}`

这是没有 c2 的字段权限的结果.

##空值
为了保证业务系统在运行时不会被打断,所以即使当前用户对于没有权限的执行仍然是放行的.

只是查询不出任何数据,取而代之的是一些默认空值.如下.

```text
STRING = "";
BOOLEAN = false;
BYTE = 0;
BYTES = [0];
SHORT = 0;
INT = 0;
LONG = 0;
FLOAT = 0.0f;
DOUBLE = 0.0f;
BIG_DECIMAL = BigDecimal.ZERO;
DATE = new Date(Long.MIN_VALUE);
TIME = new Time(Long.MIN_VALUE);
TIMESTAMP = new Timestamp(Long.MIN_VALUE);
```

如果这些空值在系统中有特殊意义,那你可能需要修改一下定义.

之后的版本中会提供修改空值的手段.

##授权信息
授权信息是使用了阿里的 transmittable-thread-local 组件来完成从上层传递到数据权限的.

所以当你处理多线程的时候请参考其文档进行代码调整.

##测试
由于 SQL 会被动态的改变,所以你的业务代码的行来可能会被改变.可能需要增加更多的测试用例来保证一切都是可控的.