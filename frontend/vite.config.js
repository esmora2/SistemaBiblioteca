import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    host: '0.0.0.0',  // ✅ Permite acceso desde Docker
    port: 5173,       // ✅ Se asegura de usar el puerto correcto
    strictPort: true, // ✅ Falla si el puerto está ocupado en lugar de cambiar automáticamente
    watch: {
      usePolling: true // ✅ Evita problemas en entornos Docker con archivos
    },
    cors: true,       // ✅ Habilita CORS en modo desarrollo
  }
});
