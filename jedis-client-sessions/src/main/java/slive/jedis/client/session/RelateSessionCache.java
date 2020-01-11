package slive.jedis.client.session;

import slive.jedis.client.session.annotation.SessionCategory;
import slive.jedis.client.session.annotation.SessionFKey;

import java.util.List;

/**
 * 描述：<br>
 *     基本的关联会话缓存类，主要实现功能包括：
 *      基于主键的加入，更新，删除和会话延长等。
 *      支持基于外键{@link SessionFKey}或者分类{@link SessionCategory}查询到会话对象
 *
 *      <i>注意：所有操作不具有原子性，可能存在数据同步问题。</i>
 *
 * @author slive
 * @date 2020/1/4
 */
public interface RelateSessionCache<T> extends SessionCache<T> {

    /**
     * 通过外键获取对应的pojo值
     * @param fName 外键字段名
     * @param fkey 外键值
     * @return 返回pojo值
     */
    T getByFKey(String fName, String fkey);

    /**
     * 基于注解方式获取到key，然后更新值
     * @param value
     * @return
     */
    boolean put(T value);

    /**
     * 通过外键获取对应的pojo值po
     * @param categoryName 外键字段名
     * @param categoryVal 外键值
     * @return 返回pojo列表值
     */
    List<T> getByCategory(String categoryName, String categoryVal);

    /**
     * 通过外键延长超时时间
     * @param fName
     * @param fkey
     * @return
     */
    boolean expireByFKey(String fName, String fkey);
}
