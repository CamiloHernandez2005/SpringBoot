import apiClient from '../axios-interceptor'; // Ajusta la ruta según tu estructura de archivos

const productService = {
  getProducts() {
    return apiClient.get('/products');
  },

  registerProduct(productData) {
    return apiClient.post('/products', productData);
  },

  updateProduct(productData) {
    return apiClient.put(`/products/${productData.id}`, productData);
  },

  deleteProduct(productId) {
    return apiClient.delete(`/products/${productId}`);
  },
};

export default productService;
