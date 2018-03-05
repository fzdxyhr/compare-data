package com.ruijie.packageservice.vo;

import lombok.Data;

import java.util.List;

/**
 * @author yhr
 * @version latest
 * @date 2018/1/18
 */

@Data
public class PackageVo {

    public String svnUrl;

    public String version;

    public String build;

    public List<Integer> packageType;
}
