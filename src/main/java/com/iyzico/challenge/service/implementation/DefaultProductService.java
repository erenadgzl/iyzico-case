package com.iyzico.challenge.service.implementation;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.entity.Status;
import com.iyzico.challenge.exception.ProductAlreadyExistException;
import com.iyzico.challenge.exception.ProductNotFoundException;
import com.iyzico.challenge.model.ProductRequestModel;
import com.iyzico.challenge.model.ProductResponseModel;
import com.iyzico.challenge.model.SimplePage;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzico.challenge.service.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * @author erenadiguzel
 */

@Service
public class DefaultProductService implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultProductService.class);

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public DefaultProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Add new Product
     *
     * @param productRequestModel Contains product information
     * @return created product id
     * @throws ProductAlreadyExistException Thrown when adding product with same name
     */
    @Override
    @Transactional
    public Long create(ProductRequestModel productRequestModel) throws ProductAlreadyExistException {
        if(!checkProductNameIsAvailable(productRequestModel.getName())){
            logger.warn("The product already exists!");
            throw new ProductAlreadyExistException("The product already exists!");
        }
        Product product = modelMapper.map(productRequestModel, Product.class);
        productRepository.save(product);
        logger.info("Product saved successfully!");
        return product.getId();
    }

    /**
     * Find product by id
     *
     * @param id Product id
     * @return Product
     * @throws ProductNotFoundException Thrown when product not found given id
     */
    @Override
    public Product findById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Could not find entity with id: " + id));
    }

    /**
     * Find product response by id
     *
     * @param id Product id
     * @return Product response model
     * @throws ProductNotFoundException Thrown when product not found given id
     */
    @Override
    public ProductResponseModel getProductModelById(Long id) throws ProductNotFoundException {
        Product product = findById(id);
        return modelMapper.map(product, ProductResponseModel.class);
    }

    /**
     * Update product by id
     *
     * @param productRequestModel Contains product information
     * @param id Product id
     * @return Product id
     * @throws ProductNotFoundException Thrown when product not found given id
     */
    @Override
    @Transactional
    public Long update(ProductRequestModel productRequestModel, Long id) throws ProductNotFoundException, ProductAlreadyExistException {
        Product product = findById(id);
        if(!product.getName().equals(productRequestModel.getName()) && !checkProductNameIsAvailable(productRequestModel.getName())){
            logger.warn("The product already exists!");
            throw new ProductAlreadyExistException("The product already exists!");
        }
        product.setName(productRequestModel.getName());
        product.setDescription(productRequestModel.getDescription());
        product.setQuantity(productRequestModel.getQuantity());
        product.setPrice(productRequestModel.getPrice());
        productRepository.save(product);
        logger.info(String.format("Product updated successfully with id: %d",id));
        return id;
    }

    /**
     * Check Product name is available
     *
     * @param productName Product's name
     * @return boolean
     */
    private boolean checkProductNameIsAvailable(String productName) {
        return !productRepository.findByName(productName).isPresent();
    }

    /**
     * Delete product by id
     *
     * @param id Product id
     * @return Product id
     * @throws ProductNotFoundException Thrown when product not found given id
     */
    @Override
    @Transactional
    public Long delete(Long id) throws ProductNotFoundException {
        Product product = findById(id);
        product.setStatus(Status.DELETED);
        productRepository.save(product);
        logger.info(String.format("Product deleted successfully with id: %d",id));
        return id;
    }

    /**
     * Find all product
     *
     * @param pageable Given request params about Page information (size, sort...)
     * @return Simple page with product information
     */
    @Override
    public SimplePage<ProductResponseModel> findAll(Pageable pageable) {
        final Page<Product> page = productRepository.findAll(pageable);
        return new SimplePage<>(page.getContent()
                .stream()
                .map(product -> modelMapper.map(product, ProductResponseModel.class))
                .collect(Collectors.toList()),
                page.getTotalElements(), pageable);
    }
}
