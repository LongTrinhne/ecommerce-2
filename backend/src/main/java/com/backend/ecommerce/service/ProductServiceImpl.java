package com.backend.ecommerce.service;

import com.backend.ecommerce.entity.Category;
import com.backend.ecommerce.entity.Product;
import com.backend.ecommerce.exception.ProductException;
import com.backend.ecommerce.repository.CategoryRepository;
import com.backend.ecommerce.repository.ProductRepository;
import com.backend.ecommerce.request.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepositiry) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepositiry;
    }
    @Override
    public Product findProductById(Long productId) throws ProductException {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            return product.get();
        }
        throw new ProductException("Product with id " + productId + "  not exist");
    }
    @Override
    public List<Product> findProductByCategory(String category) throws ProductException {
        return productRepository.findProductByCategory(category);
    }
    @Override
    public Product createProduct(ProductRequest productRequest) {

        Category topLevel = categoryRepository.findByName(productRequest.getTopLevelCategory());

        Category secondLevel = categoryRepository.
                findByNameAndParent(productRequest.getSecondLevelCategory(), topLevel.getName());

        Category thirdLevel = categoryRepository.
                findByNameAndParent(productRequest.getThirdLevelCategory(), secondLevel.getName());

        if (topLevel == null) {
            Category topLevelCategory = new Category();

            topLevelCategory.setName(productRequest.getTopLevelCategory());
            topLevelCategory.setLevel(1);

            topLevel = categoryRepository.save(topLevelCategory);
        }

        if (secondLevel == null) {
            Category secondLevelCategory = new Category();

            secondLevelCategory.setName(productRequest.getSecondLevelCategory());
            secondLevelCategory.setLevel(2);
            secondLevelCategory.setParentCategory(topLevel);

            secondLevel = categoryRepository.save(secondLevelCategory);
        }

        if (thirdLevel == null) {
            Category thirdLevelCategory = new Category();

            thirdLevelCategory.setName(productRequest.getSecondLevelCategory());
            thirdLevelCategory.setLevel(3);
            thirdLevelCategory.setParentCategory(secondLevel);

            thirdLevel = categoryRepository.save(thirdLevelCategory);
        }

        Product product = new Product();

        product.setProductId(0L);
        product.setTitle(productRequest.getTitle());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setDiscountedPrice(productRequest.getDiscountedPrice());
        product.setDiscountedPercent(productRequest.getDiscountedPercent());
        product.setQuantity(productRequest.getQuantity());
        product.setImageUrl(productRequest.getImageUrl());
        product.setCategory(thirdLevel);

        //Khi tạo một sản phẩm mới, tôi muốn xác định Product thuộc vào danh mục cấp độ 3.
        // Điều này giúp quản lý và truy xuất sản phẩm dễ dàng dựa trên danh mục
        // và hỗ trợ hiển thị sản phẩm trong danh mục tương ứng trên trang web hoặc ứng dụng .

        return productRepository.save(product);
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);
        productRepository.delete(product);
        return "Product with id " + productId + " has been deleted.";
    }

    @Override
    // Chi cho thay doi so luong, khong thay doi properties khac tranh viec thay doi cau truc database
    public Product updateProduct(Long productId, Product product) throws ProductException {

        Product tmpProduct = findProductById(productId);

        // check quantity trong parameter Product not equal to 0
        if (product.getQuantity() != 0) {
            tmpProduct.setQuantity(product.getQuantity());
        }
        return productRepository.save(tmpProduct);
    }



    @Override
    public Page<Product> getAllProduct(String category, Long minPrice, Long maxPrice, Long minDiscount, String sort,
                                       String stock, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);

        if (stock != null) {
            if(stock.equals("in_stock"))
                products = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
            else if (stock.equals("out_of_stock"))
                products = products.stream().filter(p -> p.getQuantity() < 1).collect(Collectors.toList());
        }

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());

        List<Product> pageContent = products.subList(startIndex, endIndex);

        Page<Product> filteredProducts = new PageImpl<>(pageContent, pageable, products.size());

        return filteredProducts;
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
}
