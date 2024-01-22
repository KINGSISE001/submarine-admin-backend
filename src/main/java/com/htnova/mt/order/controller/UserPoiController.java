package com.htnova.mt.order.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htnova.common.constant.ResultStatus;
import com.htnova.common.converter.DtoConverter;
import com.htnova.common.dto.Result;
import com.htnova.common.dto.XPage;
import com.htnova.mt.order.dto.UserPoiDto;
import com.htnova.mt.order.entity.UserPoi;
import com.htnova.mt.order.mapstruct.UserPoiMapStruct;
import com.htnova.mt.order.service.UserPoiService;
import java.util.List;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @menu
 */
@RestController
@RequestMapping("/userpoi")
public class UserPoiController {
    @Resource
    private UserPoiService userPoiService;

    /**
     * 分页查询
     */
    @PreAuthorize("hasAnyAuthority('userPoi.find')")
    @GetMapping("/list/page")
    public XPage<UserPoiDto> findListByPage(UserPoiDto userPoiDto, XPage<Void> xPage) {
        IPage<UserPoi> userPoiPage = userPoiService.findUserPoiList(userPoiDto, XPage.toIPage(xPage));
        return DtoConverter.toDto(userPoiPage, UserPoiMapStruct.INSTANCE);
    }

    /**
     * 查询
     */
    @PreAuthorize("hasAnyAuthority('userPoi.find')")
    @GetMapping("/list/all")
    public List<UserPoiDto> findList(UserPoiDto userPoiDto) {
        List<UserPoi> userPoiList = userPoiService.findUserPoiList(userPoiDto);
        return DtoConverter.toDto(userPoiList, UserPoiMapStruct.INSTANCE);
    }

    /**
     * 详情
     */
    @PreAuthorize("hasAnyAuthority('userPoi.find')")
    @GetMapping("/detail/{id}")
    public UserPoiDto getById(@PathVariable long id) {
        UserPoi userPoi = userPoiService.getUserPoiById(id);
        return DtoConverter.toDto(userPoi, UserPoiMapStruct.INSTANCE);
    }

    /**
     * 保存
     */
    @PreAuthorize("hasAnyAuthority('userPoi.add', 'userPoi.edit')")
    @PostMapping("/save")
    public Result<Void> save(@Valid @RequestBody UserPoiDto userPoiDto) {
        userPoiService.saveUserPoi(DtoConverter.toEntity(userPoiDto, UserPoiMapStruct.INSTANCE));
        return Result.build(ResultStatus.SAVE_SUCCESS);
    }

    /**
     * 删除
     */
    @PreAuthorize("hasAnyAuthority('userPoi.del')")
    @DeleteMapping("/del/{id}")
    public Result<Void> delete(@PathVariable long id) {
        userPoiService.deleteUserPoi(id);
        return Result.build(ResultStatus.DELETE_SUCCESS);
    }
}
