package com.ruijie.packageservice.controller;

import com.ruijie.packageservice.util.ExcelUtil;
import com.ruijie.packageservice.vo.CompareVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YHR on 2018/3/5.
 */

@RestController
@RequestMapping("/v1")
public class CompareController {

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public List<CompareVo> compare(@RequestParam("local_file") MultipartFile localFile
            , @RequestParam("remote_file") MultipartFile remoteFile) throws Exception {
        Map<String, List<String[]>> remoteMap = new HashMap<>();
        List<CompareVo> compareVos = new ArrayList<>();
        List<String[]> remoteData = ExcelUtil.getExcelData(remoteFile);
        for (String[] strings : remoteData) {
            if (remoteMap.containsKey(strings[1])) {
                List<String[]> list = remoteMap.get(strings[1]);
                list.add(strings);
            } else {
                List<String[]> list = new ArrayList<>();
                list.add(strings);
                remoteMap.put(strings[1], list);
            }
        }
        List<String[]> localData = ExcelUtil.getExcelData(localFile);
        for (String[] strings : localData) {
            Long localSum = strings[2] == null ? 0L : Long.parseLong(strings[2]);
            //获取市医保数据
            List<String[]> remote = remoteMap.get(strings[1]);
            Long totalRemote = new Long(0);
            for (String[] row : remote) {
                totalRemote += Long.parseLong(row[2]);
            }
            if (!localSum.equals(totalRemote)) {//金额相等
                CompareVo compareVo = new CompareVo();
                compareVo.setUserName(strings[0]);
                compareVo.setIdCard(strings[1]);
                compareVo.setLocalSum(localSum);
                compareVo.setRemoteSum(totalRemote);
                compareVos.add(compareVo);
            }
        }
        return compareVos;
    }

}
