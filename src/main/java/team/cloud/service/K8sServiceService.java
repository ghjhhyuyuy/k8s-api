package team.cloud.service;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import team.cloud.entity.vo.ResultVO;
import team.cloud.k8s.Fabric8;
import team.cloud.k8s.K8sDeployments;
import team.cloud.k8s.K8sServices;
import team.cloud.util.ResultVOUtil;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author tzw
 * CreateTime 20:46 2019/5/20
 **/

@org.springframework.stereotype.Service
public class K8sServiceService {
    @Resource
    private Fabric8 fabric8;
    private ResultVOUtil resultVOUtil;
    @Resource
    private K8sServices k8sServices;

    public ResultVO create(@Valid Service service) {
        KubernetesClient client = fabric8.connect();
        Boolean result;
        try{
            result = k8sServices.createService(client,service);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().Fail("创建Service失败，请检查参数");
        }
        ResultVO resultVO = new ResultVOUtil().success();
        resultVO.setData(result);
        return resultVO;
    }

    public ResultVO deleteServiceOneByNamespace(String namespaceName,String service){
        KubernetesClient client = fabric8.connect();
        Boolean result;
        try{
            result = k8sServices.deleteService(client,namespaceName,service);
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
            result = k8sServices.deleteServices(client,namespaceName);
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
            result = k8sServices.deleteAllServices(client);
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
    public ResultVO getOneByNamespace(String namespaceName,String service){
        KubernetesClient client = fabric8.connect();
        Service result;
        try{
            result = k8sServices.getService(client,namespaceName,service);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().resourceNotFound();
        }
        JSONObject list = setService(result);
        return new ResultVOUtil().success(list.toString());
    }
    public ResultVO getByNamespace(String namespaceName){
        KubernetesClient client = fabric8.connect();
        ServiceList result;
        try{
            result = k8sServices.listServicesFromNamespace(client,namespaceName);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().resourceNotFound();
        }
        List<Service> list = result.getItems();
        JSONArray resultAll = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject list1 = setService(list.get(i));
            resultAll.put(list1);
        }
        return new ResultVOUtil().success(resultAll.toString());
    }
    public ResultVO getList(){
        KubernetesClient client = fabric8.connect();
        ServiceList result;
        try{
            result = k8sServices.listServices(client);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().Fail("查找失败，请联系管理员");
        }
        List<Service> list = result.getItems();
        JSONArray resultAll = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject list1 = setService(list.get(i));
            resultAll.put(list1);
        }
        return new ResultVOUtil().success(resultAll.toString());
    }
    private JSONObject setService(Service result){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ApiVersion",result.getApiVersion());
        jsonObject.put("Kind",result.getKind());
        if(result.getMetadata()!=null){
            jsonObject.put("Name",result.getMetadata().getName());
            jsonObject.put("Namespace",result.getMetadata().getNamespace());
        }else {
            jsonObject.put("Name","");
            jsonObject.put("Namespace","");
        }
        if(result.getMetadata().getLabels()!=null){
            jsonObject.put("Labels",result.getMetadata().getLabels().toString());
        }else {
            jsonObject.put("Labels","");
        }
        if(result.getSpec().getPorts().get(0).getPort()!=null){
            jsonObject.put("Port",result.getSpec().getPorts().get(0).getPort().toString());
        }else {
            jsonObject.put("Port","");
        }
        if(result.getSpec().getPorts().get(0).getTargetPort()!=null){
            jsonObject.put("TargetPort",result.getSpec().getPorts().get(0).getTargetPort().toString());
        }else {
            jsonObject.put("TargetPort","");
        }
        if(result.getSpec().getPorts().get(0)!=null){
            jsonObject.put("Protocol",result.getSpec().getPorts().get(0).getProtocol());
        }else {
            jsonObject.put("Protocol","");
        }
        if(result.getSpec().getSelector()!=null){
            jsonObject.put("Selector",result.getSpec().getSelector().toString());
        }else {
            jsonObject.put("Selector","");
        }
        return jsonObject;
    }
}

