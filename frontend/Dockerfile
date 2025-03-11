# Usa una imagen de Node.js ligera
FROM node:18-alpine

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos del proyecto al contenedor
COPY package.json package-lock.json ./

# Instala dependencias sin modificar package-lock.json
RUN npm install --frozen-lockfile

# Copia el resto de los archivos
COPY . .

# Configurar Vite para que escuche en 0.0.0.0 y en el puerto 5173
ENV VITE_PORT=5173
ENV HOST=0.0.0.0

# Expone el puerto 5173
EXPOSE 5173

# Comando por defecto para iniciar Vite en modo desarrollo
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0", "--port", "5173"]
