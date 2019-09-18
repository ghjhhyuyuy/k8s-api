package team.cloud.service;

import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.api.model.apps.DaemonSetList;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import team.cloud.entity.vo.ResultVO;
import team.cloud.k8s.Fabric8;
import team.cloud.k8s.K8sDaemonSets;
import team.cloud.util.ResultVOUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DaemonSetService {

    @Resource
    private Fabric8 fabric8;
    @Resource
    private K8sDaemonSets k8sDaemonSets;

    private ResultVOUtil resultVOUtil;


    public ResultVO create(DaemonSet daemonSet) {
        KubernetesClient client = fabric8.connect();
        Boolean result;
        try{
            result = k8sDaemonSets.createDaemonSet(client,daemonSet);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().Fail("创建DaemonSet失败，请检查参数");
        }
        ResultVO resultVO = new ResultVOUtil().success();
        resultVO.setData(result);
        return resultVO;
    }


    public ResultVO deleteDaemonSetOneByNamespace(String namespaceName,String daemonSetName){
        KubernetesClient client = fabric8.connect();
        Boolean result;
        try{
            result = k8sDaemonSets.deleteDaemonSet(client,namespaceName,daemonSetName);
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
   public ResultVO deleteListByNamespace(String namespaceName){
       KubernetesClient client = fabric8.connect();
       Boolean result;
       try{
           result = k8sDaemonSets.deleteDaemonSets(client,namespaceName);
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
    public ResultVO deleteAll(){
        KubernetesClient client = fabric8.connect();
        Boolean result;
        try{
            result = k8sDaemonSets.deleteAllDaemonSets(client);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().Fail("删除失败，请联系管理员");
        }
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(result);
        return resultVO;
    }
    public ResultVO getOneByNamespace(String namespaceName,String daemonSetName){
        KubernetesClient client = fabric8.connect();
        DaemonSet result;
        try{
            result = k8sDaemonSets.getDaemonSet(client,namespaceName,daemonSetName);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().resourceNotFound();
        }
        JSONObject list = setDaemonSet(result);
        return new ResultVOUtil().success(list.toString());
    }
    public ResultVO getByNamespace(String namespaceName){
        KubernetesClient client = fabric8.connect();
        DaemonSetList result;
        try{
            result = k8sDaemonSets.listDaemonSetsFromNamespace(client,namespaceName);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().resourceNotFound();
        }
        List<DaemonSet> list = result.getItems();
        JSONArray resultAll = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject list1 = setDaemonSet(list.get(i));
            resultAll.put(list1);
        }
        return new ResultVOUtil().success(resultAll.toString());
    }
    public ResultVO getList(){
        KubernetesClient client = fabric8.connect();
        DaemonSetList result;
        try{
            result = k8sDaemonSets.listDaemonSets(client);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().Fail("查找失败，请联系管理员");
        }
        List<DaemonSet> list = result.getItems();
        JSONArray resultAll = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject list1 = setDaemonSet(list.get(i));
            resultAll.put(list1);
        }
        return new ResultVOUtil().success(resultAll.toString());
    }
    private JSONObject setDaemonSet(DaemonSet result){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ApiVersion",result.getApiVersion());
        jsonObject.put("Kind",result.getKind());
        jsonObject.put("Namespace",result.getMetadata().getNamespace());
        jsonObject.put("Name",result.getMetadata().getName());
        if(result.getMetadata().getLabels()!=null){
            jsonObject.put("Labels",result.getMetadata().getLabels().toString());
        }else {
            jsonObject.put("Labels","");
        }
        if(result.getSpec().getTemplate().getSpec().getContainers()!=null){
            jsonObject.put("Containers",result.getSpec().getTemplate().getSpec().getContainers().toString());
        }else {
            jsonObject.put("Containers","");
        }
        if(result.getSpec().getTemplate().getSpec().getVolumes()!=null){
            jsonObject.put("Volumes",result.getSpec().getTemplate().getSpec().getVolumes().toString());
        }else {
            jsonObject.put("Volumes","");
        }
        return jsonObject;
    }
}
