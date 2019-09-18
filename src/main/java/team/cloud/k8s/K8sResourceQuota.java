package team.cloud.k8s;

import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceQuota;
import io.fabric8.kubernetes.api.model.ResourceQuotaBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by wzw on 2019/8/12
 *
 * @Author wzw
 */
@Component
public class K8sResourceQuota {
    private static final Logger logger = LogManager.getLogger(K8sResourceQuota.class);
    public boolean createResourceQuota(KubernetesClient client, ResourceQuota resourceQuota){
        String namespaceName = resourceQuota.getMetadata().getNamespace();
        String name = resourceQuota.getMetadata().getName();
        String apiVersion=resourceQuota.getApiVersion();
        Map<String, Quantity> hard = resourceQuota.getSpec().getHard();

        try{
            ResourceQuota createResourceQuota = new ResourceQuotaBuilder()
                    .withApiVersion(apiVersion)
                    .withKind("ResourceQuota")
                    .withNewMetadata()
                    .withName(name)
                    .endMetadata()
                    .withNewSpec()
                    .addToHard(hard)
                    .endSpec()
                    .build();

            resourceQuota = client.resourceQuotas().inNamespace(namespaceName).create(resourceQuota);
            logger.info("createResourceQuota成功");
            return true;
        }catch (KubernetesClientException e){
            logger.error("createResourceQuota失败");
            logger.error("错误信息：",e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
