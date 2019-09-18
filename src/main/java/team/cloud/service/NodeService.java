package team.cloud.service;

import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeList;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import team.cloud.entity.vo.ResultVO;
import team.cloud.k8s.Fabric8;
import team.cloud.k8s.K8sNodes;
import team.cloud.util.ResultVOUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzw on 2019/5/21
 *
 * @Author wzw
 */
@Service
public class NodeService {
    @Resource
    private Fabric8 fabric8;
    private ResultVOUtil resultVOUtil;
    @Resource
    private K8sNodes k8sNodes;
    public ResultVO listNodes(){
        KubernetesClient client = fabric8.connect();
        NodeList result;
        try{
            result = k8sNodes.listNodes(client);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().Fail("获取节点信息失败");
        }
        List<Node> list = result.getItems();
        JSONArray resultAll = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject list1 = setNode(list.get(i));
            resultAll.put(list1);
        }
        return new ResultVOUtil().success(resultAll.toString());
    }
    public ResultVO getNode(String nodeName){
        KubernetesClient client = fabric8.connect();
        Node result;
        try{
            result = k8sNodes.getNode(client,nodeName);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().resourceNotFound();
        }
        JSONObject list = setNode(result);
        return new ResultVOUtil().success(list.toString());
    }
    private JSONObject setNode(Node result){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ApiVersion",result.getApiVersion());
        jsonObject.put("Kind",result.getKind());
        jsonObject.put("Name",result.getMetadata().getName());
        jsonObject.put("Namespace",result.getMetadata().getNamespace());
        jsonObject.put("Labels",result.getMetadata().getLabels().toString());
        return jsonObject;
    }
}
