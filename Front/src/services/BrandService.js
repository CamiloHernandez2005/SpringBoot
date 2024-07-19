import apiClient from '../axios-interceptor'; // Ajusta la ruta según tu estructura de archivos

const brandService = {
  getBrands() {
    return apiClient.get('/brands');
  },

  registerBrand(brandData) {
    return apiClient.post('/brands', brandData);
  },

  updateBrand(brandData) {
    return apiClient.put(`/brands/${brandData.id}`, brandData);
  },

  deleteBrand(brandId) {
    return apiClient.delete(`/brands/${brandId}`);
  },
};

export default brandService;
