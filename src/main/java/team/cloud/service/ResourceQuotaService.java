package team.cloud.service;

import io.fabric8.kubernetes.api.model.ResourceQuota;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.stereotype.Service;
import team.cloud.entity.vo.ResultVO;
import team.cloud.k8s.Fabric8;
import team.cloud.k8s.K8sResourceQuota;
import team.cloud.util.ResultVOUtil;

import javax.annotation.Resource;

/**
 * Created by wzw on 2019/8/12
 *
 * @Author wzw
 */
@Service
public class ResourceQuotaService {
    @Resource
    private Fabric8 fabric8;
    @Resource
    private K8sResourceQuota k8sResourceQuota;
    public ResultVO createResourceQuota(ResourceQuota resourceQuota){
        KubernetesClient client = fabric8.connect();
        Boolean result;
        try{
            result = k8sResourceQuota.createResourceQuota(client,resourceQuota);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVOUtil().Fail("创建ResourceQuota失败，请检查参数");
        }
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(result);
        return resultVO;
    }
}
