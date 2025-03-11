import { useState, useEffect, useContext } from "react";
import { AuthContext } from "../context/AuthContext"; 
import { useNavigate } from "react-router-dom";

const HistorialPrestamos = () => {
  const { token, user } = useContext(AuthContext);
  const [historial, setHistorial] = useState([]);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (!token || !user?.sub) {
      navigate("/login");
      return;
    }

    const fetchHistorial = async () => {
      try {
        const usuarioId = user.sub; // ✅ Usar el ID exacto que devuelve Auth0

        console.log("📌 usuarioId enviado al backend:", usuarioId);

        const response = await fetch(`http://172.191.132.105:9093/prestamos/user/${usuarioId}`, {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });

        if (!response.ok) {
          throw new Error(`Error en la respuesta: ${response.statusText}`);
        }

        const data = await response.json();
        console.log("📌 Respuesta del backend:", data);
        setHistorial(data);
      } catch (error) {
        console.error("❌ Error en fetchHistorial:", error);
        setError(error.message);
      }
    };

    fetchHistorial();
  }, [token, navigate, user]);

  // 🔹 Función para renovar un préstamo
  const renovarPrestamo = async (id) => {
    try {
      console.log(`📌 Renovando préstamo con ID: ${id}`);
      
      const response = await fetch(`http://172.191.132.105:9093/prestamos/user/renovar/${id}`, {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        throw new Error(`Error al renovar: ${response.statusText}`);
      }

      const data = await response.json();
      console.log("✅ Préstamo renovado:", data);

      // 🔄 Recargar historial después de renovar
      setHistorial((prevHistorial) =>
        prevHistorial.map((prestamo) =>
          prestamo.id === id ? { ...prestamo, fechaPrestamo: new Date().toISOString().split("T")[0] } : prestamo
        )
      );

    } catch (error) {
      console.error("❌ Error al renovar préstamo:", error);
      setError(error.message);
    }
  };

  return (
    <div>
      <button onClick={() => navigate(-1)} style={{ marginBottom: "20px" }}>
        🔙 Regresar
      </button>

      <h1>📜 Historial de Préstamos</h1>
      {error && <p style={{ color: "red" }}>⚠️ {error}</p>}

      {historial.length === 0 ? (
        <p>No tienes préstamos registrados.</p>
      ) : (
        <table border="1">
          <thead>
            <tr>
              <th>ID Préstamo</th>
              <th>Libro</th> 
              <th>Fecha Préstamo</th>
              <th>Fecha Devolución</th>
              <th>Estado</th>
              <th>Acciones</th> {/* ➕ Nueva columna */}
            </tr>
          </thead>
          <tbody>
            {historial.map((prestamo) => (
              <tr key={prestamo.id}>
                <td>{prestamo.id}</td>
                <td>{prestamo.tituloColeccion || "Título desconocido"}</td> 
                <td>{prestamo.fechaPrestamo}</td>
                <td>{prestamo.fechaDevolucion || "Pendiente"}</td>
                <td style={{ color: prestamo.devuelto ? "green" : "red" }}>
                  {prestamo.devuelto ? "Devuelto" : "Pendiente"}
                </td>
                <td>
                  {!prestamo.devuelto && ( // Solo mostrar si no está devuelto
                    <button onClick={() => renovarPrestamo(prestamo.id)}>🔄 Renovar</button>
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

export default HistorialPrestamos;
