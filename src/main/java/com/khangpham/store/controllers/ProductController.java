package com.khangpham.store.controllers;

import com.khangpham.store.dtos.ProductDto;
import com.khangpham.store.entities.Category;
import com.khangpham.store.entities.Product;
import com.khangpham.store.mappers.ProductMapper;
import com.khangpham.store.repositories.CategoryRepository;
import com.khangpham.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll(
            @RequestParam(required = false, name = "categoryId") Long categoryId
    ) {
        List<ProductDto> response;
        if (categoryId != null) {
            response =  productRepository.findAllByCategoryId(categoryId)
                    .stream()
                    .map(productMapper::toDto)
                    .toList();
        } else {
            response = productRepository.findAll()
                    .stream()
                    .map(productMapper::toDto)
                    .toList();
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getOne (
            @PathVariable Long id
    ) {
        var response = productRepository.findById(id).orElse(null);
        if (response == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(productMapper.toDto(response));
        }
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct (
            @RequestBody ProductDto request,
            UriComponentsBuilder uriBuilder
    ) {
        Category category = categoryRepository.findById(request.getCategoryId().byteValue()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        Product product = productMapper.toEntity(request);
        product.setCategory(category);
        productRepository.save(product);

        request.setId(product.getId());
        URI uri = uriBuilder.path("/products/{id}").buildAndExpand(request.getId()).toUri();
        return ResponseEntity.created(uri).body(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct (
            @PathVariable(name = "id") Long id,
            @RequestBody ProductDto request
    ) {
        Category category = categoryRepository.findById(request.getCategoryId().byteValue()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        productMapper.update(request, product);
        product.setCategory(category);
        productRepository.save(product);
        request.setId(product.getId());

        return ResponseEntity.ok(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct (
            @PathVariable(name = "id") Long id
    ) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
