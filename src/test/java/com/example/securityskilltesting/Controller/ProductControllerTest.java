package com.example.securityskilltesting.Controller;

import com.example.securityskilltesting.Dto.ProductDto;
import com.example.securityskilltesting.Service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private productController controller;

    @Test
    void createProductUsesLowercaseQuantityWhenProvided() throws IOException {
        MockMultipartFile image = new MockMultipartFile("image", "test.png", "image/png", new byte[]{1, 2});
        ProductDto expected = new ProductDto(1L, "Item", 100L, "5", "Desc", "http://image.url");

        when(productService.createProduct(eq("Item"), eq("Desc"), eq(100L), eq("5"), eq(image)))
                .thenReturn(expected);

        ResponseEntity<ProductDto> response = controller.createProduct("Item", "Desc", 100L, null, "5", image);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(expected);
        verify(productService).createProduct("Item", "Desc", 100L, "5", image);
    }

    @Test
    void createProductFallsBackToUppercaseQuantityOrDefaults() throws IOException {
        ProductDto expected = new ProductDto(2L, "Item2", 200L, "10", "Desc2", null);

        when(productService.createProduct(eq("Item2"), eq("Desc2"), eq(200L), eq("10"), isNull()))
                .thenReturn(expected);

        ResponseEntity<ProductDto> response = controller.createProduct("Item2", "Desc2", 200L, "10", null, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(expected);
        verify(productService).createProduct("Item2", "Desc2", 200L, "10", null);
    }
}
