import { useState, useEffect, useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import '../styles/AdminColecciones.css';

const AdminColecciones = () => {
  const { token } = useContext(AuthContext);
  const [colecciones, setColecciones] = useState([]);
  const [error, setError] = useState(null);
  const [selectedColeccion, setSelectedColeccion] = useState(null);
  const [formData, setFormData] = useState({
    titulo: "",
    tipo: "",
    autor: "",
    categoria: "",
    descripcion: "",
    urlRecurso: "",
    imagenPortada: "",
    cantidadDisponible: 1,
  });
  const [imagenFile, setImagenFile] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (!token) {
      navigate("/login");
      return;
    }
    fetchColecciones();
  }, [token, navigate]);

  const fetchColecciones = () => {
    fetch("http://172.191.132.105:9092/colecciones/public")
      .then((response) => response.json())
      .then((data) => setColecciones(data))
      .catch(() => toast.error("Error al cargar colecciones"));
  };

  const handleInputChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleFileChange = (e) => setImagenFile(e.target.files[0]);

  const saveImageLocally = async () => {
    if (!imagenFile) return formData.imagenPortada || "/uploads/default.png";

    const fileName = `${Date.now()}_${imagenFile.name}`;
    const filePath = `/uploads/${fileName}`;

    try {
      const reader = new FileReader();
      reader.readAsDataURL(imagenFile);
      reader.onloadend = () => {
        localStorage.setItem(filePath, reader.result);
      };
      return filePath;
    } catch (error) {
      toast.error("Error al guardar la imagen");
      return "/uploads/default.png";
    }
  };

  const handleCreateOrUpdate = async () => {
    if (formData.cantidadDisponible < 0) {
      toast.warning("La cantidad disponible no puede ser negativa.");
      return;
    }

    const imageUrl = await saveImageLocally();
    const newColeccion = { ...formData, imagenPortada: imageUrl };

    const url = selectedColeccion
      ? `http://172.191.132.105:9092/colecciones/admin/${selectedColeccion.id}`
      : "http://172.191.132.105:9092/colecciones/admin";
    const method = selectedColeccion ? "PUT" : "POST";

    fetch(url, {
      method,
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(newColeccion),
    })
      .then((response) => response.json())
      .then(() => {
        toast.success(selectedColeccion ? "Colección actualizada con éxito" : "Colección creada con éxito");
        resetForm();
        fetchColecciones();
      })
      .catch(() => toast.error("Error al guardar la colección"));
  };

  const handleDelete = async (id) => {
    toast.info(
      <div>
        <p>¿Estás seguro de que deseas eliminar esta colección?</p>
        <button onClick={() => eliminarColeccion(id)} className="btn btn-danger btn-sm">Sí, eliminar</button>
      </div>,
      { autoClose: false }
    );
  };

  const eliminarColeccion = async (id) => {
    try {
      await fetch(`http://172.191.132.105:9092/colecciones/admin/${id}`, {
        method: "DELETE",
        headers: { "Authorization": `Bearer ${token}` },
      });
      setColecciones(colecciones.filter((coleccion) => coleccion.id !== id));
      toast.success("Colección eliminada correctamente");
    } catch (error) {
      toast.error("Error al eliminar la colección");
    }
  };

  const handleEdit = (coleccion) => {
    setSelectedColeccion(coleccion);
    setFormData(coleccion);
  };

  const resetForm = () => {
    setSelectedColeccion(null);
    setFormData({
      titulo: "",
      tipo: "",
      autor: "",
      categoria: "",
      descripcion: "",
      urlRecurso: "",
      imagenPortada: "",
      cantidadDisponible: 1,
    });
    setImagenFile(null);
  };

  return (
    <div>
      {/* 🔹 Botón "Regresar" */}
      <button onClick={() => navigate(-1)} style={{ marginBottom: "20px" }}>
        Regresar
      </button>

      <h1>Administrar Colecciones</h1>
      {error && <p style={{ color: "red" }}>Error: {error}</p>}

      <h2>{selectedColeccion ? "Editar Colección" : "Agregar Nueva Colección"}</h2>
      <input type="text" name="titulo" placeholder="Título" value={formData.titulo} onChange={handleInputChange} />
      <input type="text" name="tipo" placeholder="Tipo (LIBRO, REVISTA, RECURSO_ELECTRONICO)" value={formData.tipo} onChange={handleInputChange} />
      <input type="text" name="autor" placeholder="Autor" value={formData.autor} onChange={handleInputChange} />
      <input type="text" name="categoria" placeholder="Categoría" value={formData.categoria} onChange={handleInputChange} />
      <input type="text" name="descripcion" placeholder="Descripción" value={formData.descripcion} onChange={handleInputChange} />
      <input type="text" name="urlRecurso" placeholder="URL Recurso" value={formData.urlRecurso} onChange={handleInputChange} />
      <input type="number" name="cantidadDisponible" placeholder="Cantidad Disponible" value={formData.cantidadDisponible} onChange={handleInputChange} min="0" />

      <input type="file" name="imagenPortada" onChange={handleFileChange} />

      <button onClick={handleCreateOrUpdate}>
        {selectedColeccion ? "Actualizar Colección" : "Crear Colección"}
      </button>

      <h2>Lista de Colecciones</h2>
      <table border="1">
        <thead>
          <tr>
            <th>Título</th>
            <th>Autor</th>
            <th>Tipo y Categoría</th>
            <th>Descripción</th>
            <th>Cantidad Disponible</th>
            <th>Imagen y URL Recurso</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {colecciones.map((coleccion) => (
            <tr key={coleccion.id}>
              <td>{coleccion.titulo}</td>
              <td>{coleccion.autor || "No disponible"}</td>
              <td>{`${coleccion.tipo} - ${coleccion.categoria}`}</td>
              <td>{coleccion.descripcion}</td>
              <td>{coleccion.cantidadDisponible}</td>
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
                      Ver recurso
                    </a>
                  )}
                </div>
              </td>
              <td>
                <button onClick={() => handleEdit(coleccion)}>Editar</button>
                <button onClick={() => handleDelete(coleccion.id)}>Eliminar</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};


export default AdminColecciones;
