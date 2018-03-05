package com.ruijie.packageservice.controller;

import com.ruijie.packageservice.service.PackageService;
import com.ruijie.packageservice.vo.FileVo;
import com.ruijie.packageservice.vo.PackageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yhr
 * @version latest
 * @date 2018/1/17
 */
@RestController
@RequestMapping("/v1")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @RequestMapping(value = "/package_start", method = RequestMethod.GET)
    public String packageStart(@RequestBody PackageVo packageVo) {
        return packageService.packageStart(packageVo.getSvnUrl(), packageVo.getBuild(), packageVo.getVersion(), packageVo.getPackageType());
    }

    @RequestMapping(value = "/files/{package_type}", method = RequestMethod.GET)
    public List<FileVo> listFiles(@PathVariable("package_type") Integer packageType) {
        return packageService.listFiles(packageType);
    }

    @RequestMapping(value = "/logs", method = RequestMethod.GET)
    public String readLogs() {

        return null;
    }
}
