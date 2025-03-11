import { useContext, useEffect } from 'react';
import { AuthContext } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import { Container, Card, Button, Row, Col } from 'react-bootstrap';
import { FaUserCircle, FaSignOutAlt, FaUsers, FaBook, FaClipboardList } from "react-icons/fa";
import '../styles/Profile.css';

const Profile = () => {
  const { user, rol, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  useEffect(() => {
    if (!user) {
      navigate('/login');
    }
  }, [user, navigate]);

  if (!user) {
    return <h2 className="text-center mt-5">Cargando...</h2>;
  }

  const name = user?.name || user?.nickname || "Usuario";
  const email = user?.email || "No disponible";
  const isAdmin = rol === "ADMIN";

  return (
    <Container className="profile-container">
      <Card className="profile-card">
        <Card.Body>
          <div className="profile-header">
            <FaUserCircle className={`profile-icon ${isAdmin ? "admin-icon" : "user-icon"}`} />
            <h2 className="welcome-text">
              {isAdmin ? `¡Bienvenido, Administrador ${name}!` : `¡Hola, ${name}!`}
            </h2>
            <p className="role-text">
              {isAdmin ? "Tienes acceso total al sistema" : "Explora y gestiona tus colecciones"}
            </p>
          </div>

          <p className="profile-info"><strong>Email:</strong> {email}</p>
          <p className="profile-info"><strong>Rol:</strong> {isAdmin ? 'ADMINISTRADOR' : 'USUARIO'}</p>

          <Row className="profile-buttons">
            <Col md={6}>
              <Button variant="danger" className="logout-button" onClick={() => { logout(); navigate('/'); }}>
                <FaSignOutAlt /> Cerrar Sesión
              </Button>
            </Col>

            {!isAdmin && (
              <Col md={6}>
                <Button variant="primary" className="action-button" onClick={() => navigate('/colecciones-user')}>
                  <FaBook /> Ver Colecciones
                </Button>
              </Col>
            )}
          </Row>

          {isAdmin && (
            <div className="admin-buttons">
              <Button variant="info" className="admin-button" onClick={() => navigate('/admin/usuarios')}>
                <FaUsers /> Administrar Usuarios
              </Button>
              <Button variant="success" className="admin-button" onClick={() => navigate('/admin/colecciones')}>
                <FaBook /> Administrar Colecciones
              </Button>
              <Button variant="warning" className="admin-button" onClick={() => navigate('/admin/pedidos')}>
                <FaClipboardList /> Pedidos
              </Button>
            </div>
          )}
        </Card.Body>
      </Card>
    </Container>
  );
};

export default Profile;
