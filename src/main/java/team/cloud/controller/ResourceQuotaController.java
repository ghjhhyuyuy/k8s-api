package team.cloud.controller;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceQuota;
import io.fabric8.kubernetes.api.model.ResourceQuotaSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.cloud.entity.vo.ResultVO;
import team.cloud.service.ResourceQuotaService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wzw on 2019/8/12
 *
 * @Author wzw
 */
@RestController
@RequestMapping("/resourceQuota")
public class ResourceQuotaController {
    @Autowired
    private ResourceQuotaService resourceQuotaService;
    @PostMapping("create")
    public ResultVO create(@RequestBody Map map) throws Exception {
        String name=map.get("name").toString();
        final String requestsCPU=map.get("requestsCPU").toString();
        String namespace = map.get("namespace").toString();
        String apiVersion=map.get("apiVersion").toString();
        final String requestsMemory=map.get("requestsMemory").toString();
        final String limitsCPU=map.get("limitsCPU").toString();
        final String limitsMemory=map.get("limitsMemory").toString();
        final String requestsStorage=map.get("requestsStorage").toString();
        Quantity quantity1 = new Quantity();
        quantity1.setAmount(requestsCPU);
        Quantity quantity2= new Quantity();
        quantity2.setAmount(limitsMemory);
        Quantity quantity3= new Quantity();
        quantity3.setAmount(limitsCPU);
        Map<String, Quantity> hard = new HashMap<String, Quantity>(){
            {
                put("requests.storage",quantity1);
                put("limits.cpu",quantity3);
                put("limits.memory",quantity2);
            }
        };
        ObjectMeta objectMeta=new ObjectMeta();
        objectMeta.setName(name);
        objectMeta.setNamespace(namespace);
        ResourceQuotaSpec resourceQuotaSpec = new ResourceQuotaSpec();
        resourceQuotaSpec.setHard(hard);
        ResourceQuota resourceQuota = new ResourceQuota();
        resourceQuota.setApiVersion(apiVersion);
        resourceQuota.setMetadata(objectMeta);
        resourceQuota.setSpec(resourceQuotaSpec);
        return resourceQuotaService.createResourceQuota(resourceQuota);
    }
}
