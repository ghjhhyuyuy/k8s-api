package team.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.cloud.entity.vo.ResultVO;
import team.cloud.service.NodeService;

/**
 * Created by wzw on 2019/5/21
 *
 * @Author wzw
 */
@RestController
@RequestMapping("/node")
public class NodeController {
    @Autowired
    private NodeService nodeService;
    @GetMapping("listNodes")
    public ResultVO listNodes(){
        return nodeService.listNodes();
    }

    @GetMapping("getNode")
    public ResultVO getNode(String nodeName){
        return nodeService.getNode(nodeName);
    }
}
