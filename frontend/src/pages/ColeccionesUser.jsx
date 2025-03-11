import { useState, useEffect, useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import "../styles/Colecciones.css"; // ✅ Importar estilos personalizados

const ColeccionesUser = () => {
  const { token, user } = useContext(AuthContext);
  const [colecciones, setColecciones] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false); // ✅ Nuevo estado de carga
  const navigate = useNavigate();

  // 🔹 Función para obtener las colecciones
  const fetchColecciones = async () => {
    setLoading(true); // ✅ Mostrar indicador de carga
    try {
      const response = await fetch("http://172.191.132.105:9092/colecciones/user", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        throw new Error(`Error en la petición: ${response.statusText}`);
      }

      const data = await response.json();
      setColecciones(data);
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false); // ✅ Ocultar indicador de carga
    }
  };

  // 🔹 Cargar colecciones y actualizar automáticamente cada 5 segundos
  useEffect(() => {
    if (!token) {
      navigate("/login");
      return;
    }

    fetchColecciones(); // ✅ Primera carga

    const interval = setInterval(fetchColecciones, 5000); // ✅ Actualizar cada 5 segundos

    return () => clearInterval(interval); // ✅ Limpiar el interval al desmontar
  }, [token, navigate]);

  // 🔹 Función para solicitar un préstamo
  const solicitarPrestamo = async (coleccionId) => {
    if (!user || !user.sub) {
      alert("Usuario no identificado");
      return;
    }

    console.log("📌 Usuario autenticado:", user);
    console.log("📌 Enviando usuarioId al backend:", user.sub);

    try {
      const response = await fetch("http://172.191.132.105:9093/prestamos/user", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          usuarioId: user.sub,
          coleccionId,
        }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        console.error("Error en la respuesta:", errorData);
        throw new Error("Error al solicitar el préstamo");
      }

      alert("Préstamo solicitado con éxito");
      fetchColecciones(); // ✅ Actualizar lista después de solicitar préstamo
    } catch (error) {
      alert(error.message);
    }
  };

  return (
    <div>
      <button onClick={() => navigate(-1)} style={{ marginBottom: "20px" }}>
        🔙 Regresar
      </button>

      <button onClick={() => navigate("/historial")} style={{ marginBottom: "20px" }}>
        📜 Ver Historial de Préstamos
      </button>

      <h1>Colecciones Disponibles</h1>
      {error && <p style={{ color: "red" }}>⚠️ {error}</p>}

      {loading ? (
        <p>🔄 Cargando colecciones...</p> // ✅ Indicador de carga
      ) : colecciones.length === 0 ? (
        <p>No existen colecciones</p>
      ) : (
        <table border="1">
          <thead>
            <tr>
              <th>Título</th>
              <th>Autor</th>
              <th>Tipo y Categoría</th>
              <th>Descripción</th>
              <th>Imagen y URL Recurso</th>
              <th>Stock Disponible</th>
              <th>Acción</th>
            </tr>
          </thead>
          <tbody>
            {colecciones.map((coleccion) => (
              <tr key={coleccion.id}>
                <td>{coleccion.titulo}</td>
                <td>{coleccion.autor || "No disponible"}</td>
                <td>{`${coleccion.tipo} - ${coleccion.categoria}`}</td>
                <td>{coleccion.descripcion}</td>
                <td>
                  <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
                    {coleccion.imagenPortada ? (
                      <img
                        src={localStorage.getItem(coleccion.imagenPortada) || coleccion.imagenPortada}
                        alt="Portada"
                        width="100"
                        onError={(e) => (e.target.src = "/uploads/default.png")}
                      />
                    ) : (
                      "No disponible"
                    )}
                    {coleccion.urlRecurso && (
                      <a href={coleccion.urlRecurso} target="_blank" rel="noopener noreferrer">
                        📖 Ver recurso
                      </a>
                    )}
                  </div>
                </td>
                <td>{coleccion.cantidadDisponible}</td>
                <td>
                  {coleccion.cantidadDisponible > 0 ? (
                    <button onClick={() => solicitarPrestamo(coleccion.id)}>
                      📚 Solicitar Préstamo
                    </button>
                  ) : (
                    <span style={{ color: "red" }}>No disponible</span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default ColeccionesUser;
