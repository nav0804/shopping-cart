package com.nav.shoppingcart.services.impl;

import com.nav.shoppingcart.entities.Category;
import com.nav.shoppingcart.entities.Product;
import com.nav.shoppingcart.exceptions.ProductNotFoundException;
import com.nav.shoppingcart.repository.CategoryRepository;
import com.nav.shoppingcart.repository.ProductRepository;
import com.nav.shoppingcart.request.AddProductRequest;
import com.nav.shoppingcart.request.UpdateProductRequest;
import com.nav.shoppingcart.services.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product addProduct(AddProductRequest product) {
        Category existingCategory = Optional.ofNullable(categoryRepository.findByName(product.getCategory().getName()))
                .orElseGet(()-> {
                    Category newCat = new Category(product.getCategory().getName());
                    return categoryRepository.save(newCat);
                });
        product.setCategory(existingCategory);
        return productRepository.save(createProduct(product,existingCategory));
    }

    private Product createProduct(AddProductRequest productRequest, Category category){
        return new Product(
                productRequest.getName(),
                productRequest.getBrand(),
                productRequest.getDescription(),
                productRequest.getPrice(),
                productRequest.getInventory(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not found"));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,()->{
                    throw new ProductNotFoundException("Product not found");
        });
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long id) {
        return productRepository.findById(id)
                .map(existingProduct -> updateExistingProduct(existingProduct,request))
                .map(productRepository::save)
                .orElseThrow(()-> new ProductNotFoundException("Product not found"));
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request){
        existingProduct.setBrand(request.getBrand());
        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setPrice(request.getPrice());

        Category existingCategory = new Category(request.getCategory().getName());
        existingProduct.setCategory(existingCategory);
        return existingProduct;
    }

    @Override
    public List<Product> allProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> productByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> productByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> productsByBrandAndCategory(String brand, String category) {
        return productRepository.findByCategoryNameAndBrand(brand,category);
    }

    @Override
    public List<Product> productByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public Long countProductsByNameAndBrand(String name, String brand) {
        return productRepository.countByBrandAndName(name,brand);
    }
}
