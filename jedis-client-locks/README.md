### jedis-client-locks
#### 简介
基于jedis实现的分布式锁，简单好用。

#### 分布式锁应该具备的条件
1. 在分布式系统环境下，一个方法在同一时间只能被一个机器的的一个线程执行；
2. 高可用的获取锁与释放锁；
3. 高性能的获取锁与释放锁；
4. 具备可重入特性；
5. 具备锁失效机制，防止死锁；
6. 具备非阻塞锁特性，即没有获取到锁将直接返回获取锁失败。


#### 测试用例
- [BaseLockExecutorTest](test/slive/jedis/client/lock/BaseLockExecutorTest.java)
