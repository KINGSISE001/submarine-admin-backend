package com.htnova.system.tool.mapstruct;

import com.htnova.system.tool.dto.QuartzJobDto;
import com.htnova.system.tool.entity.QuartzJob;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-18T11:06:29+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_341 (Oracle Corporation)"
)
public class QuartzJobMapStructImpl implements QuartzJobMapStruct {

    @Override
    public QuartzJob toEntity(QuartzJobDto dto) {
        if ( dto == null ) {
            return null;
        }

        QuartzJob quartzJob = new QuartzJob();

        quartzJob.setId( dto.getId() );
        quartzJob.setRemark( dto.getRemark() );
        quartzJob.setCreateBy( dto.getCreateBy() );
        quartzJob.setCreatorName( dto.getCreatorName() );
        quartzJob.setCreateTime( dto.getCreateTime() );
        quartzJob.setUpdateBy( dto.getUpdateBy() );
        quartzJob.setUpdateTime( dto.getUpdateTime() );
        quartzJob.setDelFlag( dto.getDelFlag() );
        quartzJob.setJobName( dto.getJobName() );
        quartzJob.setBeanName( dto.getBeanName() );
        quartzJob.setMethodName( dto.getMethodName() );
        quartzJob.setParams( dto.getParams() );
        quartzJob.setCronExpression( dto.getCronExpression() );
        quartzJob.setStatus( dto.getStatus() );

        return quartzJob;
    }

    @Override
    public QuartzJobDto toDto(QuartzJob entity) {
        if ( entity == null ) {
            return null;
        }

        QuartzJobDto quartzJobDto = new QuartzJobDto();

        quartzJobDto.setId( entity.getId() );
        quartzJobDto.setRemark( entity.getRemark() );
        quartzJobDto.setCreateBy( entity.getCreateBy() );
        quartzJobDto.setCreatorName( entity.getCreatorName() );
        quartzJobDto.setCreateTime( entity.getCreateTime() );
        quartzJobDto.setUpdateBy( entity.getUpdateBy() );
        quartzJobDto.setUpdateTime( entity.getUpdateTime() );
        quartzJobDto.setDelFlag( entity.getDelFlag() );
        quartzJobDto.setJobName( entity.getJobName() );
        quartzJobDto.setBeanName( entity.getBeanName() );
        quartzJobDto.setMethodName( entity.getMethodName() );
        quartzJobDto.setParams( entity.getParams() );
        quartzJobDto.setCronExpression( entity.getCronExpression() );
        quartzJobDto.setStatus( entity.getStatus() );

        return quartzJobDto;
    }

    @Override
    public List<QuartzJob> toEntity(List<QuartzJobDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<QuartzJob> list = new ArrayList<QuartzJob>( dtoList.size() );
        for ( QuartzJobDto quartzJobDto : dtoList ) {
            list.add( toEntity( quartzJobDto ) );
        }

        return list;
    }

    @Override
    public List<QuartzJobDto> toDto(List<QuartzJob> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<QuartzJobDto> list = new ArrayList<QuartzJobDto>( entityList.size() );
        for ( QuartzJob quartzJob : entityList ) {
            list.add( toDto( quartzJob ) );
        }

        return list;
    }
}
