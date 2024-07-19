// axios-interceptor.js

import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080', // Base URL de tu backend
});

// Interceptor para agregar el token de autenticación a las solicitudes
apiClient.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');
        console.log('Token en el interceptor:', token); // Verifica el token en el interceptor
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

export default apiClient;
