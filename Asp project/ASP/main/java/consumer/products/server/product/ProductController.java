package com.example.restapp;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


import java.util.List;
import java.util.stream.Collectors;

// Indicates that the data from our methods will be injected to the response payload (body)

@RestController
public class ProductController {
    private ProductRepo productDatabase;
    private ProductEntityFactory productEntityFactory;

    public ProductController(ProductRepo productDatabase,ProductEntityFactory productEntityFactory){
        this.productDatabase = productDatabase;
        this.productEntityFactory = productEntityFactory;
    }

//mohamad
    // מטודה מספר 1
    @GetMapping("/products")
    public ResponseEntity<CollectionModel<EntityModel<Product>>> getAllProducts(){
        List<EntityModel<Product>> products = productDatabase.findAll().stream()
                .map(product -> productEntityFactory.toModel(product))
                .collect(Collectors.toList());
        return ResponseEntity
                .ok(CollectionModel.of(products));
    }

//basher
    // מטודה מספר 2
    @GetMapping("products/{id}")
    public ResponseEntity<EntityModel<Product>> singleProduct(@PathVariable Long id){
        Product product = productDatabase.findById(id)
                .orElseThrow(()->new ProductNotFoundException(id));
        return ResponseEntity.ok(productEntityFactory.toModel(product));
    }

    /*
    ResponseEntity ->
    הוא מיכל מיוחד עבור Spring MVC המאפשר הוספה של קודי תגובה + נתונים
    */
    @PostMapping("/products")
    ResponseEntity<?> createProduct(@RequestBody Product newProduct){
        EntityModel<Product> productRepresentation
                = productEntityFactory.toModel(productDatabase.save(newProduct));
        return ResponseEntity
                .created(productRepresentation.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(productRepresentation);
    }


    @PutMapping("/products/{id}")
    ResponseEntity<?> replaceProduct(@RequestBody Product aProduct, @PathVariable Long id){
        Product updatedProduct =  productDatabase.findById(id).map(productToUpdate->{
                    productToUpdate.setProductName(aProduct.getProductName());
                    productToUpdate.setCategory((aProduct.getCategory()));
                    productToUpdate.setPrice(aProduct.getPrice());
                    return productDatabase.save(productToUpdate);

                })
                .orElseGet(()->{
                    aProduct.setId(id);
                    return productDatabase.save(aProduct);
                });
        EntityModel<Product> productRepresentation =
                productEntityFactory.toModel(productDatabase.save(updatedProduct));
        return ResponseEntity
                .created(productRepresentation.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(productRepresentation);
    }


    @DeleteMapping("/products/{id}")
    ResponseEntity<?> deleteProduct( @PathVariable(value = "id") Long productID) {
        // מוחק המוצר ה id שהוא בתוך products מתוך -> productDatabase
        productDatabase.deleteById(productID);
        // מחזיר בסוף דרך ResponseEntity משפט וגם מוסיף סטאטוס 200
        return ResponseEntity.ok(productEntityFactory.toModel());
    }
