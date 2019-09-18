package team.cloud.k8s;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import okhttp3.TlsVersion;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Ernest ljw9712@163.com
 * @date 2018/12/27
 *
 * 创建k8sClient链接api
 */
@Component
public class Fabric8 {

    private static final Logger logger = LogManager.getLogger(Fabric8.class);

    /**
     * 创建k8sClient链接
     *
     * @return KubernetesClient
     */
    public KubernetesClient connect(){
        try{
            Config config = new ConfigBuilder().withMasterUrl("https://39.108.74.92:6443")
                    .withTrustCerts(true)
                    .withUsername("admin")
                    .withPassword("admin")
                    .removeFromTlsVersions(TlsVersion.TLS_1_0)
                    .removeFromTlsVersions(TlsVersion.TLS_1_1)
                    .removeFromTlsVersions(TlsVersion.TLS_1_2)
                    .build();
            KubernetesClient client = new DefaultKubernetesClient(config);
            logger.info("k8s-api调用成功");
            return client;
        }catch (Exception e){
            logger.error("k8s-api调用失败");
            logger.error("错误信息：",e.getMessage());
            return null;
        }
    }
}
