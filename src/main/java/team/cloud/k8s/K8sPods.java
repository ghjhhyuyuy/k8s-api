package team.cloud.k8s;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Ernest ljw9712@163.com
 * @date 2019/3/3
 *
 * pods-api
 */
@Component
public class K8sPods {

    private static final Logger logger = LogManager.getLogger(K8sPods.class);

    /**
     * 获取所有pod信息
     *
     * @param client KubernetesClient
     * @return PodList
     */
    public PodList listPods(KubernetesClient client){
        try{
            logger.info("listPods成功");
            return client.pods().list();
        }catch (KubernetesClientException e){
            logger.error("listPods失败");
            logger.error("错误信息：",e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取某一个namespace下的所有pod信息
     *
     * @param client KubernetesClient
     * @param namespaceName namespaceName
     * @return PodList
     */
    public PodList listPodsFromNamespace(KubernetesClient client, String namespaceName){
        try{
            logger.info("listPodsFromNamespace成功");
            return client.pods().inNamespace(namespaceName).list();
        }catch (KubernetesClientException e){
            logger.error("listPodsFromNamespace失败");
            logger.error("错误信息：",e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取某一个namespace下的某一个pod信息
     *
     * @param client KubernetesClient
     * @param namespaceName namespaceName
     * @param podName podName
     * @return Pod
     */
    public Pod getPod(KubernetesClient client, String namespaceName, String podName){
        try{
            logger.info("getPod成功");
            return client.pods().inNamespace(namespaceName).withName(podName).get();
        }catch (KubernetesClientException e){
            logger.error("getPod失败");
            logger.error("错误信息：",e.getMessage());
            return null;
        }
    }

    /**
     * 删除所有的pod
     *
     * @param client KubernetesClient
     * @return boolean
     */
    public boolean deleteAllPods(KubernetesClient client){
        try{
            client.pods().delete();
            logger.info("deleteAllPods成功");
            return true;
        }catch (KubernetesClientException e){
            logger.error("deleteAllPods失败");
            logger.error("错误信息：",e.getMessage());
            return false;
        }
    }

    /**
     * 删除某一个namespace下的所有pod
     *
     * @param client KubernetesClient
     * @param namespaceName namespaceName
     * @return boolean
     */
    public boolean deletePods(KubernetesClient client, String namespaceName){
        try{
            client.pods().inNamespace(namespaceName).delete();
            logger.info("deletePods成功");
            return true;
        }catch (KubernetesClientException e){
            logger.error("deletePods失败");
            logger.error("错误信息：",e.getMessage());
            return false;
        }
    }

    /**
     * 删除某一个namespace下的某一个pod
     *
     * @param client KubernetesClient
     * @param namespaceName namespaceName
     * @param podName podName
     * @return boolean
     *
     */
    public boolean deletePod(KubernetesClient client, String namespaceName, String podName){
        try{
            client.pods().inNamespace(namespaceName).withName(podName).delete();
            logger.info("deletePod成功");
            return true;
        }catch (KubernetesClientException e){
            logger.error("deletePod失败");
            logger.error("错误信息：",e.getMessage());
            return false;
        }
    }

    /**
     * 创建pod
     *
     * @param client KubernetesClient
     * @param pod pod对象
     * @return boolean
     */
    public boolean createPod(KubernetesClient client, Pod pod){
        String namespaceName = pod.getMetadata().getNamespace();
        String podName = pod.getMetadata().getName();
        String apiVersion=pod.getApiVersion();
        Map<String, String> labels = pod.getMetadata().getLabels();
        //Map<String, String> annotations = pod.getMetadata().getAnnotations();
        //Map<String, String> nodeSelector = pod.getSpec().getNodeSelector();
        String containerName = pod.getSpec().getContainers().get(0).getName();
        String imageAddress = pod.getSpec().getContainers().get(0).getImage();
        /* 三个选择Always、Never、IfNotPresent */
        //String imagePullPolicy = pod.getSpec().getContainers().get(0).getImagePullPolicy();
        //List<EnvVar> envVars = pod.getSpec().getContainers().get(0).getEnv();
        /* 容器运行时，最低资源需求，也就是说最少需要多少资源容器才能正常运行 */
       // Map<String, Quantity> requests = pod.getSpec().getContainers().get(0).getResources().getRequests();
        /* 资源限制 */
       // Map<String, Quantity> limits = pod.getSpec().getContainers().get(0).getResources().getLimits();
        /* 容器开发对外的端口 */
        Integer containerPort = pod.getSpec().getContainers().get(0).getPorts().get(0).getContainerPort();
       // String portName = pod.getSpec().getContainers().get(0).getPorts().get(0).getName();
        //String protocol = pod.getSpec().getContainers().get(0).getPorts().get(0).getProtocol();
        //String volumeMountsName = pod.getSpec().getContainers().get(0).getVolumeMounts().get(0).getName();
        //String mountPath = pod.getSpec().getContainers().get(0).getVolumeMounts().get(0).getMountPath();
        //Boolean readOnly = pod.getSpec().getContainers().get(0).getVolumeMounts().get(0).getReadOnly();
        //String volumesName = pod.getSpec().getVolumes().get(0).getName();
        //HostPathVolumeSource hostName = pod.getSpec().getVolumes().get(0).getHostPath();

        try{
            Pod createPod = new PodBuilder()
                    .withApiVersion(apiVersion)
                    .withNewMetadata()
                    .withNamespace(namespaceName)
                    .withName(podName)
                    .withLabels(labels)
                    //.withAnnotations(annotations)
                    .endMetadata()
                    .withNewSpec()
                    //.withNodeSelector(nodeSelector)
                    .addNewContainer()
                    .withName(containerName)
                    .withImage(imageAddress)
                    //.withImagePullPolicy(imagePullPolicy)
                    //.withEnv(envVars)
                    .withNewResources()
                    //.withRequests(requests)
                    //.withLimits(limits)
                    .endResources()
                    .addNewPort()
                    .withContainerPort(containerPort)
                    //.withName(portName)
                    //.withProtocol(protocol)
                    .endPort()
                    //.addNewVolumeMount()
                    //.withName(volumeMountsName)
                    //.withMountPath(mountPath)
                    //.withReadOnly(readOnly)
                    //.endVolumeMount()
                    .endContainer()
                    //.addNewVolume()
                    //.withName(volumesName)
                    //.withHostPath(hostName)
                    //.endVolume()
                    .endSpec()
                    .build();

            pod = client.pods().inNamespace(namespaceName).create(pod);
            logger.info("createPod成功");
            return true;
        }catch (KubernetesClientException e){
            logger.error("createPod失败");
            logger.error("错误信息：",e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
