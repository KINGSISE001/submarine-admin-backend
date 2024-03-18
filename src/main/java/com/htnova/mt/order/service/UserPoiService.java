package com.htnova.mt.order.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htnova.mt.order.dto.UserPoiDto;
import com.htnova.mt.order.entity.UserPoi;
import com.htnova.mt.order.mapper.UserPoiMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class UserPoiService extends ServiceImpl<UserPoiMapper, UserPoi> {
    @Resource
    private UserPoiMapper userPoiMapper;

    @Transactional(readOnly = true)
    public IPage<UserPoi> findUserPoiList(UserPoi userPoiDto, IPage<Void> xPage) {
        return userPoiMapper.findPage(xPage, userPoiDto);
    }

    @Transactional(readOnly = true)
    public List<UserPoi> findUserPoiList(UserPoiDto userPoiDto) {
        return userPoiMapper.findList(userPoiDto);
    }

    @Transactional
    public void saveUserPoi(UserPoi userPoi) {
        super.saveOrUpdate(userPoi);
    }

    @Transactional(readOnly = true)
    public UserPoi getUserPoiById(long id) {
        return userPoiMapper.selectById(id);
    }

    @Transactional
    public void deleteUserPoi(Long id) {
        super.removeById(id);
    }

    public List<UserPoi> getUserPoiById(String Id){
        QueryWrapper<UserPoi> userPoi = Wrappers.query();
        userPoi.eq("id",Id);
        userPoi.last("LIMIT 1");
        return userPoiMapper.selectList(userPoi);
    };

    public List<UserPoi> getUserPoiByPoiId(String poiId){
        QueryWrapper<UserPoi> userPoi = Wrappers.query();
        userPoi.eq("poi_id",poiId);
        userPoi.last("LIMIT 1");
        return userPoiMapper.selectList(userPoi);
    };

    public List<UserPoi> getUserEleById(String eleId){
        QueryWrapper<UserPoi> userPoi = Wrappers.query();
        userPoi.eq("ele_id",eleId);
        userPoi.last("LIMIT 1");
        return userPoiMapper.selectList(userPoi);
    };
    public UserPoi getUserById(String merchant_id){

        return  userPoiMapper.selectById(merchant_id);
    };



}
