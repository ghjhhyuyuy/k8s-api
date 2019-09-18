package team.cloud.k8s;

import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Ernest ljw9712@163.com
 * @date 2019/3/3
 *
 * nodes-api
 */
@Component
public class K8sNodes {

    private static final Logger logger = LogManager.getLogger(K8sNodes.class);

    /**
     * 获取所有node信息
     *
     * @param client KubernetesClient
     * @return NodeList
     */
    public NodeList listNodes(KubernetesClient client){
        try{
            NodeList list=client.nodes().list();
            logger.info("listNodes成功");
            return list;
        }catch (KubernetesClientException e){
            logger.error("listNodes失败");
            logger.error("错误信息：",e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据nodeName查询某个node的信息，无此node时getNode成功但返回结果为null
     *
     * @param client KubernetesClient
     * @param nodeName nodeName
     * @return Node信息
     */
    public Node getNode(KubernetesClient client, String nodeName){
        try{
            logger.info("getNode成功");
            return client.nodes().withName(nodeName).get();
        }catch (KubernetesClientException e){
            logger.error("getNode失败");
            logger.error("错误信息：",e.getMessage());
            return null;
        }
    }
}
