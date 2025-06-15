package com.app.ecom.service;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.mapper.ProductMapper;
import com.app.ecom.model.Product;
import com.app.ecom.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = productMapper.mapToProductEntity(productRequest);
        Product savedProduct = productRepository.save(product);
        return productMapper.mapToProductResponse(savedProduct);
    }

    public void updateProductFromRequest(Product existingProduct, ProductRequest productRequest) {
        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setStockQuantity(productRequest.getStockQuantity());
        existingProduct.setCategory(productRequest.getCategory());
        existingProduct.setImageUrl(productRequest.getImageUrl());
    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    updateProductFromRequest(existingProduct, productRequest);
                    Product updatedProduct = productRepository.save(existingProduct);
                    return productMapper.mapToProductResponse(updatedProduct);
                });
    }

    public List<ProductResponse> getProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(productMapper::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(exitingProduct -> {
                    exitingProduct.setActive(false);
                    productRepository.save(exitingProduct);
                    return true;
                }).orElse(false);
    }

    public List<ProductResponse> searchProduct(String keyword) {
        return productRepository.searchProducts(keyword)
                .stream()
                .map(productMapper::mapToProductResponse)
                .collect(Collectors.toList());
    }
}
