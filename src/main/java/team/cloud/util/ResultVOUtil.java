package team.cloud.util;

import org.springframework.stereotype.Component;
import team.cloud.entity.vo.ResultEnumVO;
import team.cloud.entity.vo.ResultVO;

/**
 * Description:
 *
 * @author tzw
 * CreateTime 12:55 2019/5/18
 **/

@Component
public class ResultVOUtil {
    /*
     * 成功
     * */
    public  ResultVO success(){
        ResultVO resultVO=new ResultVO();
        resultVO.setCode(ResultEnumVO.SUCCESS.getCode());
        resultVO.setMsg(ResultEnumVO.SUCCESS.getMsg());
        resultVO.setData(null);
        return resultVO;
    }

//    public ResultVO success(String msg){
//        ResultVO resultVO=this.success();
//        resultVO.setMsg(msg);
//        return resultVO;
//    }

    public ResultVO success(Object data){
        ResultVO resultVO=this.success();
        resultVO.setData(data);
        return resultVO;
    }


    public ResultVO success(String msg,Object data){
        ResultVO resultVO=this.success(data);
        resultVO.setMsg(msg);
        return resultVO;

    }


    /*
     * 未知错误
     * */

    public ResultVO unknowError(){
        return new ResultVO(ResultEnumVO.UNKNOW_ERROR.getCode(),
                ResultEnumVO.UNKNOW_ERROR.getMsg());
    }

    public ResultVO unknowError(String msg){
        ResultVO resultVO=this.unknowError();
        resultVO.setMsg(msg);
        return resultVO;

    }

    public ResultVO unknowError(Object data){
        ResultVO resultVO=this.unknowError();
        resultVO.setData(data);
        return resultVO;

    }


    public ResultVO paramError() {
        return new ResultVO(ResultEnumVO.PARAM_ERROR.getCode(),
                ResultEnumVO.PARAM_ERROR.getMsg());
    }

    public ResultVO resourceNotFound() {
        return new ResultVO(ResultEnumVO.RESOURCE_NOT_FOUND.getCode(),
                ResultEnumVO.RESOURCE_NOT_FOUND.getMsg());
    }

    public ResultVO loginError(){
        return new ResultVO(ResultEnumVO.LOGIN_ERROR.getCode(),
                ResultEnumVO.LOGIN_ERROR.getMsg());
    }

    public ResultVO TimeOut(){
        return new ResultVO(ResultEnumVO.TIME_OUT.getCode(),
                ResultEnumVO.TIME_OUT.getMsg());
    }

    public ResultVO ResourceExist(){
        return new ResultVO(ResultEnumVO.RESOURCE_EXIST.getCode(),
                ResultEnumVO.RESOURCE_EXIST.getMsg());
    }

    public ResultVO Fail(){
        return new ResultVO(ResultEnumVO.FAIL.getCode(),
                ResultEnumVO.FAIL.getMsg());
    }

    public ResultVO Fail(String msg){
        ResultVO resultVO=this.Fail();
        resultVO.setMsg(msg);
        return resultVO;
    }
    public ResultVO Fail(String msg,Object data){
        ResultVO resultVO=this.Fail();
        resultVO.setMsg(msg);
        resultVO.setData(data);
        return resultVO;
    }
}
