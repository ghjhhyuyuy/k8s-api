package team.cloud.k8s;



import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.dsl.base.HasMetadataOperation;
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
 * deployments-api
 */
@Component
public class K8sDeployments {

    private static final Logger logger = LogManager.getLogger(K8sDeployments.class);

    /**
     * 获取所有deployment信息
     *
     * @param client KubernetesClient
     * @return DeploymentList
     */
    public DeploymentList listDeployments(KubernetesClient client){
        try{
            logger.info("listDeployments成功");
            return client.apps().deployments().list();
        }catch (KubernetesClientException e){
            logger.error("listDeployments失败");
            logger.error("错误信息：",e.getMessage());
            return null;
        }
    }

    /**
     * 获取某一个namespace下的所有deployment信息
     *
     * @param client KubernetesClient
     * @param namespaceName namespaceName
     * @return DeploymentList
     */
    public DeploymentList listDeploymentsFromNamespace(KubernetesClient client, String namespaceName){
        try{
            logger.info("listDeploymentsFromNamespace成功");
            return client.apps().deployments().inNamespace(namespaceName).list();
        }catch (KubernetesClientException e){
            logger.error("listDeploymentsFromNamespace失败");
            logger.error("错误信息：",e.getMessage());
            return null;
        }
    }

    /**
     * 获取某一个namespace下的某一个deployment信息
     *
     * @param client KubernetesClient
     * @param namespaceName namespaceName
     * @param deploymentName deploymentName
     * @return Deployment
     */
    public Deployment getDeployment(KubernetesClient client, String namespaceName, String deploymentName){
        try{
            logger.info("getDeployment成功");
            return client.apps().deployments().inNamespace(namespaceName).withName(deploymentName).get();
        }catch (KubernetesClientException e){
            logger.error("getDeployment失败");
            logger.error("错误信息：",e.getMessage());
            return null;
        }
    }

    /**
     * 删除所有的deployment
     *
     * @param client KubernetesClient
     * @return boolean
     */
    public boolean deleteAllDeployments(KubernetesClient client){
        try{
            client.apps().deployments().delete();
            logger.info("deleteAllDeployments成功");
            return true;
        }catch (KubernetesClientException e){
            logger.error("deleteAllDeployments失败");
            logger.error("错误信息：",e.getMessage());
            return false;
        }
    }

    /**
     * 删除某一个namespace下的所有deployment
     *
     * @param client KubernetesClient
     * @param namespaceName namespaceName
     * @return boolean
     */
    public boolean deleteDeployments(KubernetesClient client, String namespaceName){
        try{
            client.apps().deployments().inNamespace(namespaceName).delete();
            logger.info("deleteDeployments成功");
            return true;
        }catch (KubernetesClientException e){
            logger.error("deleteDeployments失败");
            logger.error("错误信息：",e.getMessage());
            return false;
        }
    }

    /**
     * 删除某一个namespace下的某一个deployment
     *
     * @param client KubernetesClient
     * @param namespaceName namespaceName
     * @param deploymentName deploymentName
     * @return boolean
     */
    public boolean deleteDeployment(KubernetesClient client, String namespaceName, String deploymentName){
        try{
            client.apps().deployments().inNamespace(namespaceName).withName(deploymentName).delete();
            logger.info("deleteDeployment成功");
            return true;
        }catch (KubernetesClientException e){
            e.printStackTrace();
            logger.error("deleteDeployment失败");
            logger.error("错误信息：",e.getMessage());
            return false;
        }
    }

