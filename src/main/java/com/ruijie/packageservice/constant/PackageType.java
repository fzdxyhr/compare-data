package com.ruijie.packageservice.constant;

/**
 * @author yhr
 * @version latest
 * @date 2018/1/18
 */
public enum PackageType {

    INSTALL(1), UPGRADE(2);

    private Integer value;

    PackageType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
