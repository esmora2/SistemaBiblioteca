import { Link } from "react-router-dom";

const NotFound = () => {
  return (
    <div>
      <h2>404 - Página no encontrada</h2>
      <p>La ruta que intentas acceder no existe.</p>
      <Link to="/">Volver a Inicio</Link>
    </div>
  );
};

export default NotFound;