    /**
     * 创建一个deployment
     * @param client KubernetesClient
     * @param deployment Deployment
     * @return boolean
     */
    public boolean createDeployment(KubernetesClient client,Deployment deployment)
    {

        String apiVersion=deployment.getApiVersion();
        String kind="Deployment";
        String namespace=deployment.getMetadata().getNamespace();
        String name=deployment.getMetadata().getName();
        Map<String,String> labels=deployment.getMetadata().getLabels();
        List<String> command = deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getCommand();
        List<String> args = deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getArgs();
        /**
         * 当前Deployment的规格说明
         */
        //# 具体控制器与容器的选项内容
        Integer replicas=deployment.getSpec().getReplicas();
        //# 选择器，匹配标签，控制器匹配具体的pod标签
        System.out.println("labels"+labels);
        Map<String,String> matchLables=deployment.getSpec().getSelector().getMatchLabels();
        System.out.println("matchLables"+matchLables);
       // Map<String,String> speLables=deployment.getSpec().getTemplate().getMetadata().getLabels();
//        /**
//         * 当前pod的规格说明
//         */
         String containerName=deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getName();
         String image=deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getImage();
         Integer containerPort=deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getPorts().get(0).getContainerPort();

         try{
             Deployment createDeployment=new DeploymentBuilder()
                     .withApiVersion(apiVersion)
                     .withKind(kind)
                     .withNewMetadata()
                     .withNamespace(namespace)
                     .withName(name)
                     .withLabels(labels)
                     .endMetadata()
                     .withNewSpec()
                     .withNewSelector()
                     .withMatchLabels(matchLables)
                     .endSelector()
                     .withNewReplicas(replicas)
                     .withNewTemplate()
                     .withNewMetadata()
                     .withName(name)
                     .withLabels(labels)
                     .endMetadata()
                     .withNewSpec()
                     .addNewContainer()
                     .withName(containerName)
                     .withImage(image)
                     .withCommand(command)
                     .withArgs(args)
                     .addNewPort()
                     .withContainerPort(containerPort)
                     .endPort()
                     .endContainer()
                     .endSpec()
                     .endTemplate()
                     .endSpec()
                     .build();
             client.apps().deployments().inNamespace(namespace).create(createDeployment);
             return true;
         }catch (KubernetesClientException e)
         {
             logger.error("createDeployment失败");
             logger.error("错误信息：",e.getMessage());
             e.printStackTrace();
             return false;
         }

    }

    public boolean updateDeployment(KubernetesClient client,Deployment deployment)
    {
        String apiVersion=deployment.getApiVersion();
        String kind="Deployment";
        String namespace=deployment.getMetadata().getNamespace();
        String name=deployment.getMetadata().getName();
        Map<String,String> labels=deployment.getMetadata().getLabels();
        Integer replicas=deployment.getSpec().getReplicas();
        Map<String,String> matchLables=deployment.getSpec().getSelector().getMatchLabels();
        Map<String,String> speLables=deployment.getSpec().getTemplate().getMetadata().getLabels();
        String containerName=deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getName();
        String image=deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getImage();
        Integer containerPort=deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getPorts().get(0).getContainerPort();

        try{
            Deployment patchDeployment=new DeploymentBuilder()
                    .withApiVersion(apiVersion)
                    .withKind(kind)
                    .withNewMetadata()
                    .withNamespace(namespace)
                    .withName(name)
                    .withLabels(labels)
                    .endMetadata()
                    .withNewSpec()
                    .withNewReplicas(replicas)
                    .withNewSelector()
                    .withMatchLabels(matchLables)
                    .endSelector()
                    .withNewTemplate()
                    .withNewMetadata()
                    .withLabels(speLables)
                    .endMetadata()
                    .withNewSpec()
                    .addNewContainer()
                    .withName(containerName)
                    .withImage(image)
                    .addNewPort()
                    .withContainerPort(containerPort)
                    .endPort()
                    .endContainer()
                    .endSpec()
                    .endTemplate()
                    .endSpec()
                    .build();
            HasMetadataOperation hasMetadataOperation = (HasMetadataOperation) client.apps().deployments();
            hasMetadataOperation.patch(patchDeployment);
            logger.info("更新成功");
            return true;
        }catch (KubernetesClientException e)
        {
            logger.error("更新失败");
            logger.error("错误信息：",e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
