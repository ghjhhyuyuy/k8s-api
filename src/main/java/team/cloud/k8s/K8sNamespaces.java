package team.cloud.k8s;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Ernest ljw9712@163.com
 * @date 2019/3/3
 *
 * namespaces-api
 */
@Component
public class K8sNamespaces {

    private static final Logger logger = LogManager.getLogger(K8sNamespaces.class);

    /**
     * 获取所有namespace信息
     *
     * @param client KubernetesClient
     * @return NamespaceList
     */
    public NamespaceList listNamespaces(KubernetesClient client){
        try{
            logger.info("listNamespaces成功");
            return client.namespaces().list();
        }catch (KubernetesClientException e){
            logger.error("listNamespaces失败");
            logger.error("错误信息：",e.getMessage());
            return null;
        }
    }

    /**
     * 根据namespaceName查询某个namespace的信息
     *
     * @param client KubernetesClient
     * @param namespaceName namespaceName
     * @return Namespace信息
     */
    public Namespace getNamespace(KubernetesClient client, String namespaceName){
        try{
            logger.info("getNamespace成功");
            return client.namespaces().withName(namespaceName).get();
        }catch (KubernetesClientException e){
            logger.error("getNamespace失败");
            logger.error("错误信息：",e.getMessage());
            return null;
        }
    }

    /**
     * 创建namespace
     *
     * @param client KubernetesClient
     * @param namespace namespace对象
     * @return boolean
     */
    public boolean createNamespace(KubernetesClient client, Namespace namespace){
        String namespaceName = namespace.getMetadata().getName();
        Map<String, String> labels = namespace.getMetadata().getLabels();
        Map<String, String> annotations = namespace.getMetadata().getAnnotations();
        try{
            Namespace createNamespace = new NamespaceBuilder()
                    .withNewMetadata()
                    .withName(namespaceName)
                    .withLabels(labels)
                    .withAnnotations(annotations)
                    .endMetadata()
                    .build();
            client.namespaces().create(createNamespace);
            logger.info("createNamespace成功");
            return true;
        }catch (KubernetesClientException e){
            logger.error("createNamespace失败");
            logger.error("错误信息：",e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除namespace
     *
     * @param client KubernetesClient
     * @param namespaceName namespaceName
     * @return boolean
     */
    public boolean deleteNamespace(KubernetesClient client, String namespaceName) {
        try{
            client.namespaces().withName(namespaceName).delete();
            logger.info("deleteNamespace成功");
            return true;
        }catch (KubernetesClientException e){
            logger.error("deleteNamespace失败");
            logger.error("错误信息：",e.getMessage());
            return false;
        }
    }
}
