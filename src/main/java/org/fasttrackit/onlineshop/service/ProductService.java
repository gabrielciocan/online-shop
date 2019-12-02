package org.fasttrackit.onlineshop.service;

import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fasttrackit.onlineshop.persistance.ProductRepository;
import org.fasttrackit.onlineshop.transfer.GetProductsRequest;
import org.fasttrackit.onlineshop.transfer.SaveProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(SaveProductRequest productRequest) {
        LOGGER.info("Creating product {}", productRequest);
        Product product = new Product();
        product.setDescription(productRequest.getDescription());
        product.setName(productRequest.getName());
        product.setImageUrl(productRequest.getImageUrl());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());
        return productRepository.save(product);
    }

    public Product getProduct(long id) {
        LOGGER.info("Retrieving product by id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product " + id + " does not exist"));

    }

    public Page<Product> getProducts(GetProductsRequest getProductsRequest, Pageable pageable) {
        LOGGER.info("Retrieving products: {}", getProductsRequest);
        if (getProductsRequest != null && getProductsRequest.getPartialName() != null && getProductsRequest.getMinQuantity() != null) {
            return productRepository.findByNameContainingAndQuantityGreaterThanEqual(getProductsRequest.getPartialName(), getProductsRequest.getMinQuantity(), pageable);
        } else if (getProductsRequest != null && getProductsRequest.getPartialName() != null) {
            return productRepository.findByNameContaining(getProductsRequest.getPartialName(), pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    public Product updateProduct(SaveProductRequest productRequest, long id) {
        LOGGER.info("Updating product {}: {}", id, productRequest);

        Product product = getProduct(id);
        BeanUtils.copyProperties(productRequest, product);
        return productRepository.save(product);
    }

    public void deleteProduct(long id) {
        LOGGER.info("Deleting product {}", id);
        productRepository.deleteById(id);
        LOGGER.info("Deleted product {}", id);
    }

}
