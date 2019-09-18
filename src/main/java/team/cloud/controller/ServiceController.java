package team.cloud.controller;

import io.fabric8.kubernetes.api.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.cloud.entity.vo.ResultVO;
import team.cloud.service.K8sServiceService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author tzw
 * CreateTime 20:34 2019/5/20
 **/

@RestController
@RequestMapping("/service")
public class ServiceController {

    @Autowired
    private K8sServiceService k8sServiceService;

    @RequestMapping("/create")
    public ResultVO create(@RequestBody HashMap map) throws Exception {
        String name=map.get("name").toString();
        String namespace=map.get("namespace").toString();
        String apiVersion=map.get("apiVersion").toString();
        final String labelName=map.get("labels").toString();
        Integer port=Integer.parseInt(map.get("port").toString());
        Integer targetPort=Integer.parseInt(map.get("targetPort").toString());
        String protocol=map.get("protocol").toString();

        Map<String, String> labels = new HashMap<String, String>(){
            {
                put("name",labelName);
            }
        };

        ObjectMeta objectMeta=new ObjectMeta();
        objectMeta.setName(name);
        objectMeta.setLabels(labels);
        objectMeta.setNamespace(namespace);

        List<ServicePort> servicePorts=new ArrayList<>();
        IntOrString intOrString = new IntOrString(targetPort);
        ServicePort servicePort=new ServicePort();
        servicePort.setPort(port);
        servicePort.setProtocol(protocol);
        servicePort.setTargetPort(intOrString);
        servicePorts.add(servicePort);

        ServiceSpec serviceSpec = new ServiceSpec();
        serviceSpec.setPorts(servicePorts);
        serviceSpec.setSelector(labels);

        Service service=new Service();
        service.setApiVersion(apiVersion);
        service.setSpec(serviceSpec);
        service.setMetadata(objectMeta);
        return k8sServiceService.create(service);
    }
    @PostMapping("deleteOneByNamespace")
    public ResultVO deleteOneByNamespace(@RequestBody Map<String,String> map) {
        String namespaceName = map.get("namespaceName");
        String serviceName = map.get("serviceName");
        return k8sServiceService.deleteServiceOneByNamespace(namespaceName,serviceName);
    }

    @PostMapping("deleteListByNamespace")
    public ResultVO deleteListByNamespace(@RequestBody Map<String,String> map){
        String namespaceName = map.get("namespaceName");
        return k8sServiceService.deleteListByNamespace(namespaceName);
    }

    @PostMapping("deleteAll")
    public ResultVO deleteAll(){
        return k8sServiceService.deleteAll();
    }

    @GetMapping("getOneByNamespace")
    public ResultVO getOneByNamespace(String namespaceName,String serviceName){
        return k8sServiceService.getOneByNamespace(namespaceName,serviceName);
    }

    @GetMapping("getListByNamespace")
    public ResultVO getByNamespace(String namespaceName){
        return k8sServiceService.getByNamespace(namespaceName);
    }

    @GetMapping("getList")
    public ResultVO getList(){
        return k8sServiceService.getList();
    }
}
