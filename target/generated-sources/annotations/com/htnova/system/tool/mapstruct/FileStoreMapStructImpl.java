package com.htnova.system.tool.mapstruct;

import com.htnova.system.tool.dto.FileStoreDto;
import com.htnova.system.tool.entity.FileStore;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-18T11:06:29+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_341 (Oracle Corporation)"
)
public class FileStoreMapStructImpl implements FileStoreMapStruct {

    @Override
    public FileStore toEntity(FileStoreDto dto) {
        if ( dto == null ) {
            return null;
        }

        FileStore fileStore = new FileStore();

        fileStore.setId( dto.getId() );
        fileStore.setRemark( dto.getRemark() );
        fileStore.setCreateBy( dto.getCreateBy() );
        fileStore.setCreatorName( dto.getCreatorName() );
        fileStore.setCreateTime( dto.getCreateTime() );
        fileStore.setUpdateBy( dto.getUpdateBy() );
        fileStore.setUpdateTime( dto.getUpdateTime() );
        fileStore.setDelFlag( dto.getDelFlag() );
        fileStore.setName( dto.getName() );
        fileStore.setRealName( dto.getRealName() );
        fileStore.setSize( dto.getSize() );
        fileStore.setType( dto.getType() );
        fileStore.setUrl( dto.getUrl() );
        fileStore.setMd5( dto.getMd5() );
        fileStore.setStoreType( dto.getStoreType() );

        return fileStore;
    }

    @Override
    public FileStoreDto toDto(FileStore entity) {
        if ( entity == null ) {
            return null;
        }

        FileStoreDto fileStoreDto = new FileStoreDto();

        fileStoreDto.setId( entity.getId() );
        fileStoreDto.setRemark( entity.getRemark() );
        fileStoreDto.setCreateBy( entity.getCreateBy() );
        fileStoreDto.setCreatorName( entity.getCreatorName() );
        fileStoreDto.setCreateTime( entity.getCreateTime() );
        fileStoreDto.setUpdateBy( entity.getUpdateBy() );
        fileStoreDto.setUpdateTime( entity.getUpdateTime() );
        fileStoreDto.setDelFlag( entity.getDelFlag() );
        fileStoreDto.setName( entity.getName() );
        fileStoreDto.setRealName( entity.getRealName() );
        fileStoreDto.setSize( entity.getSize() );
        fileStoreDto.setType( entity.getType() );
        fileStoreDto.setUrl( entity.getUrl() );
        fileStoreDto.setMd5( entity.getMd5() );
        fileStoreDto.setStoreType( entity.getStoreType() );

        return fileStoreDto;
    }

    @Override
    public List<FileStore> toEntity(List<FileStoreDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<FileStore> list = new ArrayList<FileStore>( dtoList.size() );
        for ( FileStoreDto fileStoreDto : dtoList ) {
            list.add( toEntity( fileStoreDto ) );
        }

        return list;
    }

    @Override
    public List<FileStoreDto> toDto(List<FileStore> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<FileStoreDto> list = new ArrayList<FileStoreDto>( entityList.size() );
        for ( FileStore fileStore : entityList ) {
            list.add( toDto( fileStore ) );
        }

        return list;
    }
}
