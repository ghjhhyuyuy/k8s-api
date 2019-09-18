package team.cloud.controller;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.cloud.entity.vo.ResultVO;
import team.cloud.service.ConfigMapService;
import team.cloud.util.ResultVOUtil;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wzw on 2019/5/16
 *
 * @Author dlz
 */
@RestController
@RequestMapping("/configMap")
public class ConfigMapController {
    @Autowired
    private ConfigMapService configMapService;


    @PostMapping("create")
    public ResultVO create(@RequestBody Map<String, String> map) throws Exception {
        String name,namespace,apiVersion;
        int size = 0;
        try{
            name = map.get("name");
            namespace = map.get("namespace");
            apiVersion = map.get("apiVersion");
            size = Integer.parseInt(map.get("size"));
        }catch (Exception e){
            return new ResultVOUtil().paramError();
        }
        Map<String, String> data1 = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String key = "dataName" + i;
            String dataName = map.get(key);
            String value = map.get(dataName);
            data1.put(dataName, value);
        }

        final String labelName = map.get("labels");
        Map<String, String> labels = new HashMap<String, String>() {
            {
                put("name", labelName);
            }
        };
        ObjectMeta objectMeta = new ObjectMeta();
        objectMeta.setName(name);
        objectMeta.setLabels(labels);
        objectMeta.setNamespace(namespace);
        ConfigMap configMap = new ConfigMap();
        configMap.setMetadata(objectMeta);
        configMap.setApiVersion(apiVersion);
        configMap.setData(data1);
        return configMapService.create(configMap);
    }

    @PostMapping("deleteConfigMap")
    public ResultVO deleteConfigMap(@RequestBody Map<String, String> map) {
        String namespaceName,configMapName;
        try {
            namespaceName = map.get("namespaceName");
            configMapName = map.get("configMapName");
        } catch (Exception e) {
            return new ResultVOUtil().paramError();
        }
        return configMapService.deleteConfigMap(namespaceName, configMapName);
    }

    @PostMapping("deleteConfigMaps")
    public ResultVO deleteConfigMaps(@RequestBody Map<String, String> map) {
        String namespaceName = map.get("namespaceName");
        return configMapService.deleteConfigMaps(namespaceName);
    }

    @PostMapping("deleteAllConfigMaps")
    public ResultVO deleteConfigMap() {
        return configMapService.deleteAllConfigMaps();
    }

    @GetMapping("getOneByNamespace")
    public ResultVO getOneByNamespace(String namespaceName, String configMapName) {
        return configMapService.getOneByNamespace(namespaceName, configMapName);
    }

    @GetMapping("getByNamespace")
    public ResultVO getByNamespace(String namespaceName) {
        return configMapService.getByNamespace(namespaceName);
    }

    @GetMapping("getList")
    public ResultVO getList() {
        return configMapService.getList();
    }
}
