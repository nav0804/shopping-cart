package com.nav.shoppingcart.services.impl;

import com.nav.shoppingcart.entities.Product;
import com.nav.shoppingcart.request.AddProductRequest;

import java.util.List;

public interface ProductServiceImpl {
    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProduct(Long id);
    void updateProduct(Product product, Long id);

    List<Product> allProducts();
    List<Product> productByCategory(String category);
    List<Product> productByBrand(String brand);
    List<Product> productsByBrandAndCategory(String brand, String category);
    List<Product> productByName(String name);
    Long countProductsByNameAndBrand(String name, String brand);
}
