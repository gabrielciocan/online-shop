package org.fasttrackit.onlineshop;

import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fasttrackit.onlineshop.service.ProductService;
import org.fasttrackit.onlineshop.transfer.SaveProductRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceIntegrationTests {

	@Autowired
	private ProductService productService;
	@Test
	public void testCreateProduct_whenValidRequest_thenProductIsSaved() {
		createProduct();
	}



	@Test(expected = TransactionSystemException.class)
	public void testCreateProduct_whenInvalidRequest_thenThrowTransactionSystemException(){
		SaveProductRequest request = new SaveProductRequest();

		Product createdProduct = productService.createProduct(request);


	}
	@Test
	public void testGetProduct_whenExistingProduct_thenProductIsReturned(){
		Product createdProduct = createProduct();
		Product retrievedProduct = productService.getProduct(createdProduct.getId());

		assertThat(retrievedProduct, notNullValue());
		assertThat(retrievedProduct.getId(), is(createdProduct.getId()));
		assertThat(retrievedProduct.getName(),is(createdProduct.getName()));
		assertThat(retrievedProduct.getDescription(),is(createdProduct.getDescription()));
		assertThat(retrievedProduct.getPrice(),is(createdProduct.getPrice()));
		assertThat(retrievedProduct.getQuantity(),is(createdProduct.getQuantity()));
	}
	@Test(expected = ResourceNotFoundException.class)
	public void testGetProduct_whenNonExistingProduct_thenThrowResourceNotFoundException(){
		productService.getProduct(0);
	}

	@Test
	public void testUpdateProduct_whenValidRequest_thenUpdateProduct(){
		Product createdProduct = createProduct();
		SaveProductRequest saveProductRequest = new SaveProductRequest();
		saveProductRequest.setName(createdProduct.getName() + " updated");
		saveProductRequest.setDescription(createdProduct.getDescription() + " updated");
		saveProductRequest.setPrice(createdProduct.getPrice() + 10);
		saveProductRequest.setQuantity(createdProduct.getQuantity() + 10);
		Product updateProduct = productService.updateProduct(saveProductRequest,createdProduct.getId());
		assertThat(updateProduct, notNullValue());
		assertThat(updateProduct.getName(),is(saveProductRequest.getName()));
		assertThat(updateProduct.getDescription(),is(saveProductRequest.getDescription()));
		assertThat(updateProduct.getPrice(),is(saveProductRequest.getPrice()));
		assertThat(updateProduct.getQuantity(),is(saveProductRequest.getQuantity()));
	}
	@Test(expected = ResourceNotFoundException.class)
	public void testUpdateProduct_whenNonExistingProduct_thenThrowResourceNotFoundException(){
		SaveProductRequest saveProductRequest = new SaveProductRequest();
		saveProductRequest.setName(" updated");
		saveProductRequest.setDescription(" updated");
		saveProductRequest.setPrice(10D);
		saveProductRequest.setQuantity(10);
		productService.updateProduct(saveProductRequest,0);
	}

	private Product createProduct() {
		SaveProductRequest request = new SaveProductRequest();
		request.setName("Banana " + System.currentTimeMillis());
		request.setPrice(5.0);
		request.setQuantity(100);
		request.setDescription("Healthy fruit");
		Product createdProduct = productService.createProduct(request);

		assertThat(createdProduct, notNullValue());
		assertThat(createdProduct.getId(),notNullValue());
		assertThat(createdProduct.getId(), greaterThan(0L));
		assertThat(createdProduct.getName(),is(request.getName()));
		assertThat(createdProduct.getDescription(),is(request.getDescription()));
		assertThat(createdProduct.getPrice(),is(request.getPrice()));
		assertThat(createdProduct.getQuantity(),is(request.getQuantity()));
		return createdProduct;
	}


}
