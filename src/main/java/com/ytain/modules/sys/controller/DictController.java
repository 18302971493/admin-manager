package com.ytain.modules.sys.controller;
import com.baomidou.mybatisplus.plugins.Page;
import com.ytain.common.constant.CommonConstant;
import com.ytain.common.vo.Result;
import com.ytain.modules.sys.entity.Dict;
import com.ytain.modules.sys.service.DictService;
import com.ytain.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: 六娃
 * @description: 字典Controller
 * @create: 2018-12-21 17:33
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin/dict")
@Api(description = "字典管理接口")
public class DictController {
    @Autowired
    private DictService dictService;
    @ApiOperation(value = "分页获取全部字典数据")
    @GetMapping(value = "/findDictList")
    public Result<Page<Dict>> findDictList(Dict dict, Integer pageNo, Integer pageSize){
        Page<Dict> page=dictService.findPage(new Page<>(pageNo,pageSize),dict);
        return new ResultUtil<Page<Dict>>().setData(page);
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "保存字典")
    public Result<Dict> save(@RequestBody Dict dict){
        dictService.insert(dict);
        return new ResultUtil().setSuccessMsg("保存成功");
    }
    @PostMapping(value = "/edit")
    @ApiOperation(value = "编辑字典")
    public Result<Dict> edit(@RequestBody Dict dict){
        dictService.update(dict);
        return new ResultUtil().setSuccessMsg("修改成功");
    }
    @DeleteMapping(value = "/delByIds/{ids}")
    @ApiOperation(value = "编辑字典")
    public Result<Object> edit(@PathVariable String[] ids){
        for(String id :ids){
            dictService.delete(id);
        }
        return new ResultUtil().setSuccessMsg("删除字典成功");
    }
    @ApiOperation(value = "分页获取全部字典数据")
    @GetMapping(value = "/findDictByType")
    public Result<Object> findDictByType(String type){
        List<Dict> list=dictService.findByType(type);
        return new ResultUtil<>().setData(list);
    }
    @PostMapping(value = "/enableDict")
    @ApiOperation(value = "启用/禁用字典")
    public Result<Dict> enableDict(Dict dict){
        Integer status=dict.getStatus().equals(CommonConstant.STATUS_NORMAL)?CommonConstant.STATUS_DISABLE:CommonConstant.STATUS_NORMAL;
        dict.setStatus(status);
        dictService.update(dict);
        return new ResultUtil().setSuccessMsg("操作成功");
    }
}
