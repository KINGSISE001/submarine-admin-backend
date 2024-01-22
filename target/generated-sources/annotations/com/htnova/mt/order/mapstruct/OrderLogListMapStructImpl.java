package com.htnova.mt.order.mapstruct;

import com.htnova.mt.order.dto.OrderLogListDto;
import com.htnova.mt.order.entity.OrderLogList;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-18T11:06:29+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_341 (Oracle Corporation)"
)
public class OrderLogListMapStructImpl implements OrderLogListMapStruct {

    @Override
    public OrderLogList toEntity(OrderLogListDto dto) {
        if ( dto == null ) {
            return null;
        }

        OrderLogList orderLogList = new OrderLogList();

        if ( dto.getOrderId() != null ) {
            orderLogList.setOrderId( Long.parseLong( dto.getOrderId() ) );
        }
        orderLogList.setTitle( dto.getTitle() );
        orderLogList.setCode( dto.getCode() );
        orderLogList.setContent( dto.getContent() );
        orderLogList.setCreatedTime( dto.getCreatedTime() );

        return orderLogList;
    }

    @Override
    public OrderLogListDto toDto(OrderLogList entity) {
        if ( entity == null ) {
            return null;
        }

        OrderLogListDto orderLogListDto = new OrderLogListDto();

        if ( entity.getOrderId() != null ) {
            orderLogListDto.setOrderId( String.valueOf( entity.getOrderId() ) );
        }
        orderLogListDto.setTitle( entity.getTitle() );
        orderLogListDto.setCode( entity.getCode() );
        orderLogListDto.setContent( entity.getContent() );
        orderLogListDto.setCreatedTime( entity.getCreatedTime() );

        return orderLogListDto;
    }

    @Override
    public List<OrderLogList> toEntity(List<OrderLogListDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<OrderLogList> list = new ArrayList<OrderLogList>( dtoList.size() );
        for ( OrderLogListDto orderLogListDto : dtoList ) {
            list.add( toEntity( orderLogListDto ) );
        }

        return list;
    }

    @Override
    public List<OrderLogListDto> toDto(List<OrderLogList> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<OrderLogListDto> list = new ArrayList<OrderLogListDto>( entityList.size() );
        for ( OrderLogList orderLogList : entityList ) {
            list.add( toDto( orderLogList ) );
        }

        return list;
    }
}
