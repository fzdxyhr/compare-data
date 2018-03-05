package com.ruijie.packageservice.service;

import com.ruijie.packageservice.vo.FileVo;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author yhr
 * @version latest
 * @date 2018/1/18
 */
public interface PackageService {

    String packageStart(String svnUrl, String build, String version,List<Integer> packageTypes);

    List<FileVo> listFiles(Integer packageType);

    String readLogs();

}
