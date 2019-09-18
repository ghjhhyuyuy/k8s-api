package team.cloud.controller;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.cloud.entity.vo.ResultVO;
import team.cloud.service.NamespaceService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wzw on 2019/5/21
 *
 * @Author wzw
 */
@RestController
@RequestMapping("/namespace")
public class NamespaceController {
    @Autowired
    private NamespaceService namespaceService;
    @PostMapping("/create")
    public ResultVO create(@RequestBody Map map) throws Exception {
        String name=map.get("name").toString();
        String namespace=map.get("namespaceName").toString();
        String apiVersion=map.get("apiVersion").toString();
        final String labelName=map.get("labels").toString();

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

        Namespace namespace1=new Namespace();
        namespace1.setApiVersion(apiVersion);
        namespace1.setMetadata(objectMeta);
        return namespaceService.create(namespace1);
    }
    @PostMapping("delete")
    public ResultVO delete(@RequestBody Map map) {
        String namespaceName = map.get("namespaceName").toString();
        return namespaceService.delete(namespaceName);
    }

    @GetMapping("getByNamespaceName")
    public ResultVO getByNamespaceName(String namespaceName){
        return namespaceService.getByNamespaceName(namespaceName);
    }

    @GetMapping("getAll")
    public ResultVO getAll(){
        return namespaceService.getAll();
    }
}
