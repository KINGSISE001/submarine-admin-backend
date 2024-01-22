package com.htnova.system.manage.mapstruct;

import com.htnova.system.manage.dto.DictionaryItemDto;
import com.htnova.system.manage.entity.DictionaryItem;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-18T11:06:29+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_341 (Oracle Corporation)"
)
public class DictionaryItemMapStructImpl implements DictionaryItemMapStruct {

    @Override
    public DictionaryItem toEntity(DictionaryItemDto dto) {
        if ( dto == null ) {
            return null;
        }

        DictionaryItem dictionaryItem = new DictionaryItem();

        dictionaryItem.setId( dto.getId() );
        dictionaryItem.setRemark( dto.getRemark() );
        dictionaryItem.setCreateBy( dto.getCreateBy() );
        dictionaryItem.setCreatorName( dto.getCreatorName() );
        dictionaryItem.setCreateTime( dto.getCreateTime() );
        dictionaryItem.setUpdateBy( dto.getUpdateBy() );
        dictionaryItem.setUpdateTime( dto.getUpdateTime() );
        dictionaryItem.setDelFlag( dto.getDelFlag() );
        dictionaryItem.setDictionaryId( dto.getDictionaryId() );
        dictionaryItem.setLabel( dto.getLabel() );
        dictionaryItem.setValue( dto.getValue() );
        dictionaryItem.setSort( dto.getSort() );

        return dictionaryItem;
    }

    @Override
    public DictionaryItemDto toDto(DictionaryItem entity) {
        if ( entity == null ) {
            return null;
        }

        DictionaryItemDto dictionaryItemDto = new DictionaryItemDto();

        dictionaryItemDto.setId( entity.getId() );
        dictionaryItemDto.setRemark( entity.getRemark() );
        dictionaryItemDto.setCreateBy( entity.getCreateBy() );
        dictionaryItemDto.setCreatorName( entity.getCreatorName() );
        dictionaryItemDto.setCreateTime( entity.getCreateTime() );
        dictionaryItemDto.setUpdateBy( entity.getUpdateBy() );
        dictionaryItemDto.setUpdateTime( entity.getUpdateTime() );
        dictionaryItemDto.setDelFlag( entity.getDelFlag() );
        dictionaryItemDto.setDictionaryId( entity.getDictionaryId() );
        dictionaryItemDto.setLabel( entity.getLabel() );
        dictionaryItemDto.setValue( entity.getValue() );
        dictionaryItemDto.setSort( entity.getSort() );

        return dictionaryItemDto;
    }

    @Override
    public List<DictionaryItem> toEntity(List<DictionaryItemDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<DictionaryItem> list = new ArrayList<DictionaryItem>( dtoList.size() );
        for ( DictionaryItemDto dictionaryItemDto : dtoList ) {
            list.add( toEntity( dictionaryItemDto ) );
        }

        return list;
    }

    @Override
    public List<DictionaryItemDto> toDto(List<DictionaryItem> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<DictionaryItemDto> list = new ArrayList<DictionaryItemDto>( entityList.size() );
        for ( DictionaryItem dictionaryItem : entityList ) {
            list.add( toDto( dictionaryItem ) );
        }

        return list;
    }
}
