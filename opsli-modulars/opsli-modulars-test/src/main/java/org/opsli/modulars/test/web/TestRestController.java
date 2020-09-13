package org.opsli.modulars.test.web;

import org.opsli.common.api.ResultVo;
import org.opsli.common.base.concroller.BaseController;
import org.opsli.plugins.mail.MailHandler;
import org.opsli.plugins.mail.model.MailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.web
 * @Author: Parker
 * @CreateTime: 2020-09-13 17:40
 * @Description: 测试类
 */
@RestController
@RequestMapping("/{opsli.prefix}/{opsli.version}/test")
public class TestRestController extends BaseController {

    @Autowired
    private MailHandler mailHandler;

    @GetMapping("/sendMail")
    public ResultVo sendMail(){
        MailModel mailModel = new MailModel();
        mailModel.setTo("meet.carina@foxmail.com");
        mailModel.setSubject("测试邮件功能");
        mailModel.setContent("<h1>这是哪里呢？</h1><br><font color='red'>lalalalalalallalalalalal!!!!</font>");
        mailHandler.send(mailModel);
        return ResultVo.success("发送邮件成功！！！！！！");
    }

}
