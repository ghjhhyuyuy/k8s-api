package team.cloud.service;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import team.cloud.entity.vo.ResultVO;
import team.cloud.k8s.Fabric8;
import team.cloud.k8s.K8sNamespaces;
import team.cloud.util.ResultVOUtil;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wzw on 2019/5/21
 *
 * @Author wzw
 */
@Service
public class NamespaceService {
    @Resource
    private Fabric8 fabric8;
    private ResultVOUtil resultVOUtil;
    @Resource
    private K8sNamespaces k8sNamespaces;
    private static final Logger logger = LogManager.getLogger(NamespaceService.class);
    public ResultVO create(Namespace namespace) {
        KubernetesClient client = fabric8.connect();
        Boolean result;
        try{
            result = k8sNamespaces.createNamespace(client,namespace);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().Fail("创建Namespace失败，请检查参数");
        }
        ResultVO resultVO = new ResultVOUtil().success();
        resultVO.setData(result);
        return resultVO;
    }

    public ResultVO delete(String namespaceName){
        KubernetesClient client = fabric8.connect();
        Boolean result;
        try{
            result = k8sNamespaces.deleteNamespace(client,namespaceName);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().resourceNotFound();
        }
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(result);
        return resultVO;
    }

    public ResultVO getByNamespaceName(String namespaceName){
        KubernetesClient client = fabric8.connect();
        Namespace result;
        try{
            result = k8sNamespaces.getNamespace(client,namespaceName);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().resourceNotFound();
        }
        JSONObject list = setNamespace(result);
        return new ResultVOUtil().success(list.toString());
    }
    public ResultVO getAll(){
        KubernetesClient client = fabric8.connect();
        NamespaceList result;
        try{
            result = k8sNamespaces.listNamespaces(client);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().Fail("查找失败，请联系管理员");
        }
        List<Namespace> list = result.getItems();
        JSONArray resultAll = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject list1 = setNamespace(list.get(i));
            resultAll.put(list1);
        }
        return new ResultVOUtil().success(resultAll.toString());
    }
    private JSONObject setNamespace(Namespace result){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ApiVersion",result.getApiVersion());
        jsonObject.put("Kind",result.getKind());
        jsonObject.put("Name",result.getMetadata().getName());
        if(result.getMetadata().getLabels()!=null){
            jsonObject.put("Labels",result.getMetadata().getLabels().toString());
        }else {
            jsonObject.put("Labels","");
        }
        if(result.getMetadata().getAnnotations()!=null){
            jsonObject.put("Annotations",result.getMetadata().getAnnotations().toString());
        }else {
            jsonObject.put("Annotations","");
        }
        return jsonObject;
    }
}
