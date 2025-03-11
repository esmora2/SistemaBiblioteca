import { BrowserRouter as Router, Routes, Route, Link, Navigate } from "react-router-dom";
import { useContext } from "react";
import { AuthContext, AuthProvider } from "./context/AuthContext";
import SignupForm from "./components/SignupForm";
import LoginForm from "./components/LoginForm";
import Profile from "./pages/Profile";
import AdminUsuarios from "./pages/AdminUsuarios";
import Colecciones from "./pages/Colecciones";
import ColeccionesUser from "./pages/ColeccionesUser";
import HistorialPrestamos from "./pages/HistorialPrestamos";
import NotFound from "./components/NotFound";
import AdminColecciones from "./pages/AdminColecciones";
import PedidosAdmin from "./pages/PedidosAdmin";

import { Container, Navbar, Nav, Button, Row, Col } from "react-bootstrap";
import { ToastContainer } from "react-toastify";
import "./App.css"; // Agregar estilos personalizados

const ProtectedRoute = ({ element, requiredRole }) => {
  const { user, rol } = useContext(AuthContext);

  if (!user) {
    return <Navigate to="/login" />;
  }

  if (requiredRole && rol !== requiredRole) {
    return <Navigate to="/profile" />;
  }

  return element;
};

function App() {
  return (
    <AuthProvider>
      <Router>
        {/* ✅ Barra de Navegación */}
        <Navbar bg="dark" variant="dark" expand="lg" className="mb-4">
          <Container>
            <Navbar.Brand as={Link} to="/">📚 Mi Biblioteca</Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
              <Nav className="ms-auto">
                <Nav.Link as={Link} to="/signup">Registrarse</Nav.Link>
                <Nav.Link as={Link} to="/login">Iniciar Sesión</Nav.Link>
                <Nav.Link as={Link} to="/colecciones">Colecciones</Nav.Link>
              </Nav>
            </Navbar.Collapse>
          </Container>
        </Navbar>

        {/* ✅ Contenido Principal */}
        <Container fluid className="p-0">
          <Routes>
            <Route
              path="/"
              element={
                <div>
                  {/* Hero Section */}
                  <section className="hero">
                    <Container className="text-center text-white">
                      <h1 className="hero-title animate__animated animate__fadeInDown">
                        Descubre el Conocimiento Ilimitado 📚
                      </h1>
                      <p className="hero-subtitle animate__animated animate__fadeInUp">
                        Explora miles de libros y administra tus préstamos de manera sencilla.
                      </p>
                      <Button as={Link} to="/colecciones" variant="light" size="lg" className="mt-3 animate__animated animate__zoomIn">
                        Explorar Colecciones
                      </Button>
                    </Container>
                  </section>

                  {/* Sección de Información */}
                  <section className="info-section py-5">
                    <Container>
                      <Row className="text-center">
                        <Col md={4} className="mb-4">
                          <h3>📖 Amplia Biblioteca</h3>
                          <p>Accede a una vasta colección de libros digitales y físicos.</p>
                        </Col>
                        <Col md={4} className="mb-4">
                          <h3>🚀 Préstamos Rápidos</h3>
                          <p>Gestiona tus libros con un sistema ágil y eficiente.</p>
                        </Col>
                        <Col md={4} className="mb-4">
                          <h3>🌍 Disponible en Línea</h3>
                          <p>Consulta y reserva libros desde cualquier lugar del mundo.</p>
                        </Col>
                      </Row>
                    </Container>
                  </section>
                </div>
              }
            />

            <Route path="/signup" element={<SignupForm />} />
            <Route path="/login" element={<LoginForm />} />
            <Route path="/profile" element={<ProtectedRoute element={<Profile />} />} />
            <Route path="/admin/usuarios" element={<ProtectedRoute element={<AdminUsuarios />} requiredRole="ADMIN" />} />
            <Route path="/colecciones" element={<Colecciones />} />
            <Route path="/colecciones-user" element={<ProtectedRoute element={<ColeccionesUser />} />} />
            <Route path="/admin/colecciones" element={<ProtectedRoute element={<AdminColecciones />} requiredRole="ADMIN" />} />
            <Route path="/admin/pedidos" element={<ProtectedRoute element={<PedidosAdmin />} requiredRole="ADMIN" />} />
            <Route path="/historial" element={<ProtectedRoute element={<HistorialPrestamos />} />} />
            <Route path="*" element={<NotFound />} />
          </Routes>
        </Container>

        {/* ✅ Footer */}
        <footer className="footer text-center text-white py-4">
          <Container>
            <p>&copy; 2024 Mi Biblioteca. Todos los derechos reservados.</p>
          </Container>
        </footer>

        {/* ✅ Contenedor de Notificaciones */}
        <ToastContainer />
      </Router>
    </AuthProvider>
  );
}

export default App;
