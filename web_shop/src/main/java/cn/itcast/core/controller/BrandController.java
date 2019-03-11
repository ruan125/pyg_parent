package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.service.BrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/brand")
public class BrandController {


    @Reference
    private BrandService brandService;

    @RequestMapping("/add")
    public Result add(@RequestBody Brand brand) {
        try {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            brand.setSellerId(userName);
            brandService.add(brand);
            return new Result(true,"添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败!");
        }
    }

}
