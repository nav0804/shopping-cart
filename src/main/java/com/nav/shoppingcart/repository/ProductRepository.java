package com.nav.shoppingcart.repository;

import com.nav.shoppingcart.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByCategoryName(String category);

    List<Product> findByBrand(String brand);

    List<Product> findByCategoryNameAndBrand(String brand, String category);

    List<Product> findByName(String name);

    Long countByBrandAndName(String name, String brand);
}
