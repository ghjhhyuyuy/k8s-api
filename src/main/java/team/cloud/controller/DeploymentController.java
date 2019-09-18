package team.cloud.controller;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.cloud.entity.vo.ResultVO;
import team.cloud.service.DeploymentService;
import team.cloud.util.ResultVOUtil;

import javax.validation.Valid;
import java.util.*;

/**
 * Description:
 * @author tzw
 * CreateTime 12:48 2019/5/18
 **/
@RestController
@RequestMapping("/deployment")
public class DeploymentController {

    @Autowired
    private DeploymentService deploymentService;

    @PostMapping("/create")
    public ResultVO createDeployment(@RequestBody Map map) throws Exception {
        String name,namespace,apiVersion,containerName,containerImage,command,arg;
        Integer replicas,containerPort;
        List<String> commands = new ArrayList<>();
        List<String> args = new ArrayList<>();
        final String matchLable,labelName;
        try{
            name=map.get("name").toString();
            namespace=map.get("namespace").toString();
            apiVersion=map.get("apiVersion").toString();
            labelName=map.get("labelName").toString();
            replicas=Integer.parseInt(map.get("replicas").toString());
            containerName=map.get("containerName").toString();
            containerPort=Integer.parseInt(map.get("containerPort").toString());
            containerImage=map.get("containerImage").toString();
            if(map.get("command")!=null){
                command = map.get("command").toString();
                String[] command1 = command.split(",");
                commands.addAll(Arrays.asList(command1));

            }
            if(map.get("arg")!=null){
                arg = map.get("arg").toString();
                String[] arg1 = arg.split(",");
                args.addAll(Arrays.asList(arg1));

            }
            matchLable=map.get("matchLable").toString();
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().paramError();
        }

        //label相关封装成map
        Map<String, String> labels1 = new HashMap<String, String>(){
            {
                put("app",matchLable);
            }
        };
        ObjectMeta objectMeta=new ObjectMeta();
        objectMeta.setName(name);
        objectMeta.setLabels(labels1);
        objectMeta.setNamespace(namespace);
        DeploymentSpec deploymentSpec=new DeploymentSpec();
        deploymentSpec.setReplicas(replicas);

        PodSpec podSpec = new PodSpec();
        List<ContainerPort> containerPorts=new ArrayList<>();
        ContainerPort containerPortObject=new ContainerPort();
        containerPortObject.setContainerPort(containerPort);
        containerPorts.add(containerPortObject);
        List<Container> containers=new ArrayList<>();
        Container container=new Container();
        container.setName(containerName);
        container.setImage(containerImage);
        container.setCommand(commands);
        container.setArgs(args);
        container.setPorts(containerPorts);
        containers.add(container);
        podSpec.setContainers(containers);

        PodTemplateSpec templateSpec=new PodTemplateSpec();
        templateSpec.setSpec(podSpec);
        deploymentSpec.setTemplate(templateSpec);

        LabelSelector labelSelector = new LabelSelector();
        labelSelector.setMatchLabels(labels1);
        deploymentSpec.setSelector(labelSelector);

        Deployment deployment=new Deployment();
        deployment.setApiVersion(apiVersion);
        deployment.setSpec(deploymentSpec);
        deployment.setMetadata(objectMeta);
        return deploymentService.create(deployment);
    }
    @PostMapping("deleteOneByNamespace")
    public ResultVO deleteOneByNamespace(@RequestBody Map<String,String> map) {
        String namespaceName,deploymentName;
        try {
            namespaceName = map.get("namespaceName");
            deploymentName = map.get("deploymentName");
        }catch (Exception e){
            return new ResultVOUtil().paramError();
        }

        return deploymentService.deleteDeploymentOneByNamespace(namespaceName,deploymentName);
    }

    @PostMapping("deleteListByNamespace")
    public ResultVO deleteListByNamespace(@RequestBody Map<String,String> map){
        String namespaceName = map.get("namespaceName");
        return deploymentService.deleteListByNamespace(namespaceName);
    }

    @PostMapping("deleteAll")
    public ResultVO deleteAll(){
        return deploymentService.deleteAll();
    }

    @GetMapping("getOneByNamespace")
    public ResultVO getOneByNamespace(String namespaceName,String deploymentName){
        return deploymentService.getOneByNamespace(namespaceName,deploymentName);
    }

    @GetMapping("getListByNamespace")
    public ResultVO getByNamespace(String namespaceName){
        return deploymentService.getByNamespace(namespaceName);
    }

    @GetMapping("getList")
    public ResultVO getList(){
        return deploymentService.getList();
    }

}

