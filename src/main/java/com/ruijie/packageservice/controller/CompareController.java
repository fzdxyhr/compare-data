package com.ruijie.packageservice.controller;

import com.ruijie.packageservice.constant.CommonContant;
import com.ruijie.packageservice.util.ExcelUtil;
import com.ruijie.packageservice.util.ExportExcelUtil;
import com.ruijie.packageservice.vo.CompareVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
            CompareVo compareVo = new CompareVo();
            Long localSum = strings[2] == null ? 0L : Long.parseLong(strings[2]);
            //获取市医保数据
            List<String[]> remote = remoteMap.get(strings[1]);
            Long totalRemote = new Long(0);
            if (CollectionUtils.isNotEmpty(remote)) {
                for (String[] row : remote) {
                    totalRemote += Long.parseLong(row[2]);
                }
            }
            if (localSum.equals(totalRemote)) {//金额相等
                continue;
            } else if (remote == null || remote.size() == 0) {//市医保不存在数据
                compareVo.setStatus(3);
            } else {
                compareVo.setStatus(1);
            }
            compareVo.setUserName(strings[0]);
            compareVo.setIdCard(strings[1]);
            compareVo.setLocalSum(localSum);
            compareVo.setRemoteSum(totalRemote);
            compareVos.add(compareVo);
        }
        mergeData(localData, remoteData, compareVos);
        CommonContant.compareVos = compareVos;
        return compareVos;
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public void export(HttpServletResponse response) throws Exception {
        ExportExcelUtil<CompareVo> exportExcelUtil = new ExportExcelUtil<>();
        String[] headers = new String[]{"姓名", "身份证", "系统金额", "医保中心金额", "状态"};
        exportExcelUtil.exportExcel("差别用户", headers, CommonContant.compareVos, "", "差别用户", response, new HashMap<String, String>(), new HashMap<String, Object>());
    }

    private void mergeData(List<String[]> localData, List<String[]> remoteData, List<CompareVo> compareVos) {
        //以远程数据为准，检验本地数据是否存在
        Map<String, List<String[]>> localMap = new HashMap<>();
        Map<String, String> isKeyMap = new HashMap<>();
        for (String[] strings : localData) {
            if (localMap.containsKey(strings[1])) {
                List<String[]> list = localMap.get(strings[1]);
                list.add(strings);
            } else {
                List<String[]> list = new ArrayList<>();
                list.add(strings);
                localMap.put(strings[1], list);
            }
        }
        for (String[] strings : remoteData) {
            CompareVo compareVo = new CompareVo();
            if (isKeyMap.containsKey(strings[1])) {
                continue;
            }
            isKeyMap.put(strings[1], strings[1]);
            Long localSum = strings[2] == null ? 0L : Long.parseLong(strings[2]);
            //获取本地数据
            List<String[]> remote = localMap.get(strings[1]);
            Long totalRemote = new Long(0);
            if (CollectionUtils.isNotEmpty(remote)) {
                for (String[] row : remote) {
                    totalRemote += Long.parseLong(row[2]);
                }
            }
            if (remote == null || remote.size() == 0) {
                compareVo.setStatus(2);
                compareVo.setUserName(strings[0]);
                compareVo.setIdCard(strings[1]);
                compareVo.setLocalSum(totalRemote);
                compareVo.setRemoteSum(localSum);
                compareVos.add(compareVo);
            } else {
                continue;
            }

        }

    }

}
