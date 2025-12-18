package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品:{}" , dishDTO);
        String key = "dish_" + dishDTO.getCategoryId();
        redisTemplate.delete(key);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品查询")
    public Result<PageResult> find(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品列表：{}" , dishPageQueryDTO);
        PageResult pageResult =  dishService.find(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 菜品删除
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("菜品删除")
    public Result delete(@RequestParam List<Long> ids){
        log.info("菜品删除 ：{}" , ids);
        dishService.delete(ids);
        //Todo 后续可以只删除相关key 目前写的删除所有redis缓存的key
        deleteKey("dish_");
        return Result.success();

    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("查询菜品：{}" , id);
        DishVO dish  = dishService.getById(id);
        return Result.success(dish);
    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品：{}" , dishDTO);
        dishService.update(dishDTO);
        deleteKey("dish_");
        return Result.success();
    }
    /**
     * 启用、禁用分类
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用分类")
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id){
        dishService.startOrStop(status,id);
        deleteKey("dish_");
        log.info("执行成功");
        return Result.success();
    }
    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    /**
     * 删除指定前缀的所有缓存键
     * @param prefix
     */
    private void deleteKey(String prefix) {
        // 关键：加通配符*，匹配所有以prefix开头的键（如dish_16、dish_17）
        String pattern = prefix + "*";
        // 指定泛型，避免类型转换问题
        Set<String> keys = redisTemplate.keys(pattern);
        // 空值判断：避免delete(null)
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("成功删除缓存键：{}", keys);
        } else {
            log.warn("未匹配到任何缓存键，pattern：{}", pattern);
        }
    }
}
