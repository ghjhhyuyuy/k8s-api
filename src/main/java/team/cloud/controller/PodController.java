package team.cloud.controller;

import io.fabric8.kubernetes.api.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.cloud.entity.vo.ResultVO;
import team.cloud.service.PodService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wzw on 2019/5/21
 *
 * @Author wzw
 */
@RestController
@RequestMapping("/pod")
public class PodController {
    @Autowired
    private PodService podService;
    @GetMapping("getPod")
    public ResultVO getPod(String namespaceName,String podName){
        return podService.getPod(namespaceName,podName);
    }
    @GetMapping("listPodsFromNamespace")
    public ResultVO listPodsFromNamespace(@RequestBody Map<String,String> map){
        String namespaceName = map.get("namespaceName");
        return podService.listPodsFromNamespace(namespaceName);
    }
    @PostMapping("deletePod")
    public ResultVO deletePod(@RequestBody Map<String,String> map){
        String namespaceName = map.get("namespaceName");
        String podName = map.get("podName");
        return podService.deletePod(namespaceName,podName);
    }
    @PostMapping("createPod")
    public ResultVO createPod( @RequestBody Map map) throws Exception {
        String name=map.get("podName").toString();
        String namespace=map.get("namespace").toString();
        String apiVersion=map.get("apiVersion").toString();
        final String labelName=map.get("labelName").toString();
        String containerName=map.get("containerName").toString();
        Integer containerPort=Integer.parseInt(map.get("containerPort").toString());
        String containerImage=map.get("containerImage").toString();
        //label相关封装成map
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
        Pod pod = new Pod();
        pod.setApiVersion(apiVersion);
        pod.setSpec(podSpec);
        pod.setMetadata(objectMeta);
        return podService.createPod(pod);
    }
    @PostMapping("deletePods")
    public ResultVO deletePods(String namespaceName){
        return podService.deletePods(namespaceName);
    }
    @PostMapping("deleteAll")
    public ResultVO deleteAll(){
        return podService.deleteAll();
    }

    @GetMapping("getAll")
    public ResultVO getAll(){
        return podService.getAll();
    }
}
