import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Container, Card, Button, Row, Col, Modal, Form, InputGroup, Badge } from "react-bootstrap";
import { FaUserEdit, FaTrashAlt, FaUserPlus, FaArrowLeft, FaSearch } from "react-icons/fa";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import apiUsers from "../api/apiUsers";
import "../styles/AdminUsuarios.css";

const AdminUsuarios = () => {
  const [usuarios, setUsuarios] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedUsuario, setSelectedUsuario] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [search, setSearch] = useState("");
  const [formData, setFormData] = useState({ nombre: "", email: "", password: "", rol: "USER" });

  const navigate = useNavigate();

  useEffect(() => {
    fetchUsuarios();
  }, []);

  const fetchUsuarios = async () => {
    try {
      const response = await apiUsers.get("admin/usuarios").json();
      setUsuarios(response);
    } catch (error) {
      toast.error("Error al cargar usuarios");
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleCreateOrUpdate = async () => {
    const url = selectedUsuario ? `admin/usuarios/${selectedUsuario.id}` : "admin/usuarios";
    const method = selectedUsuario ? "PUT" : "POST";

    try {
      await apiUsers(url, { method, json: formData });
      setShowModal(false);
      fetchUsuarios();
      toast.success(selectedUsuario ? "Usuario actualizado" : "Usuario creado");
    } catch (error) {
      toast.error("Error al guardar usuario");
    }
  };

  const handleEdit = (usuario) => {
    setSelectedUsuario(usuario);
    setFormData({ nombre: usuario.nombre, email: usuario.email, password: "", rol: usuario.rol });
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    toast.info(
      <div>
        <p>¿Eliminar este usuario?</p>
        <Button variant="danger" size="sm" onClick={() => eliminarUsuario(id)}>Sí, eliminar</Button>
      </div>,
      { autoClose: false }
    );
  };

  const eliminarUsuario = async (id) => {
    try {
      await apiUsers.delete(`admin/usuarios/${id}`);
      setUsuarios(usuarios.filter((usuario) => usuario.id !== id));
      toast.success("Usuario eliminado correctamente");
    } catch (error) {
      toast.error("Error al eliminar usuario");
    }
  };

  const resetForm = () => {
    setSelectedUsuario(null);
    setFormData({ nombre: "", email: "", password: "", rol: "USER" });
    setShowModal(true);
  };

  if (loading) return <h2 className="text-center mt-5">Cargando usuarios...</h2>;

  return (
    <Container className="admin-users__container">
      <Button variant="secondary" className="admin-users__back-button" onClick={() => navigate("/profile")}>
        <FaArrowLeft /> Volver al Perfil
      </Button>

      <h2 className="text-center">Gestión de Usuarios</h2>

      {/* 🔹 Barra de búsqueda */}
      <InputGroup className="admin-users__search-bar">
        <InputGroup.Text><FaSearch /></InputGroup.Text>
        <Form.Control type="text" placeholder="Buscar usuario..." value={search} onChange={(e) => setSearch(e.target.value)} />
      </InputGroup>

      <Button variant="admin-users__edit-button" className="admin-users__add-button" onClick={resetForm}>
        <FaUserPlus /> Agregar Usuario
      </Button>

      <Row className="g-4">
        {usuarios.filter((usuario) => usuario.nombre.toLowerCase().includes(search.toLowerCase())).map((usuario) => (
          <Col md={4} key={usuario.id}>
            <Card className="admin-users__user-card">
              <Card.Body>
                <Card.Title>{usuario.nombre}</Card.Title>
                <Card.Text>
                  <strong>Email:</strong> {usuario.email} <br />
                  <Badge pill bg={usuario.rol === "ADMIN" ? "danger" : "primary"}>
                    {usuario.rol}
                  </Badge>
                </Card.Text>
                <div className="admin-users__card-buttons">
                  <Button variant="admin-users__edit-button" onClick={() => handleEdit(usuario)}>
                    <FaUserEdit /> Editar
                  </Button>
                  <Button variant="admin-users__delete-button" onClick={() => handleDelete(usuario.id)}>
                    <FaTrashAlt /> Eliminar
                  </Button>
                </div>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>

      {/* 🔹 Modal para Crear/Editar Usuario */}
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>{selectedUsuario ? "Editar Usuario" : "Agregar Usuario"}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group>
              <Form.Label>Nombre</Form.Label>
              <Form.Control type="text" name="nombre" value={formData.nombre} onChange={handleInputChange} required />
            </Form.Group>
            <Form.Group>
              <Form.Label>Email</Form.Label>
              <Form.Control type="email" name="email" value={formData.email} onChange={handleInputChange} required />
            </Form.Group>
            <Form.Group>
              <Form.Label>Contraseña</Form.Label>
              <Form.Control type="password" name="password" value={formData.password} onChange={handleInputChange} />
            </Form.Group>
            <Form.Group>
              <Form.Label>Rol</Form.Label>
              <Form.Select name="rol" value={formData.rol} onChange={handleInputChange}>
                <option value="USER">Usuario</option>
                <option value="ADMIN">Administrador</option>
              </Form.Select>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="danger" onClick={() => setShowModal(false)}>Cancelar</Button>
          <Button variant="primary" onClick={handleCreateOrUpdate}>
            {selectedUsuario ? "Actualizar" : "Crear"}
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
};

export default AdminUsuarios;
