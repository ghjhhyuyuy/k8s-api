package team.cloud.controller;

import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import team.cloud.entity.vo.ResultVO;
import team.cloud.util.ResultVOUtil;

/**
 * Created by wzw on 2019/6/12
 *
 * @Author wzw
 */
@ControllerAdvice
public class ErrorInterceptor {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultVO handleIOException() {
        System.out.println("进入处理");
        return new ResultVOUtil().paramError();
    }
}
