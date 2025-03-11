import { createContext, useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);
  const [rol, setRol] = useState(null); // ✅ Estado para almacenar el rol

  useEffect(() => {
    const storedToken = localStorage.getItem('token');
    const storedRol = localStorage.getItem('rol'); // ✅ Recuperamos el rol del localStorage

    if (storedToken) {
      try {
        const decoded = jwtDecode(storedToken);
        console.log("Usuario autenticado:", decoded);
        setUser(decoded);
        setToken(storedToken);
        setRol(storedRol); // ✅ Asegurarnos de guardar el rol
      } catch (error) {
        console.error("Error al decodificar el token:", error);
        localStorage.removeItem('token');
        localStorage.removeItem('rol');
        setUser(null);
        setToken(null);
        setRol(null);
      }
    }
  }, []);

  const login = (accessToken, idToken, userRole) => {
    localStorage.setItem('token', accessToken);
    localStorage.setItem('id_token', idToken);
    localStorage.setItem('rol', userRole); // ✅ Guardamos el rol en localStorage

    const decoded = jwtDecode(idToken);
    console.log("Usuario después de iniciar sesión:", decoded, "Rol:", userRole);
    
    setUser(decoded);
    setToken(accessToken);
    setRol(userRole); // ✅ Establecemos el rol en el estado
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('id_token');
    localStorage.removeItem('rol'); // ✅ Eliminamos el rol
    setUser(null);
    setToken(null);
    setRol(null);
  };

  return (
    <AuthContext.Provider value={{ user, token, rol, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
