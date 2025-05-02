import axios from '../api/axiosConfig';

const register = async (data) => {
  const response = await axios.post('/auth/register', data);
  return response.data;
};

const login = async (data) => {
  const response = await axios.post('/auth/login', data);
  return response.data;
};

const getUserProfile = async (token) => {
  const response = await axios.get('/auth/profile', {
    headers: { Authorization: `Bearer ${token}` },
  });
  return response.data;
};

export default {
  register,
  login,
  getUserProfile,
}; 