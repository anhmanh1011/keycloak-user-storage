package com.kss.keycloakuserstore.utils;

import com.kss.keycloakuserstore.entity.CfmastEntity;
import com.kss.keycloakuserstore.model.UserDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConvertUtils {
    public static UserDto convertCfmastToUserDto(CfmastEntity cfmastEntity) {
        if (cfmastEntity == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setUserName(cfmastEntity.getUserName());
        userDto.setEmail(cfmastEntity.getEmail());
        userDto.setFullName(cfmastEntity.getFullName());
        userDto.setPhone(cfmastEntity.getPhone());
        userDto.setId(cfmastEntity.getId());
        return userDto;
    }

    public static CfmastEntity convertUserDtoToEntity(UserDto userDto) {

        CfmastEntity cfmastEntity = new CfmastEntity();
        cfmastEntity.setUserName(userDto.getUserName());
        cfmastEntity.setEmail(userDto.getEmail());
        cfmastEntity.setFullName(userDto.getFullName());
        cfmastEntity.setPhone(userDto.getPhone());
        cfmastEntity.setId(userDto.getId());
        return cfmastEntity;
    }
}
