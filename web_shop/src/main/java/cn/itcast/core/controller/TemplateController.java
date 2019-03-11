package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.service.TemplateService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/typeTemplate")
public class TemplateController {

    @Reference
    private TemplateService templateService;



    @RequestMapping("/findOne")
    public TypeTemplate findOne(Long id) {
        return templateService.findOne(id);
    }

    /**
     *
     * @param id
     */
    @RequestMapping("/findBySpecList")
    public List<Map> findBySpecList(Long id) {
        List<Map> specList = templateService.findBySpecList(id);
        return specList;
    }


    @RequestMapping("/add")
    public Result add(@RequestBody TypeTemplate template) {
        try {
            templateService.add(template);
            return  new Result(true, "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败!");
        }
    }


}
