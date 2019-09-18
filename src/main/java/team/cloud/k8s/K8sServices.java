package team.cloud.k8s;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.channels.Selector;
import java.util.List;
import java.util.Map;

/**
 * @author Ernest ljw9712@163.com
 * @date 2019/3/3
 *
 * services-api
 */
@Component
public class K8sServices {

    private static final Logger logger = LogManager.getLogger(K8sServices.class);

    /**
     * 获取所有service信息
     *
     * @param client KubernetesClient
     * @return ServiceList
     */
    public ServiceList listServices(KubernetesClient client){
        try{
            logger.info("listServices成功");
            return client.services().list();
        }catch (KubernetesClientException e){
            logger.error("listServices失败");
            logger.error("错误信息：",e.getMessage());
            return null;
        }
    }

    /**
     * 获取某一个namespace下的所有service信息
     *
     * @param client KubernetesClient
     * @param namespaceName namespaceName
     * @return ServiceList
     */
    public ServiceList listServicesFromNamespace(KubernetesClient client, String namespaceName){
        try{
            logger.info("listServicesFromNamespace成功");
            return client.services().inNamespace(namespaceName).list();
        }catch (KubernetesClientException e){
            logger.error("listServicesFromNamespace失败");
            logger.error("错误信息：",e.getMessage());
            return null;
        }
    }

    /**
     * 获取某一个namespace下的某一个service信息
     *
     * @param client KubernetesClient
     * @param namespaceName namespaceName
     * @param serviceName serviceName
     * @return Service
     */
    public Service getService(KubernetesClient client, String namespaceName, String serviceName){
        try{
            logger.info("getService成功");
            return client.services().inNamespace(namespaceName).withName(serviceName).get();
        }catch (KubernetesClientException e){
            logger.error("getService失败");
            logger.error("错误信息：",e.getMessage());
            return null;
        }
    }

    /**
     * 删除所有的service
     *
     * @param client KubernetesClient
     * @return boolean
     */
    public boolean deleteAllServices(KubernetesClient client){
        try{
            client.services().delete();
            logger.info("deleteAllServices成功");
            return true;
        }catch (KubernetesClientException e){
            logger.error("deleteAllServices失败");
            logger.error("错误信息：",e.getMessage());
            return false;
        }
    }

    /**
     * 删除某一个namespace下的所有service
     *
     * @param client KubernetesClient
     * @param namespaceName namespaceName
     * @return boolean
     */
    public boolean deleteServices(KubernetesClient client, String namespaceName){
        try{
            client.services().inNamespace(namespaceName).delete();
            logger.info("deleteServices成功");
            return true;
        }catch (KubernetesClientException e){
            logger.error("deleteServices失败");
            logger.error("错误信息：",e.getMessage());
            return false;
        }
    }

    /**
     * 删除某一个namespace下的某一个service
     *
     * @param client KubernetesClient
     * @param namespaceName namespaceName
     * @param serviceName serviceName
     * @return boolean
     */
    public boolean deleteService(KubernetesClient client, String namespaceName, String serviceName){
        try{
            client.services().inNamespace(namespaceName).withName(serviceName).delete();
            logger.info("deleteService成功");
            return true;
        }catch (KubernetesClientException e){
            logger.error("deleteService失败");
            logger.error("错误信息：",e.getMessage());
            return false;
        }
    }

    /**
     * 创建一个service
     * @param client KubernetesClient
     * @param service Service
     * @return boolean
     */

    public boolean createService(KubernetesClient client, Service service)
    {
        String apiVersion=service.getApiVersion();
        String kind="Service";
        String namespace=service.getMetadata().getNamespace();
        String name=service.getMetadata().getName();
        Map<String,String> labels=service.getMetadata().getLabels();

        Integer port=service.getSpec().getPorts().get(0).getPort();
        IntOrString targetPort=service.getSpec().getPorts().get(0).getTargetPort();
        String protocol=service.getSpec().getPorts().get(0).getProtocol();
        Map<String,String> selector=service.getSpec().getSelector();

        try {
            Service createService=new ServiceBuilder()
                    .withApiVersion(apiVersion)
                    .withApiVersion(kind)
                    .withNewMetadata()
                    .withNamespace(namespace)
                    .withName(name)
                    .withLabels(labels)
                    .endMetadata()
                    .withNewSpec()
                    .addNewPort()
                    .withNewPort(port)
                    .withTargetPort(targetPort)
                    .withProtocol(protocol)
                    .endPort()
                    .withSelector(selector)
                    .endSpec()
                    .build();



            service = client.services().inNamespace(namespace).create(service);
            logger.error("createService成功");
            return true;
        }catch (KubernetesClientException e)
        {
            logger.error("createService失败");
            logger.error("错误信息：",e.getMessage());
            e.printStackTrace();
            return false;
        }

    }
}
