package com.htnova.system.tool.mapstruct;

import com.htnova.system.tool.dto.QuartzLogDto;
import com.htnova.system.tool.entity.QuartzLog;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-18T11:06:29+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_341 (Oracle Corporation)"
)
public class QuartzLogMapStructImpl implements QuartzLogMapStruct {

    @Override
    public QuartzLog toEntity(QuartzLogDto dto) {
        if ( dto == null ) {
            return null;
        }

        QuartzLog quartzLog = new QuartzLog();

        quartzLog.setId( dto.getId() );
        quartzLog.setRemark( dto.getRemark() );
        quartzLog.setCreateBy( dto.getCreateBy() );
        quartzLog.setCreatorName( dto.getCreatorName() );
        quartzLog.setCreateTime( dto.getCreateTime() );
        quartzLog.setUpdateBy( dto.getUpdateBy() );
        quartzLog.setUpdateTime( dto.getUpdateTime() );
        quartzLog.setDelFlag( dto.getDelFlag() );
        quartzLog.setJobName( dto.getJobName() );
        quartzLog.setBeanName( dto.getBeanName() );
        quartzLog.setMethodName( dto.getMethodName() );
        quartzLog.setParams( dto.getParams() );
        quartzLog.setCronExpression( dto.getCronExpression() );
        quartzLog.setStatus( dto.getStatus() );
        quartzLog.setTime( dto.getTime() );
        quartzLog.setDetail( dto.getDetail() );

        return quartzLog;
    }

    @Override
    public QuartzLogDto toDto(QuartzLog entity) {
        if ( entity == null ) {
            return null;
        }

        QuartzLogDto quartzLogDto = new QuartzLogDto();

        quartzLogDto.setId( entity.getId() );
        quartzLogDto.setRemark( entity.getRemark() );
        quartzLogDto.setCreateBy( entity.getCreateBy() );
        quartzLogDto.setCreatorName( entity.getCreatorName() );
        quartzLogDto.setCreateTime( entity.getCreateTime() );
        quartzLogDto.setUpdateBy( entity.getUpdateBy() );
        quartzLogDto.setUpdateTime( entity.getUpdateTime() );
        quartzLogDto.setDelFlag( entity.getDelFlag() );
        quartzLogDto.setJobName( entity.getJobName() );
        quartzLogDto.setBeanName( entity.getBeanName() );
        quartzLogDto.setMethodName( entity.getMethodName() );
        quartzLogDto.setParams( entity.getParams() );
        quartzLogDto.setCronExpression( entity.getCronExpression() );
        quartzLogDto.setStatus( entity.getStatus() );
        quartzLogDto.setTime( entity.getTime() );
        quartzLogDto.setDetail( entity.getDetail() );

        return quartzLogDto;
    }

    @Override
    public List<QuartzLog> toEntity(List<QuartzLogDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<QuartzLog> list = new ArrayList<QuartzLog>( dtoList.size() );
        for ( QuartzLogDto quartzLogDto : dtoList ) {
            list.add( toEntity( quartzLogDto ) );
        }

        return list;
    }

    @Override
    public List<QuartzLogDto> toDto(List<QuartzLog> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<QuartzLogDto> list = new ArrayList<QuartzLogDto>( entityList.size() );
        for ( QuartzLog quartzLog : entityList ) {
            list.add( toDto( quartzLog ) );
        }

        return list;
    }
}
