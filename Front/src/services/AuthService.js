// AuthService.js

import axios from 'axios';

const API_URL = 'http://localhost:8080';

export const register = async (userData) => {
  try {
    const response = await axios.post(`${API_URL}/auth/register`, userData);
    return response.data; // Puedes devolver más información según lo que necesites
  } catch (error) {
    throw new Error('Registration failed'); // Maneja el error según tu lógica
  }
};

export const login = async (username, password) => {
  try {
    const response = await axios.post(`${API_URL}/auth/login`, {
      username,
      password
    });
    if (response.data.token) {
      localStorage.setItem('token', response.data.token); // Almacena el token en localStorage
    }
    return response.data;
  } catch (error) {
    throw new Error(`Login failed: ${error.message}`);
  }
};

export const logout = () => {
  localStorage.removeItem('token'); // Elimina el token al cerrar sesión
};

export const authHeader = () => {
  const token = localStorage.getItem('token');
  if (token) {
    return { Authorization: `Bearer ${token}` };
  } else {
    return {};
  }
};
