package com.example.securityskilltesting.Service;

import com.example.securityskilltesting.Dto.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    ProductDto createProduct(String name, String description, Long price, String quantity, MultipartFile image) throws IOException;
    ProductDto getProductById(Long id);
    ProductDto updateProduct(String name, String description, Long price, String quantity, Long productId, MultipartFile image) throws IOException;
    void deleteProduct(Long productId);
    List<ProductDto> getAllProducts();
//void savedProdu
}
