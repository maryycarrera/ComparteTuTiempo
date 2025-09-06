# Etapa 1: Construcción
FROM node:22 AS build
WORKDIR /app
COPY frontend/ .
RUN npm install && npm run build -- --configuration=production

# Etapa 2: Servir archivos estáticos
FROM node:22-alpine AS serve
WORKDIR /app
RUN npm install -g http-server
COPY --from=build /app/dist/frontend/ ./dist
EXPOSE 8081
CMD ["http-server", "./dist", "-p", "8081"]
