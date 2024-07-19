// router/index.js

import { createRouter, createWebHistory } from 'vue-router';
import ProductList from '../components/ProductList.vue';
import LoginComponent from '../components/LoginComponent.vue';
import RegisterComponent from '@/components/RegisterComponent.vue';
import BrandList from '@/components/BrandList.vue';
import UnauthorizedComponent from '@/components/Unauthorized.vue';
import apiClient from '../axios-interceptor';

const routes = [
  {
    path: '/home',
    name: 'ProductList',
    component: ProductList,
    meta: { requiresAuth: true, roles: ['USER', 'ADMIN'] } // Define que esta ruta requiere autenticación y roles permitidos
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
    meta: { requiresAuth: true, roles: ['ADMIN'] } // Solo ADMIN puede acceder a /brands
  },
  {
    path: '/unauthorized',
    name: 'Unauthorized',
    component: UnauthorizedComponent
  },
  
  // Puedes añadir más rutas aquí según sea necesario
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Guard de navegación para verificar la autenticación y roles
router.beforeEach(async (to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    const token = localStorage.getItem('token');
    console.log('Token en localStorage:', token); // Verifica el token almacenado

    if (!token) {
      next({ name: 'Login' });
    } else {
      try {
        const response = await apiClient.get('/auth/verifyToken', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });

        // Verifica la estructura de la respuesta
        const roles = Array.isArray(response.data.roles) ? response.data.roles : [];
        const allowedRoles = Array.isArray(to.meta.roles) ? to.meta.roles : [];

        console.log('Roles obtenidos:', roles); // Verifica los roles obtenidos del backend
        console.log('Roles permitidos:', allowedRoles); // Verifica los roles permitidos para la ruta

        // Verifica si el usuario tiene al menos uno de los roles permitidos
        if (allowedRoles.some(role => roles.includes(role))) {
          next();
        } else {
          next('/unauthorized');
        }
      } catch (error) {
        console.error('Error al verificar token:', error);
        next({ name: 'Login' });
      }
    }
  } else {
    next();
  }
});

export default router;
