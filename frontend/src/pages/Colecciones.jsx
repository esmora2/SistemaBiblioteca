import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Container, Row, Col, Card, Button, Alert } from "react-bootstrap";
import "../styles/Colecciones.css"; // ✅ Importar estilos personalizados

const Colecciones = () => {
  const [colecciones, setColecciones] = useState([]);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    fetch("http://172.191.132.105:9092/colecciones/public")
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Error en la petición: ${response.statusText}`);
        }
        return response.json();
      })
      .then((data) => setColecciones(data))
      .catch((error) => setError(error.message));
  }, []);

  return (
    <Container className="colecciones-container">
      <h1 className="text-center mb-4">Lista de Colecciones</h1>

      {error && <Alert variant="danger">{error}</Alert>}

      {colecciones.length === 0 ? (
        <p className="text-center">No hay colecciones disponibles</p>
      ) : (
        <Row className="g-4">
          {colecciones.map((coleccion) => (
            <Col md={4} key={coleccion.id}>
              <Card className="coleccion-card">
                <div className="image-container">
                  <Card.Img
                    variant="top"
                    src={localStorage.getItem(coleccion.imagenPortada) || coleccion.imagenPortada || "/uploads/default.png"}
                    onError={(e) => (e.target.src = "/uploads/default.png")}
                    className="coleccion-img"
                  />
                </div>
                <Card.Body>
                  <Card.Title className="coleccion-title">{coleccion.titulo}</Card.Title>
                  <Card.Subtitle className="coleccion-author">
                    {coleccion.autor || "Autor desconocido"}
                  </Card.Subtitle>
                  <Card.Text className="coleccion-text">
                    <span className="coleccion-info"><strong>Tipo:</strong> {coleccion.tipo}</span> <br />
                    <span className="coleccion-info"><strong>Categoría:</strong> {coleccion.categoria}</span> <br />
                    <span className="coleccion-desc">{coleccion.descripcion}</span>
                  </Card.Text>
                  {coleccion.urlRecurso && (
                    <Button
                      variant="primary"
                      href={coleccion.urlRecurso}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="coleccion-button"
                    >
                      Ver recurso
                    </Button>
                  )}
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      )}
    </Container>
  );
};

export default Colecciones;
