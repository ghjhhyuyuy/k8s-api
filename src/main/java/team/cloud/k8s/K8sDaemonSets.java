package team.cloud.k8s;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.api.model.apps.DaemonSetBuilder;
import io.fabric8.kubernetes.api.model.apps.DaemonSetList;
import io.fabric8.kubernetes.api.model.apps.DaemonSetSpec;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.dsl.base.HasMetadataOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ernest ljw9712@163.com
 * @date 2019/3/3
 */
@Component
public class K8sDaemonSets {

    private static final Logger logger = LogManager.getLogger(K8sDaemonSets.class);

    /**
     * 获取所有daemonSet信息
     *
     * @param client KubernetesClient
     * @return DaemonSetList
     */
    public DaemonSetList listDaemonSets(KubernetesClient client){
        try{
            logger.info("listDaemonSets成功");
            return client.apps().daemonSets().list();
        }catch (KubernetesClientException e){
            logger.error("listDaemonSets失败");
            logger.error("错误信息：",e.getMessage());
            return null;
        }
    }

    /**
     * 获取某一个namespace下的所有daemonSet信息
     *
     * @param client KubernetesClient
     * @param namespaceName namespaceName
     * @return DaemonSetList
     */
    public DaemonSetList listDaemonSetsFromNamespace(KubernetesClient client, String namespaceName){
        try{
            logger.info("listDaemonSetsFromNamespace成功");
            return client.apps().daemonSets().inNamespace(namespaceName).list();
        }catch (KubernetesClientException e){
            logger.error("listDaemonSetsFromNamespace失败");
            logger.error("错误信息：",e.getMessage());
            return null;
        }
    }

    /**
     * 获取某一个namespace下的某一个daemonSet信息
     *
     * @param client KubernetesClient
     * @param namespaceName namespaceName
     * @param daemonSetName daemonSetName
     * @return DaemonSet
     */
    public DaemonSet getDaemonSet(KubernetesClient client, String namespaceName, String daemonSetName){
        try{
            logger.info("getDaemonSet成功");
            return client.apps().daemonSets().inNamespace(namespaceName).withName(daemonSetName).get();
        }catch (KubernetesClientException e){
            logger.error("getDaemonSet失败");
            logger.error("错误信息：",e.getMessage());
            return null;
        }
    }

    /**
     * 删除所有的daemonSet
     *
     * @param client KubernetesClient
     * @return boolean
     */
    public boolean deleteAllDaemonSets(KubernetesClient client){
        try{
            client.apps().daemonSets().delete();
            logger.info("deleteAllDaemonSets成功");
            return true;
        }catch (KubernetesClientException e){
            logger.error("deleteAllDaemonSets失败");
            logger.error("错误信息：",e.getMessage());
            return false;
        }
    }

    /**
     * 删除某一个namespace下的所有daemonSet
     *
     * @param client KubernetesClient
     * @param namespaceName namespaceName
     * @return boolean
     */
    public boolean deleteDaemonSets(KubernetesClient client, String namespaceName){
        try{
            client.apps().daemonSets().inNamespace(namespaceName).delete();
            logger.info("deleteDaemonSets成功");
            return true;
        }catch (KubernetesClientException e){
            logger.error("deleteDaemonSets失败");
            logger.error("错误信息：",e.getMessage());
            return false;
        }
    }

    /**
     * 删除某一个namespace下的某一个daemonSet
     *
     * @param client KubernetesClient
     * @param namespaceName namespaceName
     * @param daemonSetName daemonSetName
     * @return boolean
     */
    public boolean deleteDaemonSet(KubernetesClient client, String namespaceName, String daemonSetName){
        try{
            client.apps().daemonSets().inNamespace(namespaceName).withName(daemonSetName).delete();
            logger.info("deleteDaemonSet成功");
            return true;
        }catch (KubernetesClientException e){
            logger.error("deleteDaemonSet失败");
            logger.error("错误信息：",e.getMessage());
            return false;
        }
    }


    /**
     * 创建一个daemonSet
     * @param client KubernetesClient
     * @param daemonSet DaemonSet
     * @return boolean
     */

    public boolean createDaemonSet(KubernetesClient client,DaemonSet daemonSet)
    {
        String apiVersion=daemonSet.getApiVersion();
        String kind="DaemonSet";
        String namespace=daemonSet.getMetadata().getNamespace();
        String name=daemonSet.getMetadata().getName();
        Map<String,String> labels=daemonSet.getMetadata().getLabels();
//        Map<String,String> selectorLables=daemonSet.getSpec().getSelector().getMatchLabels();
//        Map<String,String> tempLables=daemonSet.getSpec().getTemplate().getMetadata().getLabels();
       List<Container> containers=daemonSet.getSpec().getTemplate().getSpec().getContainers();
//        Container container=daemonSet.getSpec().getTemplate().getSpec().getContainers().get(0);
        List<Volume> volumes=daemonSet.getSpec().getTemplate().getSpec().getVolumes();

        try{

            DaemonSet createDaemonSet=new DaemonSetBuilder()
                    .withApiVersion(apiVersion)
                    .withKind(kind)
                    .withNewMetadata()
                    .withName(name)
                    .withNamespace(namespace)
                    .endMetadata()
                    .withNewSpec()
                    .withNewSelector()
                    .withMatchLabels(labels)
                    .endSelector()
                    .withNewTemplate()
                    .withNewMetadata()
                    .withLabels(labels)
                    .endMetadata()
                    .withNewSpec()
                    .withContainers(containers)
                    .withVolumes(volumes)
                    .endSpec()
                    .endTemplate()
                    .endSpec()
                    .build();
            logger.error("createDaemonSet成功");
            client.apps().daemonSets().inNamespace(namespace).create(createDaemonSet);
            return true;

        }catch (KubernetesClientException e)
        {
            logger.error("createDaemonSet失败");
            logger.error("错误信息：",e.getMessage());
            e.printStackTrace();
            return false;
        }

    }

    public boolean updateDaemonSet(KubernetesClient client,DaemonSet daemonSet) {
        String apiVersion = daemonSet.getApiVersion();
        String kind = "DaemonSet";
        String namespace = daemonSet.getMetadata().getNamespace();
        String name = daemonSet.getMetadata().getName();
        Map<String, String> labels = daemonSet.getMetadata().getLabels();
        List<Container> containers = daemonSet.getSpec().getTemplate().getSpec().getContainers();
        List<Volume> volumes = daemonSet.getSpec().getTemplate().getSpec().getVolumes();
        try {
            DaemonSet patchDaemonSet = new DaemonSetBuilder()
                    .withApiVersion(apiVersion)
                    .withKind(kind)
                    .withNewMetadata()
                    .withName(name)
                    .withNamespace(namespace)
                    .endMetadata()
                    .withNewSpec()
                    .withNewSelector()
                    .withMatchLabels(labels)
                    .endSelector()
                    .withNewTemplate()
                    .withNewMetadata()
                    .withLabels(labels)
                    .endMetadata()
                    .withNewSpec()
                    .withContainers(containers)
                    .withVolumes(volumes)
                    .endSpec()
                    .endTemplate()
                    .endSpec()
                    .build();
            HasMetadataOperation hasMetadataOperation = (HasMetadataOperation) client.apps().daemonSets();
            hasMetadataOperation.patch(patchDaemonSet);
            logger.info("更新成功");
            return true;

        } catch (KubernetesClientException e) {
            logger.error("更新失败");
            logger.error("错误信息：", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }}
