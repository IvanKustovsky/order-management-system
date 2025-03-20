package com.example.online_shop.mapper;

import com.example.online_shop.dto.OrderResponseDto;
import com.example.online_shop.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderResponseDto toOrderResponseDto(Order order);
}
