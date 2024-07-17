<script>
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import brandService from '@/services/BrandService';
import { logout } from '@/services/AuthService';

export default {
  name: 'BrandList',
  components: {
    DataTable,
    Column,
    Dialog,
    InputText,
  },
  data() {
    return {
      brands: [],
      displayDialog: false,
      newBrand: {
        id: null,
        name: '',
      },
      isEdit: false,
      dialogHeader: '',
      confirmDialog: false,
      brandToDelete: null,
    };
  },
  async created() {
    await this.loadBrands();
  },
  methods: {
    async loadBrands() {
      try {
        const response = await brandService.getBrands();
        this.brands = response.data;
      } catch (error) {
        console.error('Error fetching brands:', error);
      }
    },
    showModal(mode, brand = null) {
      this.isEdit = mode === 'edit';
      this.dialogHeader = this.isEdit ? 'Editar Marca' : 'Registrar Marca';
      if (this.isEdit && brand) {
        this.newBrand = { ...brand };
      } else {
        this.newBrand = {
          id: null,
          name: '',
        };
      }
      this.displayDialog = true;
    },
    async saveBrand() {
      try {
        if (this.isEdit) {
          const response = await brandService.updateBrand(this.newBrand);
          console.log('Marca actualizada:', response.data);
        } else {
          const response = await brandService.registerBrand(this.newBrand);
          console.log('Marca registrada:', response.data);
        }
        this.displayDialog = false;
        await this.loadBrands();
      } catch (error) {
        console.error('Error al guardar marca:', error);
      }
    },
    editBrand(rowData) {
      this.showModal('edit', rowData);
    },
    async deleteBrand(brandId) {
      try {
        const response = await brandService.deleteBrand(brandId);
        console.log('Marca eliminada:', response.data);
        await this.loadBrands();
      } catch (error) {
        console.error('Error al eliminar marca:', error);
      }
      this.closeConfirmDialog();
    },
    openConfirmDialog(brand) {
      this.brandToDelete = brand;
      this.confirmDialog = true;
    },
    closeConfirmDialog() {
      this.confirmDialog = false;
      this.brandToDelete = null;
    },
    async performLogout() {
      try {
        await logout();
        this.$router.push('/');
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
  <div>
    <button class="p-button p-button-primary mb-2 buttonregistrar" @click="showModal('new')">Registrar Marca</button>
    <div class="container2">
      <DataTable :value="brands">
        <Column field="id" header="ID" />
        <Column field="name" header="Nombre" />
        <Column header="Acciones">
          <template #body="slotProps">
            <button class="p-button p-button-success" @click="editBrand(slotProps.data)">Editar</button>
            <button class="p-button p-button-danger" @click="openConfirmDialog(slotProps.data)">Eliminar</button>
          </template>
        </Column>
      </DataTable>
    </div>

    <Dialog v-model:visible="displayDialog" :header="dialogHeader">
      <form @submit.prevent="saveBrand">
        <div>
          <label for="brandName">Nombre:</label>
          <InputText id="brandName" v-model="newBrand.name" type="text" required class="p-inputtext" />
        </div>
        <br>
        <button type="submit" class="p-button">{{ isEdit ? 'Actualizar' : 'Guardar' }}</button>
      </form>
    </Dialog>

    <Dialog v-model:visible="confirmDialog" header="Confirmar Eliminación">
      <p>¿Estás seguro que deseas eliminar la marca '{{ brandToDelete ? brandToDelete.name : '' }}'?</p><br>
      <div class="p-dialog-footer">
        <button class="p-button p-button-secondary" @click="closeConfirmDialog">Cancelar</button>
        <button class="p-button p-button-danger" @click="deleteBrand(brandToDelete.id)">Eliminar</button>
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
</style>
