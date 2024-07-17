import { createRouter, createWebHistory } from 'vue-router';
import ProductList from '../components/ProductList.vue';
import LoginComponent from '../components/LoginComponent.vue';
import RegisterComponent from '@/components/RegisterComponent.vue';
import BrandList from '@/components/BrandList.vue';
import axios from 'axios';


const routes = [
  {
    path: '/home',
    name: 'ProductList',
    component: ProductList,
    meta: { requiresAuth: true } // Define que esta ruta requiere autenticación
  },
  {
    path: '/',
    name: 'Login',
    component: LoginComponent,
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterComponent,
  },
  {
    path: '/brands',
    name: 'Brands',
    component: BrandList,
  },
  // Puedes añadir más rutas aquí según sea necesario
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Guard de navegación para verificar la autenticación
router.beforeEach(async (to, from, next) => {
  // Verificar si la ruta requiere autenticación
  if (to.matched.some(record => record.meta.requiresAuth)) {
    // Comprobar si el usuario está autenticado usando el token almacenado localmente
    const token = localStorage.getItem('token');
    if (!token) {
      // Si no hay token, redirige a la página de inicio de sesión
      next({ name: 'Login' });
    } else {
      try {
        // Verificar la validez del token haciendo una solicitud al backend
        await axios.get('http://localhost:8080/auth/verifyToken', {
          headers: {
            Authorization: `Bearer ${token}` // Agrega el token al encabezado Authorization
          }
        });
        // Si la verificación es exitosa, permite acceder a la ruta protegida
        next();
      } catch (error) {
        console.error('Error al verificar token:', error);
        // Si hay un error o el token no es válido, redirige a la página de inicio de sesión
        next({ name: 'Login' });
      }
    }
  } else {
    // Si la ruta no requiere autenticación, permite acceder directamente
    next();
  }
});

export default router;
