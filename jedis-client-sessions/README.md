### jedis-client-sessions
#### 简介

基于redis，实现共享会话的基本功能，包括：

 - 基本的会话
 - 关联会话

 在使用时更加方便，体现在：
 1. 不再需要关注序列化和反序列号<code>javabean</code>；

 2. 满足主键，外键做简单的关联查询；

     有一种场景，假设某个用户会话如下：
     
     ```java
     class UserSession{
            // 用户id，作为外键
            String userId;
            // 用户会话id，作为主键
            String sessionId;
            String name;
            int age;
        }
     ```

    当该会话存储于redis时，假设`sessionId`作为一个主键，能获取到整个`UserSession`，也希望能通作为外键`userId`能获取到整个`UserSession`。


#### 测试用例
- 通过主键查询的使用[BaseSessionCacheTest](test/java/slive/jedis/client/session/BaseSessionCacheTest.java)	
- 通过关联外键查询的使用[BaseRelateSessionCacheTest](test/java/slive/jedis/client/session/BaseRelateSessionCacheTest.java)

