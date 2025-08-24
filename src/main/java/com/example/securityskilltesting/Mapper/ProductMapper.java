package com.example.securityskilltesting.Mapper;

import com.example.securityskilltesting.Dto.ProductDto;
import com.example.securityskilltesting.Entity.products;

import java.util.Base64;

public class ProductMapper {
    public static ProductDto mapToproductDto(products products){

        ProductDto productDto = new ProductDto();
        productDto.setId(products.getId());
        productDto.setName(products.getName());
        productDto.setPrice(products.getPrice());
        productDto.setDescription(products.getDescription());
        productDto.setQuantity(products.getQuantity());
        if (products.getImage()!=null){
            productDto.setImage(Base64.getEncoder().encodeToString(products.getImage()));
        };




        return productDto;
    }
        public static products mapToproduct(ProductDto productDto){
        products products = new products();
        products.setId(productDto.getId());
        products.setName(productDto.getName());
        products.setPrice(productDto.getPrice());
        products.setDescription(productDto.getDescription());
        products.setQuantity(productDto.getQuantity());
        if (productDto.getImage()!=null){
            products.setImage(Base64.getDecoder().decode(productDto.getImage()));
        }



        return products;
        }


}
