package team.cloud.controller;


import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.api.model.apps.DaemonSetSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.cloud.entity.vo.ResultVO;
import team.cloud.service.DaemonSetService;
import team.cloud.util.ResultVOUtil;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/daemonSet")
public class DaemonSetController {

    @Autowired
    private DaemonSetService daemonSetService;

    @PostMapping("/create")
    public ResultVO create(@RequestBody Map map) throws Exception {
        String name,namespace,apiVersion,containerName,containerImage;
        Integer containerPort;
        final String labelName;
        try {
            name=map.get("name").toString();
            namespace=map.get("namespace").toString();
            apiVersion=map.get("apiVersion").toString();
            labelName=map.get("labels").toString();
            containerName=map.get("containerName").toString();
            containerPort=Integer.parseInt(map.get("containerPort").toString());
            containerImage=map.get("containerImage").toString();
        }catch (Exception e){
            return new ResultVOUtil().paramError();
        }
        Map<String, String> labels = new HashMap<String, String>(){
            {
                put("name",labelName);
            }
        };
        ObjectMeta objectMeta=new ObjectMeta();
        objectMeta.setName(name);
        objectMeta.setLabels(labels);
        objectMeta.setNamespace(namespace);
        PodSpec podSpec = new PodSpec();
        List<ContainerPort> containerPorts=new ArrayList<>();
        ContainerPort containerPortObject=new ContainerPort();
        containerPortObject.setContainerPort(containerPort);
        containerPorts.add(containerPortObject);
        List<Container> containers=new ArrayList<>();
        Container container=new Container();
        container.setName(containerName);
        container.setImage(containerImage);
        container.setPorts(containerPorts);
        containers.add(container);
        podSpec.setContainers(containers);

        PodTemplateSpec templateSpec=new PodTemplateSpec();
        templateSpec.setSpec(podSpec);

        DaemonSetSpec daemonSetSpec=new DaemonSetSpec();
        daemonSetSpec.setTemplate(templateSpec);

        LabelSelector labelSelector = new LabelSelector();
        labelSelector.setMatchLabels(labels);
        daemonSetSpec.setSelector(labelSelector);

        DaemonSet daemonSet=new DaemonSet();
        daemonSet.setApiVersion(apiVersion);
        daemonSet.setMetadata(objectMeta);
        daemonSet.setSpec(daemonSetSpec);
        return daemonSetService.create(daemonSet);
    }

    @PostMapping("deleteOneByNamespace")
    public ResultVO deleteOneByNamespace(@RequestBody Map<String,String> map) {
        String namespaceName,daemonSetName;
        try {
            namespaceName = map.get("namespaceName");
            daemonSetName = map.get("daemonSetName");
        }catch (Exception e){
            return new ResultVOUtil().paramError();
        }

         return daemonSetService.deleteDaemonSetOneByNamespace(namespaceName,daemonSetName);
    }

    @PostMapping("deleteListByNamespace")
    public ResultVO deleteListByNamespace(@RequestBody Map<String,String> map){
        String namespaceName = map.get("namespaceName");
        return daemonSetService.deleteListByNamespace(namespaceName);
    }

    @PostMapping("deleteAll")
    public ResultVO deleteAll(){
        return daemonSetService.deleteAll();
    }

    @GetMapping("getOneByNamespace")
    public ResultVO getOneByNamespace(String namespaceName,String daemonSetName){
        return daemonSetService.getOneByNamespace(namespaceName,daemonSetName);
    }

    @GetMapping("getByNamespace")
    public ResultVO getByNamespace(String namespaceName){
        return daemonSetService.getByNamespace(namespaceName);
    }

    @GetMapping("getList")
    public ResultVO getList(){
        return daemonSetService.getList();
    }
}
