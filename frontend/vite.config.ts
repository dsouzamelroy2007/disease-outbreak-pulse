import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

// The Spring Boot backend runs under context-path "/tracker" and serves this
// app's production build as static content from that same path, so the app
// is built (and served in dev) under "/tracker/" in both environments.
export default defineConfig({
  plugins: [react(), tailwindcss()],
  base: '/tracker/',
  server: {
    proxy: {
      '/tracker/api': 'http://localhost:8080',
    },
  },
  build: {
    outDir: '../src/main/resources/static',
    emptyOutDir: true,
  },
})
