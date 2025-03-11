import { useState } from 'react';
import { Form, Button, Container, Row, Col, Alert } from 'react-bootstrap';
import { toast } from 'react-toastify';
import 'animate.css';
import '../styles/Signup.css';
import api from '../api/api';

const SignupForm = () => {
  const [formData, setFormData] = useState({
    nombre: '',
    email: '',
    password: '',
    confirmPassword: '',
  });

  const [errors, setErrors] = useState({
    nombre: '',
    email: '',
    password: '',
    confirmPassword: '',
  });

  const [serverError, setServerError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  const validateInput = (name, value) => {
    let error = '';

    switch (name) {
      case 'nombre':
        if (!/^[A-Za-zÁÉÍÓÚáéíóúñÑ ]{3,}$/.test(value)) {
          error = 'El nombre debe tener al menos 3 caracteres y solo contener letras.';
        }
        break;

      case 'email':
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
          error = 'Ingrese un correo válido.';
        }
        break;

      case 'password':
        if (!/(?=.*\d)(?=.*[A-Z])(?=.*\W).{8,}/.test(value)) {
          error = 'La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un símbolo.';
        }
        break;

      case 'confirmPassword':
        if (value !== formData.password) {
          error = 'Las contraseñas no coinciden.';
        }
        break;

      default:
        break;
    }

    setErrors({ ...errors, [name]: error });
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });

    validateInput(name, value); // ✅ Validar mientras se escribe
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // ✅ Revisar si hay errores antes de enviar el formulario
    for (let key in formData) {
      validateInput(key, formData[key]);
    }

    if (Object.values(errors).some((error) => error !== '')) {
      return;
    }

    try {
      const { confirmPassword, ...dataToSend } = formData;
      const response = await api.post('signup', { json: dataToSend }).json();

      setSuccessMessage(response.message);
      setServerError('');

      toast.success('¡Registro exitoso!', { position: "top-center", autoClose: 3000 });

      setFormData({ nombre: '', email: '', password: '', confirmPassword: '' });
      setTimeout(() => setSuccessMessage(''), 5000); // ✅ Ocultar mensaje después de 5 segundos
    } catch (error) {
      console.error('Error en la solicitud:', error);

      if (error.response) {
        try {
          const errorData = await error.response.json();
          setServerError(errorData.error || 'Error desconocido en el servidor');
        } catch {
          setServerError('Error en el servidor');
        }
      } else {
        setServerError('No se pudo conectar con el servidor');
      }
      setTimeout(() => setServerError(''), 5000); // ✅ Ocultar error después de 5 segundos
    }
  };

  return (
    <Container className="signup-container animate__animated animate__fadeIn">
      <Row className="justify-content-center">
        <Col md={8}>
          <div className="form-box">
            <h2 className="text-center">Registro de Usuario</h2>

            {/* ✅ Mostrar errores del backend */}
            {serverError && <Alert variant="danger">{serverError}</Alert>}

            {/* ✅ Mostrar mensaje de éxito */}
            {successMessage && <Alert variant="success">{successMessage}</Alert>}

            <Form onSubmit={handleSubmit}>
              <Form.Group className="mb-3">
                <Form.Label>Nombre</Form.Label>
                <Form.Control
                  type="text"
                  name="nombre"
                  value={formData.nombre}
                  onChange={handleChange}
                  required
                  placeholder="Ingresa tu nombre"
                  isInvalid={!!errors.nombre}
                />
                <Form.Control.Feedback type="invalid">{errors.nombre}</Form.Control.Feedback>
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Email</Form.Label>
                <Form.Control
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  required
                  placeholder="correo@ejemplo.com"
                  isInvalid={!!errors.email}
                />
                <Form.Control.Feedback type="invalid">{errors.email}</Form.Control.Feedback>
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Contraseña</Form.Label>
                <Form.Control
                  type="password"
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  required
                  placeholder="Al menos 8 caracteres, una mayúscula, un número y un símbolo."
                  isInvalid={!!errors.password}
                />
                <Form.Control.Feedback type="invalid">{errors.password}</Form.Control.Feedback>
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Confirmar Contraseña</Form.Label>
                <Form.Control
                  type="password"
                  name="confirmPassword"
                  value={formData.confirmPassword}
                  onChange={handleChange}
                  required
                  placeholder="Repite tu contraseña"
                  isInvalid={!!errors.confirmPassword}
                />
                <Form.Control.Feedback type="invalid">{errors.confirmPassword}</Form.Control.Feedback>
              </Form.Group>

              <Button variant="primary" type="submit" className="w-100 signup-button">
                Registrarse
              </Button>
            </Form>
          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default SignupForm;
