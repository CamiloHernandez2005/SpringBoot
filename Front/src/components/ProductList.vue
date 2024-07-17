<script>
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import productService from '@/services/ProductService';
import { logout } from '@/services/AuthService';

export default {
  name: 'ProductList',
  components: {
    DataTable,
    Column,
    Dialog,
    InputText,
  },
  data() {
    return {
      products: [],
      displayDialog: false,
      newProduct: {
        id: null,
        name: '',
        price: null,
        date: '',
        antiquity: '',
      },
      isEdit: false,
      dialogHeader: '',
      confirmDialog: false,
      productToDelete: null,
    };
  },
  async created() {
    await this.loadProducts();
  },
  methods: {
    async loadProducts() {
      try {
        const response = await productService.getProducts();
        this.products = response.data;
      } catch (error) {
        console.error('Error fetching products:', error);
      }
    },
    showModal(mode, product = null) {
      this.isEdit = mode === 'edit';
      this.dialogHeader = this.isEdit ? 'Editar Producto' : 'Registrar Producto';
      if (this.isEdit && product) {
        this.newProduct = { ...product };
      } else {
        this.newProduct = {
          id: null,
          name: '',
          price: null,
          date: '',
          antiquity: '',
        };
      }
      this.displayDialog = true;
    },
    async saveProduct() {
      try {
        if (this.isEdit) {
          const response = await productService.updateProduct(this.newProduct);
          console.log('Producto actualizado:', response.data);
        } else {
          const response = await productService.registerProduct(this.newProduct);
          console.log('Producto registrado:', response.data);
        }
        this.displayDialog = false;
        await this.loadProducts();
      } catch (error) {
        console.error('Error al guardar producto:', error);
      }
    },
    editProduct(rowData) {
      this.showModal('edit', rowData);
    },
    async deleteProduct(productId) {
      try {
        const response = await productService.deleteProduct(productId);
        console.log('Producto eliminado:', response.data);
        await this.loadProducts();
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
        await logout(); // Llama al método de logout del servicio de autenticación
        this.$router.push('/'); // Redirige a la página de inicio de sesión
      } catch (error) {
        console.error('Error al cerrar sesión:', error);
      }
    },
  },
  
};
</script>

<template>
<nav class="navbar navbar-expand-lg navbar-light bg-body-tertiary">
  <div class="container-fluid">
    <router-link to="/home"
    style="color: #393f81; margin-right: 2rem; margin-left: 3rem;">Productos</router-link>
    <router-link to="/brands"
    style="color: #393f81;">Marcas</router-link>
    <div class="d-flex align-items-center ms-auto">
      <button class="btn btn-outline-dark" @click="performLogout">
        <i class="fas fa-sign-out-alt me-1"></i>
        Log out :(
      </button>
    </div>
  </div>
</nav>

<br>
  <div>
    
    <button
      class="p-button p-button-primary mb-2 buttonregistrar"
      @click="showModal('new')"
    >
      Registrar Producto
    </button>
    <div class="container2"> <!-- Clase 'container' para centrar y márgenes y 'my-4' para márgenes verticalmente -->
      
  <DataTable :value="products">
    <Column
      field="id"
      header="ID"
    />
    <Column
      field="name"
      header="Nombre"
    />
    <Column
      field="price"
      header="Precio"
    />
    <Column
      field="date"
      header="Fecha"
    />
    <Column
      field="antiquity"
      header="Antigüedad"
    />
    <Column header="Acciones">
      <template #body="slotProps">
        <button
          class="p-button p-button-success"
          @click="editProduct(slotProps.data)"
        >
          Editar
        </button>
        <button
          class="p-button p-button-danger"
          @click="openConfirmDialog(slotProps.data)"
        >
          Eliminar
        </button>
      </template>
    </Column>
  </DataTable>
</div>


    <Dialog
      v-model:visible="displayDialog"
      :header="dialogHeader"
    >
      <form @submit.prevent="saveProduct">
        <div>
          <label for="productName">Nombre:</label>
          <InputText
            id="productName"
            v-model="newProduct.name"
            type="text"
            required
            class="p-inputtext"
          />
        </div>
        <br>
        <div>
          <label for="productPrice">Precio:</label>
          <InputText
            id="productPrice"
            v-model="newProduct.price"
            type="number"
            required
            class="p-inputtext"
          />
        </div>
        <br>
        <div>
          <label for="productDate">Fecha:</label>
          <InputText
            id="productDate"
            v-model="newProduct.date"
            type="date"
            required
            class="p-inputtext"
          />
        </div>
        <br>
        <button
          type="submit"
          class="p-button"
        >
          {{ isEdit ? 'Actualizar' : 'Guardar' }}
        </button>
      </form>
    </Dialog>

    <Dialog
      v-model:visible="confirmDialog"
      header="Confirmar Eliminación"
    >
      <p>¿Estás seguro que deseas eliminar el producto '{{ productToDelete ? productToDelete.name : '' }}'?</p><br>
      <div class="p-dialog-footer">
        <button
          class="p-button p-button-secondary"
          @click="closeConfirmDialog"
        >
          Cancelar
        </button>
        <button
          class="p-button p-button-danger"
          @click="deleteProduct(productToDelete.id)"
        >
          Eliminar
        </button>
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
.rounded-full{
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
  /* Otros estilos de datatable según necesites */
}

.buttonregistrar{
margin-left: 6rem;
}

</style>
