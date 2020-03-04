package com.softserve.maklertaboo.mapping.request;

import com.softserve.maklertaboo.dto.request.RequestForUserDto;
import com.softserve.maklertaboo.entity.request.RequestForUserVerification;
import com.softserve.maklertaboo.mapping.MapperToDto;
import com.softserve.maklertaboo.mapping.MapperToEntity;
import com.softserve.maklertaboo.mapping.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestForUserMapper implements MapperToDto<RequestForUserVerification, RequestForUserDto>,
        MapperToEntity<RequestForUserDto, RequestForUserVerification> {

    private UserMapper userMapper;

    @Autowired
    public RequestForUserMapper(UserMapper flatMapper) {
        this.userMapper = flatMapper;
    }

    @Override
    public RequestForUserDto convertToDto(RequestForUserVerification requestForUser) {
        RequestForUserDto requestForUserDto = new RequestForUserDto();
        requestForUserDto.setCreationDate(requestForUser.getCreationDate());

        requestForUserDto.setApprovalDate(requestForUser.getApprovalDate());
        requestForUserDto.setId(requestForUser.getId());

        requestForUserDto.setApproved(requestForUser.getIsApproved());

        requestForUserDto.setUser(userMapper.convertToDto(requestForUser.getUser()));

        return requestForUserDto;
    }

    @Override
    public RequestForUserVerification convertToEntity(RequestForUserDto dto) {
        return null;
    }
}