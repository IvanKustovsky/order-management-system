package com.example.online_shop.mapper;

import com.example.online_shop.dto.ProductDto;
import com.example.online_shop.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto toDto(Product product);

    Product toEntity(ProductDto productDto);
}
