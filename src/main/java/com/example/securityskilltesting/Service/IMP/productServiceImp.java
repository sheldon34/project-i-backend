package com.example.securityskilltesting.Service.IMP;

import com.example.securityskilltesting.Cdn.MediaService;
import com.example.securityskilltesting.Dto.ProductDto;
import com.example.securityskilltesting.Entity.products;
import com.example.securityskilltesting.Mapper.ProductMapper;
import com.example.securityskilltesting.Repo.ProductRepo;
import com.example.securityskilltesting.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
@Service
@RequiredArgsConstructor
public class productServiceImp implements ProductService {

    private final  ProductRepo productrepo;
    private final MediaService mediaService;

    @Override
    public ProductDto createProduct(String name, String description, Long price, String quantity, MultipartFile image) throws IOException {

        products product=new products();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);

//        if (!image.isEmpty()) {
//            product.setImage(image.getBytes());
//        }
        if (image !=null && !image.isEmpty()) {
            String key=mediaService.uploadFile(image);
            String imageUrl= mediaService.getPublicUrl(key);
            product.setImageUrl(imageUrl);
        }
        products savedproducts= productrepo.save(product);

        return  com.example.securityskilltesting.Mapper.ProductMapper.mapToproductDto(savedproducts);

    }
    ///getting by id
    @Override
    public ProductDto getProductById(Long id)
    {
        products product= productrepo.findById(id).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return com.example.securityskilltesting.Mapper.ProductMapper.mapToproductDto(product);
    }

    @Override
    public ProductDto updateProduct(String name, String description, Long price, String quantity, Long productId, MultipartFile image) throws IOException {



        products product = productrepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        if (product.getName() !=null){

            product.setName(product.getName());}
        if(product.getDescription() !=null){
            product.setDescription(description);}
        if(product.getPrice() !=null){

            product.setPrice(price);
        }
        if(product.getQuantity() !=null){
            product.setQuantity(quantity);}

//
//        if(image != null && !image.isEmpty()) {
//            product.setImage(image.getBytes());
//        }
        if (image !=null && !image.isEmpty()){
            String newKey= mediaService.updateFile(product.getImageUrl(),image);
            String imageUrl= mediaService.getPublicUrl(newKey);
            product.setImageUrl(imageUrl);
        }
        products updatedProduct = productrepo.save(product);

        return ProductMapper.mapToproductDto(updatedProduct);
    }
    ///deleting a products
    @Override
    public void deleteProduct(Long productId) {
        products product=productrepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        if (product.getImageUrl() !=null){
            mediaService.deleteFile(product.getImageUrl());
        }
        productrepo.deleteById(productId);

    }
    // geting all products
    @Override
    public   List<ProductDto> getAllProducts() {
        List<products>  Products= productrepo.findAll();

        return Products.stream().map(products-> ProductMapper.mapToproductDto(products))
                .collect(Collectors.toList());
    }

//    @Override
//    public void savedProduct(MultipartFile file) throws IOException {
//
//    }
}
