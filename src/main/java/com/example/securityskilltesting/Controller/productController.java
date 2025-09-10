package com.example.securityskilltesting.Controller;

import com.example.securityskilltesting.Dto.ProductDto;
import com.example.securityskilltesting.Repo.ProductRepo;
import com.example.securityskilltesting.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

// @CrossOrigin(origins = "http://localhost:5174")
@RestController
@RequestMapping("/api/product")
public class productController {
    @Autowired
    private ProductService productservice;
    @Autowired
    private ProductRepo productrepo;

    ///  uploading products
    @PostMapping("/upload")
    public ResponseEntity<ProductDto> createProduct(
//@RequestBody ProductDto productdto,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Long price,
            @RequestParam("Quantity") String Quantity,
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        ProductDto newProduct= productservice.createProduct(name,description,price,Quantity,image);
        return  new ResponseEntity<>( newProduct,HttpStatus.CREATED);
    }

    ///  getAll products
    @GetMapping("/getAll")
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        List<ProductDto> productdto= productservice.getAllProducts();
        if (productdto.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productdto, HttpStatus.OK);
    }


    ///  getby id
    @GetMapping("/getById/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable("id")  Long id ){
        ProductDto productdto= productservice.getProductById(id);
        return ResponseEntity.ok(productdto);
    }


    /// update
    @PutMapping("update/{id}")
    public ResponseEntity<ProductDto>updateProduct(@PathVariable("id") Long id,
                                                   @RequestParam(value = "name",required = false) String name,
                                                   @RequestParam(value = "description",required = false) String description,
                                                   @RequestParam(value = "price", required = false) Long price,
                                                   @RequestParam(value = "Quantity" ,required = false) String Quantity,
                                                   @RequestParam(value = "image", required = false) MultipartFile image
    )throws IOException {

        ProductDto updatedProduct = productservice.updateProduct(name, description, price, Quantity, id, image);
        return ResponseEntity.ok(updatedProduct);
    }


    /// deleting product
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id")  Long id ){
        if (productservice.getProductById(id) != null) {
            productservice.deleteProduct(id);
        }
        return ResponseEntity.ok("deleted");
    }
}
