package com.htnova.mt.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htnova.common.constant.ResultStatus;
import com.htnova.common.converter.DtoConverter;
import com.htnova.common.dto.Result;
import com.htnova.common.dto.XPage;
import com.htnova.mt.order.dto.UserPoiDto;
import com.htnova.mt.order.entity.UserPoi;
import com.htnova.mt.order.mapstruct.UserPoiMapStruct;
import com.htnova.mt.order.service.UserPoiService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

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
    public XPage<UserPoi> findListByPage(UserPoi userPoi, XPage<Void> xPage) {
        IPage<UserPoi> userPoiPage = userPoiService.findUserPoiList(userPoi, XPage.toIPage(xPage));
        return XPage.fromIPage(userPoiPage);
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
    public UserPoi getById(@PathVariable long id) {
        return userPoiService.getUserPoiById(id);
    }

    /**
     * 保存
     */
    @PreAuthorize("hasAnyAuthority('userPoi.add', 'userPoi.edit')")
    @PostMapping("/save")
    public Result<Void> save(@Valid @RequestBody UserPoi userPoiDto) {
        userPoiService.saveUserPoi(userPoiDto);
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
