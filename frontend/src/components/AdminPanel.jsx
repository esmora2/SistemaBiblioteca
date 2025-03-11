import { useEffect, useState } from 'react';
import api from '../api/api';

const AdminPanel = () => {
  const [usuarios, setUsuarios] = useState([]);

  useEffect(() => {
    api.get('admin/usuarios').json().then(setUsuarios).catch(console.error);
  }, []);

  return (
    <div>
      <h2>Administración de Usuarios</h2>
      <ul>
        {usuarios.map((user) => (
          <li key={user.id}>{user.nombre} - {user.email} ({user.rol})</li>
        ))}
      </ul>
    </div>
  );
};

export default AdminPanel;
