
<script>
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Dropdown from 'primevue/dropdown';
import apiClient from '@/axios-interceptor'; // Asegúrate de importar el cliente con el interceptor
import productService from '@/services/ProductService';
import brandService from '@/services/BrandService';
import { logout, getRoles } from '@/services/AuthService';

export default {
  name: 'ProductList',
  components: {
    DataTable,
    Column,
    Dialog,
    InputText,
    Dropdown,
  },
  data() {
    return {
      products: [],
      brands: [],
      roles: [],
      displayDialog: false,
      product: {
        name: '',
        price: '',
        date: '',
        brand: {
          id: null
        }
      },
      isEdit: false,
      dialogHeader: '',
      confirmDialog: false,
      productToDelete: null,
      isAdmin: false,
      errorMessage: '',
    };
  },
  async created() {
    try {
      // Obtener roles del usuario
      const token = localStorage.getItem('token');
      const response = await apiClient.get('/auth/verifyToken', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      this.roles = response.data.roles;
      console.log('Roles obtenidos en el componente:', this.roles);

      // Verifica si el usuario es admin
      this.isAdmin = this.checkUserRole('ADMIN');

      // Cargar datos
      await this.loadBrands();
      await this.loadProducts();
    } catch (error) {
      console.error('Error al obtener roles o cargar datos:', error);
      this.$router.push('/'); // Redirigir al login en caso de error
    }
  },
  methods: {
    async loadProducts() {
      try {
        const response = await productService.getProducts();
        this.products = response.data.map(product => ({
          ...product,
          brand: this.findBrandById(product.brand.id),
        }));
      } catch (error) {
        console.error('Error fetching products:', error);
      }
    },
    async loadBrands() {
      try {
        const response = await brandService.getBrands();
        this.brands = response.data;
      } catch (error) {
        console.error('Error fetching brands:', error);
      }
    },
    findBrandById(brandId) {
      const foundBrand = this.brands.find(brand => brand.id === brandId);
      return foundBrand ? { id: foundBrand.id, name: foundBrand.name } : { id: null, name: '' };
    },
    showModal(mode, product = null) {
      this.isEdit = mode === 'edit';
      this.dialogHeader = this.isEdit ? 'Editar Producto' : 'Registrar Producto';
      if (this.isEdit && product) {
        this.product = {
          ...product,
          brand: {
            id: product.brand.id
          }
        };
      } else {
        this.product = {
          name: '',
          price: '',
          date: '',
          brand: {
            id: null
          }
        };
      }
      this.displayDialog = true;
    },
    async saveProduct() {
      try {
        if (this.isEdit) {
          await productService.updateProduct(this.product);
          console.log('Producto actualizado');
        } else {
          await productService.registerProduct(this.product);
          console.log('Producto registrado');
        }
        this.displayDialog = false;
        await this.loadProducts(); // Recargar productos después de guardar
      } catch (error) {
        console.error('Error al guardar producto:', error);
      }
    },
    async deleteProduct(productId) {
      try {
        await productService.deleteProduct(productId);
        console.log('Producto eliminado');
        await this.loadProducts(); // Recargar productos después de eliminar
      } catch (error) {
        console.error('Error al eliminar producto:', error);
      }
      this.closeConfirmDialog();
    },
    openConfirmDialog(product) {
      this.productToDelete = product;
      this.confirmDialog = true;
    },
    closeConfirmDialog() {
      this.confirmDialog = false;
      this.productToDelete = null;
    },
    async performLogout() {
      try {
        await logout();
        this.$router.push('/');
      } catch (error) {
        console.error('Error al cerrar sesión:', error);
      }
    },
    checkUserRole(role) {
      return this.roles.includes(role);
    },
    editProduct(product) {
      this.showModal('edit', product);
    },
  },
};
</script>

<template>
  <div>
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-light bg-body-tertiary">
      <div class="container-fluid">
        <router-link to="/home" style="color: #393f81; margin-right: 2rem; margin-left: 3rem;">Productos</router-link>
        <router-link to="/brands" style="color: #393f81;">Marcas</router-link>
        <div class="d-flex align-items-center ms-auto">
          <button class="btn btn-outline-dark" @click="performLogout">
            <i class="fas fa-sign-out-alt me-1"></i>
            Log out :(
          </button>
        </div>
      </div>
    </nav>

    <br>
    
    <!-- Registrar Producto Button (Visible solo para Admin) -->
    <button v-if="isAdmin" class="p-button p-button-primary mb-2 buttonregistrar" @click="showModal('new')">Registrar Producto</button>

    <!-- DataTable -->
    <div class="container2">
      <DataTable :value="products">
        <Column field="id" header="ID" />
        <Column field="name" header="Nombre" />
        <Column field="price" header="Precio" />
        <Column field="date" header="Fecha" />
        <Column field="antiquity" header="Antigüedad" />
        <Column field="brand.name" header="Marca" />
        <Column v-if="isAdmin" header="Acciones">
          <template #body="slotProps">
            <button class="p-button p-button-success" @click="editProduct(slotProps.data)">Editar</button>
            <button class="p-button p-button-danger" @click="openConfirmDialog(slotProps.data)">Eliminar</button>
          </template>
        </Column>
      </DataTable>
    </div>

    <!-- Product Dialog -->
    <Dialog v-model:visible="displayDialog" :header="dialogHeader">
      <form @submit.prevent="saveProduct">
        <div>
          <label for="productName">Nombre:</label>
          <InputText id="productName" v-model="product.name" type="text" required class="p-inputtext" />
        </div>
        <br>
        <div>
          <label for="productPrice">Precio:</label>
          <InputText id="productPrice" v-model="product.price" type="number" required class="p-inputtext" />
        </div>
        <br>
        <div>
          <label for="productDate">Fecha:</label>
          <InputText id="productDate" v-model="product.date" type="date" required class="p-inputtext" />
        </div>
        <br>
        <div>
          <label for="productBrand">Marca:</label>
          <Dropdown
            id="productBrand"
            v-model="product.brand.id"
            :options="brands"
            optionLabel="name"
            optionValue="id"
            placeholder="Selecciona una Marca"
            required
            class="p-inputtext"
          />
        </div>
        <br>
        <button type="submit" class="p-button">{{ isEdit ? 'Actualizar' : 'Guardar' }}</button>
      </form>
    </Dialog>

    <!-- Confirm Dialog -->
    <Dialog v-model:visible="confirmDialog" header="Confirmar Eliminación">
      <p>¿Estás seguro que deseas eliminar el producto '{{ productToDelete ? productToDelete.name : '' }}'?</p><br>
      <div class="p-dialog-footer">
        <button class="p-button p-button-secondary" @click="closeConfirmDialog">Cancelar</button>
        <button class="p-button p-button-danger" @click="deleteProduct(productToDelete.id)">Eliminar</button>
      </div>
    </Dialog>
  </div>
</template>


<style scoped>
button {
  margin-right: 1rem;
}
label {
  margin-right: 1rem;
}
.rounded-full {
  margin-bottom: -8px;
  margin-left: 1rem;
  margin-right: 1rem;
}
.container2 {
  width: 90%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.p-datatable {
  width: 90%;
}

.buttonregistrar {
  margin-left: 6rem;
}

/* Alineación horizontal de los botones en la columna de acciones */
.p-datatable .p-column-filter {
  display: flex;
  justify-content: center;
}

.p-datatable .p-button {
  margin-right: 0.5rem; /* Espaciado entre los botones */
}

.p-dropdown {
  max-height: 300px; /* Ajusta la altura máxima del Dropdown */
  overflow-y: auto; /* Habilita el desplazamiento vertical si es necesario */
}
</style>
