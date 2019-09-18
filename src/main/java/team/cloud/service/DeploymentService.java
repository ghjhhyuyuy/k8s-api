package team.cloud.service;

import io.fabric8.kubernetes.api.model.apps.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.cloud.entity.vo.ResultVO;
import team.cloud.k8s.Fabric8;
import team.cloud.k8s.K8sDeployments;
import team.cloud.util.ResultVOUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author tzw
 * CreateTime 10:37 2019/5/19
 **/
@Service
public class DeploymentService {

    @Resource
    private Fabric8 fabric8;
    @Resource
    private K8sDeployments k8sDeployments;
    private ResultVOUtil resultVOUtil;

    public ResultVO create(Deployment deployment) {
        KubernetesClient client = fabric8.connect();
        Boolean result;
        try{
            result = k8sDeployments.createDeployment(client,deployment);;
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().Fail("创建Deployment失败，请检查参数");
        }
        ResultVO resultVO = new ResultVOUtil().success();
        resultVO.setData(result);
        return resultVO;
    }
    public ResultVO deleteDeploymentOneByNamespace(String namespaceName,String deploymentName){
        KubernetesClient client = fabric8.connect();
        Boolean result;
        try{
            result = k8sDeployments.deleteDeployment(client,namespaceName,deploymentName);
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
            result = k8sDeployments.deleteDeployments(client,namespaceName);
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
            result = k8sDeployments.deleteAllDeployments(client);
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
    public ResultVO getOneByNamespace(String namespaceName,String deploymentName){
        KubernetesClient client = fabric8.connect();
        Deployment result;
        try{
            result = k8sDeployments.getDeployment(client,namespaceName,deploymentName);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().resourceNotFound();
        }
        JSONObject list = setDeployment(result);
        return new ResultVOUtil().success(list.toString());
    }
    public ResultVO getByNamespace(String namespaceName){
        KubernetesClient client = fabric8.connect();
        DeploymentList result;
        try{
            result = k8sDeployments.listDeploymentsFromNamespace(client,namespaceName);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().resourceNotFound();
        }
        List<Deployment> list = result.getItems();
        JSONArray resultAll = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject list1 = setDeployment(list.get(i));
            resultAll.put(list1);
        }
        return new ResultVOUtil().success(resultAll.toString());
    }
    public ResultVO getList(){
        KubernetesClient client = fabric8.connect();
        DeploymentList result;
        try{
            result = k8sDeployments.listDeployments(client);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().Fail("查找失败，请联系管理员");
        }
        List<Deployment> list = result.getItems();
        JSONArray resultAll = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject list1 = setDeployment(list.get(i));
            resultAll.put(list1);
        }
        return new ResultVOUtil().success(resultAll.toString());
    }
    private JSONObject setDeployment(Deployment result){
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
        if(result.getSpec().getReplicas()!=null){
            jsonObject.put("Replicas",result.getSpec().getReplicas().toString());
        }else {
            jsonObject.put("Replicas","");
        }
        if(result.getSpec().getSelector().getMatchLabels()!=null){
            jsonObject.put("MatchLabels",result.getSpec().getSelector().getMatchLabels().toString());
        }else {
            jsonObject.put("MatchLabels","");
        }
        if(result.getSpec().getTemplate().getMetadata().getLabels()!=null){
            jsonObject.put("TemplateLabels",result.getSpec().getTemplate().getMetadata().getLabels().toString());
        }else {
            jsonObject.put("TemplateLabels","");
        }
        if(result.getSpec().getTemplate().getSpec().getContainers().get(0)!=null){
            jsonObject.put("ContainersName",result.getSpec().getTemplate().getSpec().getContainers().get(0).getName());
            jsonObject.put("ContainersImage",result.getSpec().getTemplate().getSpec().getContainers().get(0).getImage());
        }else {
            jsonObject.put("ContainersName","");
            jsonObject.put("ContainersImage","");
        }
        if(result.getSpec().getTemplate().getSpec().getContainers().get(0).getPorts().get(0).getContainerPort()!=null){
            jsonObject.put("ContainerPort",result.getSpec().getTemplate().getSpec().getContainers().get(0).getPorts().get(0).getContainerPort().toString());
        }else {
            jsonObject.put("ContainerPort","");
        }
        return jsonObject;
    }
}
