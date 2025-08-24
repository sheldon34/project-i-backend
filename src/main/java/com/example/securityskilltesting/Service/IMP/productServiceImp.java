package com.example.securityskilltesting.Service.IMP;

import com.example.securityskilltesting.Dto.ProductDto;
import com.example.securityskilltesting.Entity.products;
import com.example.securityskilltesting.Mapper.ProductMapper;
import com.example.securityskilltesting.Repo.ProductRepo;
import com.example.securityskilltesting.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
@Service
public class productServiceImp implements ProductService {
    @Autowired
    private ProductRepo productrepo;
    @Override
    public ProductDto createProduct(String name, String description, Long price, String quantity, MultipartFile image) throws IOException {
//        try{
//        products products= ProductMapper.mapToproduct(productdto);
//        products savedProduct = productrepo.save(products);
//
//
//        return ProductMapper.mapToproductDto(savedProduct);}
//        catch(Exception e){
//            throw  new RuntimeException(e.getMessage());
//        }

        products product=new products();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);

        if (!image.isEmpty()) {
            product.setImage(image.getBytes());
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


        if(image != null && !image.isEmpty()) {
            product.setImage(image.getBytes());
        }


        products updatedProduct = productrepo.save(product);

        return com.example.securityskilltesting.Mapper.ProductMapper.mapToproductDto(updatedProduct);
    }
    ///deleting a products
    @Override
    public void deleteProduct(Long productId) {
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
