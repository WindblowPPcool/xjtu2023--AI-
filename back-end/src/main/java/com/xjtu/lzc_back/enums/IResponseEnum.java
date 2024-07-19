package com.xjtu.lzc_back.enums;
/**
 * 枚举类公共接口
 * @author lily
 * @param <K>
 * @param <V>
 * @param <C>
 */

public interface IResponseEnum<K, V, C extends Enum>{
    /**
     * 返回枚举对象
     * */
    C get();
    /**
     * 返回枚举项的 key
     * */
    K code();
    /**
     * 返回枚举项的 value
     * */
    V message();
}