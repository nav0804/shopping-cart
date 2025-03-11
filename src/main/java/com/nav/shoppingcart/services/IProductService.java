package com.nav.shoppingcart.services;

import com.nav.shoppingcart.entities.Product;
import com.nav.shoppingcart.request.AddProductRequest;
import com.nav.shoppingcart.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProduct(Long id);
    Product updateProduct(UpdateProductRequest request, Long id);

    List<Product> allProducts();
    List<Product> productByCategory(String category);
    List<Product> productByBrand(String brand);
    List<Product> productsByBrandAndCategory(String brand, String category);
    List<Product> productByName(String name);
    Long countProductsByNameAndBrand(String name, String brand);
}
