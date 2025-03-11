import { useEffect, useState, useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

const PedidosAdmin = () => {
  const { token, rol } = useContext(AuthContext);
  const [pedidos, setPedidos] = useState([]);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (!token || rol !== "ADMIN") {
      navigate("/login");
      return;
    }

    fetch("http://172.191.132.105:9093/prestamos/admin", {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Error en la petición: ${response.statusText}`);
        }
        return response.json();
      })
      .then((data) => setPedidos(data))
      .catch((error) => setError(error.message));
  }, [token, rol, navigate]);

  const marcarComoDevuelto = async (id) => {
    try {
      const response = await fetch(`http://172.191.132.105:9093/prestamos/admin/devolver/${id}`, {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        throw new Error(`Error al actualizar el estado: ${response.statusText}`);
      }

      setPedidos((prevPedidos) =>
        prevPedidos.map((p) =>
          p.id === id ? { ...p, devuelto: true, fechaDevolucion: new Date().toISOString().split("T")[0] } : p
        )
      );
    } catch (error) {
      console.error("❌ Error al marcar como devuelto:", error);
      setError(error.message);
    }
  };

  return (
    <div>
      <button onClick={() => navigate(-1)} style={{ marginBottom: "20px" }}>
        ⬅ Regresar
      </button>

      <h1>Historial de Préstamos (ADMIN)</h1>
      {error && <p style={{ color: "red" }}>Error: {error}</p>}

      {pedidos.length === 0 ? (
        <p>No hay préstamos registrados</p>
      ) : (
        <table border="1">
          <thead>
            <tr>
              <th>ID de Colección</th> {/* ✅ Nueva columna */}
              <th>Nombre de la Colección</th>
              <th>Fecha de Préstamo</th>
              <th>Fecha de Devolución</th>
              <th>Estado</th>
              <th>Acción</th> 
            </tr>
          </thead>
          <tbody>
            {pedidos.map((prestamo) => (
              <tr key={prestamo.id}>
                <td>{prestamo.coleccionId || "N/A"}</td> {/* ✅ Mostrar el ID de la colección */}
                <td>{prestamo.tituloColeccion || "Colección desconocida"}</td>
                <td>{prestamo.fechaPrestamo}</td>
                <td>{prestamo.fechaDevolucion || "No devuelto"}</td>
                <td style={{ color: prestamo.devuelto ? "green" : "red" }}>
                  {prestamo.devuelto ? "Devuelto" : "Pendiente"}
                </td>
                <td>
                  {!prestamo.devuelto && (
                    <button onClick={() => marcarComoDevuelto(prestamo.id)}>Marcar como Devuelto</button>
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

export default PedidosAdmin;
