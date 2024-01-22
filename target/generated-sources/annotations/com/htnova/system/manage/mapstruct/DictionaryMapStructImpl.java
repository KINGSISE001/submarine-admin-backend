package com.htnova.system.manage.mapstruct;

import com.htnova.system.manage.dto.DictionaryDto;
import com.htnova.system.manage.dto.DictionaryItemDto;
import com.htnova.system.manage.entity.Dictionary;
import com.htnova.system.manage.entity.DictionaryItem;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-18T11:06:29+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_341 (Oracle Corporation)"
)
public class DictionaryMapStructImpl implements DictionaryMapStruct {

    @Override
    public Dictionary toEntity(DictionaryDto dto) {
        if ( dto == null ) {
            return null;
        }

        Dictionary dictionary = new Dictionary();

        dictionary.setId( dto.getId() );
        dictionary.setRemark( dto.getRemark() );
        dictionary.setCreateBy( dto.getCreateBy() );
        dictionary.setCreatorName( dto.getCreatorName() );
        dictionary.setCreateTime( dto.getCreateTime() );
        dictionary.setUpdateBy( dto.getUpdateBy() );
        dictionary.setUpdateTime( dto.getUpdateTime() );
        dictionary.setDelFlag( dto.getDelFlag() );
        dictionary.setName( dto.getName() );
        dictionary.setDictionaryItemList( dictionaryItemDtoListToDictionaryItemList( dto.getDictionaryItemList() ) );

        return dictionary;
    }

    @Override
    public DictionaryDto toDto(Dictionary entity) {
        if ( entity == null ) {
            return null;
        }

        DictionaryDto dictionaryDto = new DictionaryDto();

        dictionaryDto.setId( entity.getId() );
        dictionaryDto.setRemark( entity.getRemark() );
        dictionaryDto.setCreateBy( entity.getCreateBy() );
        dictionaryDto.setCreatorName( entity.getCreatorName() );
        dictionaryDto.setCreateTime( entity.getCreateTime() );
        dictionaryDto.setUpdateBy( entity.getUpdateBy() );
        dictionaryDto.setUpdateTime( entity.getUpdateTime() );
        dictionaryDto.setDelFlag( entity.getDelFlag() );
        dictionaryDto.setName( entity.getName() );
        dictionaryDto.setDictionaryItemList( dictionaryItemListToDictionaryItemDtoList( entity.getDictionaryItemList() ) );

        return dictionaryDto;
    }

    @Override
    public List<Dictionary> toEntity(List<DictionaryDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Dictionary> list = new ArrayList<Dictionary>( dtoList.size() );
        for ( DictionaryDto dictionaryDto : dtoList ) {
            list.add( toEntity( dictionaryDto ) );
        }

        return list;
    }

    @Override
    public List<DictionaryDto> toDto(List<Dictionary> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<DictionaryDto> list = new ArrayList<DictionaryDto>( entityList.size() );
        for ( Dictionary dictionary : entityList ) {
            list.add( toDto( dictionary ) );
        }

        return list;
    }

    protected DictionaryItem dictionaryItemDtoToDictionaryItem(DictionaryItemDto dictionaryItemDto) {
        if ( dictionaryItemDto == null ) {
            return null;
        }

        DictionaryItem dictionaryItem = new DictionaryItem();

        dictionaryItem.setId( dictionaryItemDto.getId() );
        dictionaryItem.setRemark( dictionaryItemDto.getRemark() );
        dictionaryItem.setCreateBy( dictionaryItemDto.getCreateBy() );
        dictionaryItem.setCreatorName( dictionaryItemDto.getCreatorName() );
        dictionaryItem.setCreateTime( dictionaryItemDto.getCreateTime() );
        dictionaryItem.setUpdateBy( dictionaryItemDto.getUpdateBy() );
        dictionaryItem.setUpdateTime( dictionaryItemDto.getUpdateTime() );
        dictionaryItem.setDelFlag( dictionaryItemDto.getDelFlag() );
        dictionaryItem.setDictionaryId( dictionaryItemDto.getDictionaryId() );
        dictionaryItem.setLabel( dictionaryItemDto.getLabel() );
        dictionaryItem.setValue( dictionaryItemDto.getValue() );
        dictionaryItem.setSort( dictionaryItemDto.getSort() );

        return dictionaryItem;
    }

    protected List<DictionaryItem> dictionaryItemDtoListToDictionaryItemList(List<DictionaryItemDto> list) {
        if ( list == null ) {
            return null;
        }

        List<DictionaryItem> list1 = new ArrayList<DictionaryItem>( list.size() );
        for ( DictionaryItemDto dictionaryItemDto : list ) {
            list1.add( dictionaryItemDtoToDictionaryItem( dictionaryItemDto ) );
        }

        return list1;
    }

    protected DictionaryItemDto dictionaryItemToDictionaryItemDto(DictionaryItem dictionaryItem) {
        if ( dictionaryItem == null ) {
            return null;
        }

        DictionaryItemDto dictionaryItemDto = new DictionaryItemDto();

        dictionaryItemDto.setId( dictionaryItem.getId() );
        dictionaryItemDto.setRemark( dictionaryItem.getRemark() );
        dictionaryItemDto.setCreateBy( dictionaryItem.getCreateBy() );
        dictionaryItemDto.setCreatorName( dictionaryItem.getCreatorName() );
        dictionaryItemDto.setCreateTime( dictionaryItem.getCreateTime() );
        dictionaryItemDto.setUpdateBy( dictionaryItem.getUpdateBy() );
        dictionaryItemDto.setUpdateTime( dictionaryItem.getUpdateTime() );
        dictionaryItemDto.setDelFlag( dictionaryItem.getDelFlag() );
        dictionaryItemDto.setDictionaryId( dictionaryItem.getDictionaryId() );
        dictionaryItemDto.setLabel( dictionaryItem.getLabel() );
        dictionaryItemDto.setValue( dictionaryItem.getValue() );
        dictionaryItemDto.setSort( dictionaryItem.getSort() );

        return dictionaryItemDto;
    }

    protected List<DictionaryItemDto> dictionaryItemListToDictionaryItemDtoList(List<DictionaryItem> list) {
        if ( list == null ) {
            return null;
        }

        List<DictionaryItemDto> list1 = new ArrayList<DictionaryItemDto>( list.size() );
        for ( DictionaryItem dictionaryItem : list ) {
            list1.add( dictionaryItemToDictionaryItemDto( dictionaryItem ) );
        }

        return list1;
    }
}
