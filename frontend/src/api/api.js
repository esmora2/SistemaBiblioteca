import ky from 'ky';

const api = ky.create({
  prefixUrl: 'http://172.191.132.105:9090', // ✅ Solo la URL base del backend
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;
