package team.cloud.service;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapList;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.api.model.apps.DaemonSetList;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.cloud.entity.vo.ResultVO;
import team.cloud.k8s.Fabric8;
import team.cloud.k8s.K8sConfigMaps;
import team.cloud.util.ResultVOUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wzw on 2019/5/18
 *
 * @Author dlz
 */
@Service
public class ConfigMapService {
    @Resource
    private Fabric8 fabric8;
    @Resource
    private K8sConfigMaps k8sConfigMaps;
    private ResultVOUtil resultVOUtil;
    public ResultVO create(ConfigMap configMap) {
        KubernetesClient client = fabric8.connect();
        Boolean result;
        try{
            result = k8sConfigMaps.createConfigMap(client,configMap);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().Fail("创建ConfigMap失败，请检查参数");
        }
        ResultVO resultVO = new ResultVOUtil().success();
        resultVO.setData(result);
        return resultVO;
    }
    public ResultVO deleteConfigMap(String namespaceName,String configMapName)
    {
        Fabric8 fabric8 = new Fabric8();
        KubernetesClient client = fabric8.connect();
        Boolean result;
        try{
            result = k8sConfigMaps.deleteConfigMap(client,namespaceName,configMapName);
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

    public ResultVO deleteConfigMaps(String namespaceName)
    {
        Fabric8 fabric8 = new Fabric8();
        KubernetesClient client = fabric8.connect();
        Boolean result;
        try{
            result = k8sConfigMaps.deleteConfigMaps(client,namespaceName);
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
    public ResultVO deleteAllConfigMaps()
    {
        Fabric8 fabric8 = new Fabric8();
        KubernetesClient client = fabric8.connect();
        Boolean result;
        try{
            result = k8sConfigMaps.deleteAllConfigMaps(client);
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
        ConfigMap result;
        try{
            result = k8sConfigMaps.getConfigMap(client,namespaceName,daemonSetName);;
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().resourceNotFound();
        }
        JSONObject list = setConfigMap(result);
        return new ResultVOUtil().success(list.toString());
    }
    public ResultVO getByNamespace(String namespaceName){
        KubernetesClient client = fabric8.connect();
        ConfigMapList result  = k8sConfigMaps.listConfigMapsFromNamespace(client,namespaceName);
        if(result==null){
            return new ResultVOUtil().resourceNotFound();
        }
        List<ConfigMap> list = result.getItems();
        JSONArray resultAll = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject list1 = setConfigMap(list.get(i));
            resultAll.put(list1);
        }
        return new ResultVOUtil().success(resultAll.toString());
    }
    public ResultVO getList(){
        KubernetesClient client = fabric8.connect();
        ConfigMapList result;
        try{
            result = k8sConfigMaps.listConfigMaps(client);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().Fail("查找失败，请联系管理员");
        }
        List<ConfigMap> list = result.getItems();
        JSONArray resultAll = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject list1 = setConfigMap(list.get(i));
            resultAll.put(list1);
        }
        return new ResultVOUtil().success(resultAll.toString());
    }
    private JSONObject setConfigMap(ConfigMap result){
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
        if(result.getData()!=null){
            jsonObject.put("Data",result.getData().toString());
        }else {
            jsonObject.put("Data","");
        }
        return jsonObject;
    }
}
