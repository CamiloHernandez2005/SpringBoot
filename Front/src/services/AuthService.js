// AuthService.js

import axios from 'axios';

const API_URL = 'http://localhost:8080';

export const register = async (userData) => {
  try {
    const response = await axios.post(`${API_URL}/auth/register`, userData);
    return response.data;
  } catch (error) {
    throw new Error('Registration failed');
  }
};

export const login = async (username, password) => {
  try {
    const response = await axios.post(`${API_URL}/auth/login`, { username, password });
    if (response.data.token) {
      localStorage.setItem('token', response.data.token);

      // Almacenar roles en localStorage
      const roles = response.data.roles || [];
      localStorage.setItem('roles', JSON.stringify(roles));
    }
    return response.data;
  } catch (error) {
    throw new Error(`Login failed: ${error.message}`);
  }
};

export const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('roles');
};

export const authHeader = () => {
  const token = localStorage.getItem('token');
  if (token) {
    return { Authorization: `Bearer ${token}` };
  } else {
    return {};
  }
};

export const getRoles = () => {
  const roles = localStorage.getItem('roles');
  return roles ? JSON.parse(roles) : [];
};