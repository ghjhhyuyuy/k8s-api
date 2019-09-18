package team.cloud.service;

import io.fabric8.kubernetes.api.model.HostPathVolumeSource;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import team.cloud.entity.vo.ResultVO;
import team.cloud.k8s.Fabric8;
import team.cloud.k8s.K8sPods;
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
public class PodService {
    @Resource
    private Fabric8 fabric8;
    private ResultVOUtil resultVOUtil;
    @Resource
    private K8sPods k8sPods;
    public ResultVO getPod(String namespaceName,String podName){
        KubernetesClient client = fabric8.connect();
        Pod result;
        try{
            result = k8sPods.getPod(client,namespaceName,podName);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().resourceNotFound();
        }
        JSONObject list = setPod(result);
        return new ResultVOUtil().success(list.toString());
    }
    public ResultVO listPodsFromNamespace(String namespaceName){
        KubernetesClient client = fabric8.connect();
        PodList result;
        try{
            result = k8sPods.listPodsFromNamespace(client,namespaceName);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().resourceNotFound();
        }
        List<Pod> list = result.getItems();
        JSONArray resultAll = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject list1 = setPod(list.get(i));
            resultAll.put(list1);
        }
        return new ResultVOUtil().success(resultAll.toString());
    }
    public ResultVO deletePod(String namespaceName,String podName){
        KubernetesClient client = fabric8.connect();
        Boolean result;
        try{
            result = k8sPods.deletePod(client,namespaceName,podName);
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
    public ResultVO createPod(Pod pod){
        KubernetesClient client = fabric8.connect();
        Boolean result;
        try{
            result = k8sPods.createPod(client,pod);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().Fail("创建Pod失败，请检查参数");
        }
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(result);
        return resultVO;
    }
    public ResultVO deletePods(String namespaceName){
        KubernetesClient client = fabric8.connect();
        Boolean result;
        try{
            result = k8sPods.deletePods(client,namespaceName);
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
            result = k8sPods.deleteAllPods(client);
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
    public ResultVO getAll(){
        KubernetesClient client = fabric8.connect();
        PodList result;
        try{
            result = k8sPods.listPods(client);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().Fail("查找失败，请联系管理员");
        }
        List<Pod> list = result.getItems();
        JSONArray resultAll = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject list1 = setPod(list.get(i));
            resultAll.put(list1);
        }
        return new ResultVOUtil().success(resultAll.toString());
    }
    private JSONObject setPod(Pod result){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ApiVersion",result.getApiVersion());
        jsonObject.put("Kind",result.getKind());
        jsonObject.put("Name",result.getMetadata().getName());
        jsonObject.put("Namespace",result.getMetadata().getNamespace());
        if(result.getMetadata().getLabels()!=null){
            jsonObject.put("Labels",result.getMetadata().getLabels().toString());
        }else {
            jsonObject.put("Labels","");
        }
        if(result.getSpec().getContainers().get(0)!=null){
            jsonObject.put("ContainersName",result.getSpec().getContainers().get(0).getName());
            jsonObject.put("ContainersImage",result.getSpec().getContainers().get(0).getImage());
        }else {
            jsonObject.put("ContainersName","");
            jsonObject.put("ContainersImage","");
        }
        if(result.getSpec().getNodeSelector()!=null){
            jsonObject.put("NodeSelector",result.getSpec().getNodeSelector().toString());
        }else {
            jsonObject.put("NodeSelector","");
        }
        if(result.getMetadata().getAnnotations()!=null){
            jsonObject.put("Annotations",result.getMetadata().getAnnotations().toString());
        }else {
            jsonObject.put("Annotations","");
        }
        if(result.getSpec().getContainers().get(0).getEnv()!=null){
            jsonObject.put("Env",result.getSpec().getContainers().get(0).getEnv().toString());
        }else {
            jsonObject.put("Env","");
        }
        if(result.getSpec().getContainers().get(0)!=null){
            jsonObject.put("ImagePullPolicy",result.getSpec().getContainers().get(0).getImagePullPolicy());
        }else {
            jsonObject.put("ImagePullPolicy","");
        }
        if(result.getSpec().getContainers().get(0).getResources().getRequests()!=null){
            jsonObject.put("Requests",result.getSpec().getContainers().get(0).getResources().getRequests().toString());
        }else {
            jsonObject.put("Requests","");
        }
        if(result.getSpec().getContainers().get(0).getResources().getLimits()!=null){
            jsonObject.put("Limits",result.getSpec().getContainers().get(0).getResources().getLimits().toString());
        }else {
            jsonObject.put("Limits","");
        }
        if(result.getSpec().getContainers().get(0).getPorts().size()!=0){
            if(result.getSpec().getContainers().get(0).getPorts().get(0).getContainerPort()!=null){
                jsonObject.put("ContainerPort",result.getSpec().getContainers().get(0).getPorts().get(0).getContainerPort().toString());
            }else {
                jsonObject.put("ContainerPort","");
            }
            jsonObject.put("PortsName",result.getSpec().getContainers().get(0).getPorts().get(0).getName());
            jsonObject.put("Protocol",result.getSpec().getContainers().get(0).getPorts().get(0).getProtocol());
        }else {
            jsonObject.put("PortsName","");
            jsonObject.put("Protocol","");
        }
        if(result.getSpec().getContainers().get(0).getVolumeMounts().get(0)!=null){
            jsonObject.put("VolumeMountsName",result.getSpec().getContainers().get(0).getVolumeMounts().get(0).getName());
            jsonObject.put("MountPath",result.getSpec().getContainers().get(0).getVolumeMounts().get(0).getMountPath());
        }else {
            jsonObject.put("VolumeMountsName","");
            jsonObject.put("MountPath","");
        }
        if(result.getSpec().getContainers().get(0).getVolumeMounts().get(0).getReadOnly()!=null){
            jsonObject.put("ReadOnly",result.getSpec().getContainers().get(0).getVolumeMounts().get(0).getReadOnly().toString());
        }else {
            jsonObject.put("ReadOnly","");
        }
        if(result.getSpec().getVolumes().get(0)!=null){
            jsonObject.put("VolumesName",result.getSpec().getVolumes().get(0).getName());
        }else {
            jsonObject.put("VolumesName","");
        }
        if(result.getSpec().getVolumes().get(0).getHostPath()!=null){
            jsonObject.put("HostPath",result.getSpec().getVolumes().get(0).getHostPath().toString());
        }else {
            jsonObject.put("HostPath","");
        }
        return jsonObject;
    }
}
