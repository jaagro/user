package com.jaagro.user.web.mapper;

import com.jaagro.user.api.dto.response.ReturnPermissionDto;
import com.jaagro.user.web.vo.PermissionVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author tonyZheng
 * @date 2019-01-24 11:13
 */
@Mapper(componentModel = "spring")
public interface DtoToVoUtils {

    DtoToVoUtils INSTANCE = Mappers.getMapper(DtoToVoUtils.class);

    PermissionVo toPermissionVo(ReturnPermissionDto permissionDto);

    List<PermissionVo> toPermissionList(List<ReturnPermissionDto> permissionDtoList);
}
