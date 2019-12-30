//==============================================================================
//
//	@author Slive
//	@date  2018-9-11
//
//==============================================================================
package slive.jedis.client.core;

/**
 * 描述：数据类型有：
 * <ul>
 * <li>none (key不存在)
 * <li>string (字符串)
 * <li>list (列表)
 * <li>set (集合)
 * <li>zset (有序集)
 * <li>hash (哈希表)
 * </ul>
 * 
 */
public enum ValueType {

    none("none"), string("string"), list("list"), set("set"), zset("zset"), hash("hash");

    private String type;

    private ValueType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ValueType getType(String type) {
        ValueType[] values = values();
        for (ValueType vt : values) {
            if (vt.getType().equals(type)) {
                return vt;
            }
        }
        return none;
    }
}
