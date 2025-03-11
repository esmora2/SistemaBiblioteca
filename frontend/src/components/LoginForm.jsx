import { useState, useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import apiAuth from '../api/apiAuth';
import { Form, Button, Container, Row, Col, Alert } from 'react-bootstrap';
import '../styles/Login.css'; // ✅ Importar los estilos correctos

const LoginForm = () => {
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const [showError, setShowError] = useState(false); // ✅ Control de visibilidad del error

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await apiAuth.post('public/auth/login', { json: formData }).json();

      console.log("Respuesta del backend:", response); // ✅ Verificar datos del backend

      login(response.accessToken, response.idToken, response.rol); // ✅ Guardar el rol en el contexto
      navigate('/profile');
    } catch (error) {
      console.error('Error en login:', error);
      setError('Credenciales incorrectas');
      setShowError(true);
      setTimeout(() => setShowError(false), 5000); // ✅ Ocultar error después de 5 segundos
    }
  };

  return (
    <Container className="login-container animate__animated animate__fadeIn">
      <Row className="justify-content-center">
        <Col md={6}>
          <div className="form-box">
            <h2 className="text-center">Iniciar Sesión</h2>

            {/* ✅ Mostrar error si hay credenciales incorrectas */}
            {showError && (
              <Alert variant="danger" dismissible onClose={() => setShowError(false)}>
                {error}
              </Alert>
            )}

            <Form onSubmit={handleSubmit}>
              <Form.Group className="mb-3">
                <Form.Label>Email</Form.Label>
                <Form.Control 
                  type="email" 
                  name="email" 
                  value={formData.email} 
                  onChange={handleChange} 
                  required 
                  placeholder="correo@ejemplo.com"
                />
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Contraseña</Form.Label>
                <Form.Control 
                  type="password" 
                  name="password" 
                  value={formData.password} 
                  onChange={handleChange} 
                  required 
                  placeholder="********"
                />
              </Form.Group>

              <Button variant="primary" type="submit" className="w-100 login-button">
                Iniciar Sesión
              </Button>
            </Form>
          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default LoginForm;
