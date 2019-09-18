package team.cloud.k8s;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.base.HasMetadataOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 包子戴
 * @date 2019/03/27
 *
 * configMaps-api
 */
@Component
public class K8sConfigMaps {
    private static final Logger logger = LogManager.getLogger(K8sConfigMaps.class);

    /**
    * @Description:  删除某一个namespace下的某个configmap
    * @param client KubernetesClient
    * @param namespaceName namespaceName
    * @param configMapName configMapName
    * @return:  boolean
    * @Author: 戴龙至
    * @Date: 2019/3/27
    */

    public boolean deleteConfigMap(KubernetesClient client, String namespaceName, String configMapName){
        try{
            client.configMaps().inNamespace(namespaceName).withName(configMapName).delete();
            logger.info("deleteConfigMap成功");
            return true;
        }catch (KubernetesClientException e){
            logger.error("deleteConfigMap失败");
            logger.error("错误信息：", e.getMessage());
            return false;
        }
    }

     /**
     * @Description 删除某个namespace下的所有的configmap
     * @param client KubernetesClient
     * @param namespaceName namespaceName
     * @return boolean
     * @Author 戴龙至
     * @Date 2019/3/27
     */
     public boolean deleteConfigMaps(KubernetesClient client, String namespaceName){
         try{
             client.configMaps().inNamespace(namespaceName).delete();
             logger.info("deleteConfigMaps成功（namespace下）");
             return true;
         }catch (KubernetesClientException e){
             logger.error("deleteConfigMaps失败（namespace下）");
             logger.error("错误信息：",e.getMessage());
             return false;
         }
     }

    /**
     * @Description 删除所有的configmap（全局操作）
     * @param client KubernetesClient
     * @return boolean
     * @Author 戴龙至
     * @Date 2019/5/13
     */
    public boolean deleteAllConfigMaps(KubernetesClient client)
    {
        try
        {
            client.configMaps().inAnyNamespace().delete();
            logger.info("deleteConfigMaps成功(全局)");
            return true;
        }
        catch (KubernetesClientException e)
        {
            logger.error("deleteConfigMaps失败(全局)");
            logger.error("错误信息：",e.getMessage());
            return false;
        }

    }
    public ConfigMap getConfigMap(KubernetesClient client, String namespaceName, String configMapName){
        try{
            logger.info("getConfigMap成功");
            return client.configMaps().inNamespace(namespaceName).withName(configMapName).get();
        }catch (KubernetesClientException e){
            logger.error("getConfigMap失败");
            logger.error("错误信息：",e.getMessage());
            return null;
        }
    }

    public ConfigMapList listConfigMapsFromNamespace(KubernetesClient client, String namespaceName){
        try{
            logger.info("getConfigMap成功");
            return client.configMaps().inNamespace(namespaceName).list();
        }catch (KubernetesClientException e){
            logger.error("getConfigMap失败");
            logger.error("错误信息：",e.getMessage());
            return null;
        }
    }

    public ConfigMapList listConfigMaps(KubernetesClient client){
        try{
            logger.info("listConfigMaps成功");
            return client.configMaps().list();
        }catch (KubernetesClientException e){
            logger.error("listConfigMaps失败");
            logger.error("错误信息：",e.getMessage());
            return null;
        }
    }

    public boolean createConfigMap(KubernetesClient client,ConfigMap configMap)
    {
        String apiVersion=configMap.getApiVersion();
        String kind="ConfigMap";
        String namespace=configMap.getMetadata().getNamespace();
        String name=configMap.getMetadata().getName();
        Map<String,String> labels=configMap.getMetadata().getLabels();
        Map<String,String> data= configMap.getData();

        try{

            ConfigMap createConfigMap=new ConfigMapBuilder()
                    .withApiVersion(apiVersion)
                    .withKind(kind)
                    .withNewMetadata()
                    .withName(name)
                    .withNamespace(namespace)
                    .withLabels(labels)
                    .endMetadata()
                    .withData(data)
                    .build();
            logger.error("createConfigMap成功");
            client.configMaps().inNamespace(namespace).create(createConfigMap);
            return true;

        }catch (KubernetesClientException e)
        {
            logger.error("createConfigMap失败");
            logger.error("错误信息：",e.getMessage());
            e.printStackTrace();
            return false;
        }

    }
//    public boolean updateAllConfigMaps(KubernetesClient client,ConfigMap configMap)
//        {
//        try
//        {
//            HasMetadataOperation hasMetadataOperation = (HasMetadataOperation)client.configMaps();
//            hasMetadataOperation.patch(configMap);
//            logger.info("更新成功");
//            return true;
//        }
//        catch (KubernetesClientException e)
//        {
//            logger.error("更新失败");
//            logger.error("错误信息：",e.getMessage());
//            return false;
//        }
//
//    }
}
