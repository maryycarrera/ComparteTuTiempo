
# ComparteTuTiempo

![Java](https://img.shields.io/badge/Java-21.0.8-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen?logo=springboot)
![Angular](https://img.shields.io/badge/Angular-20.1.5-red?logo=angular)
![Node.js](https://img.shields.io/badge/Node.js-22.18.0-brightgreen?logo=node.js)
![MariaDB](https://img.shields.io/badge/MariaDB-11.7.2-darkblue?logo=mariadb)
![Último commit](https://img.shields.io/github/last-commit/maryycarrera/ComparteTuTiempo)
![Issues abiertas](https://img.shields.io/github/issues/maryycarrera/ComparteTuTiempo)
![Pull Requests abiertas](https://img.shields.io/github/issues-pr/maryycarrera/ComparteTuTiempo)
![Licencia MIT](https://img.shields.io/github/license/maryycarrera/ComparteTuTiempo?label=license)
![Tamaño del repo](https://img.shields.io/github/repo-size/maryycarrera/ComparteTuTiempo)
![Estado de CI](https://img.shields.io/github/actions/workflow/status/maryycarrera/ComparteTuTiempo/commits.yml?branch=main)
![Release](https://img.shields.io/github/v/release/maryycarrera/ComparteTuTiempo)
[![Quality Gate Status (Backend)](https://sonarcloud.io/api/project_badges/measure?project=maryycarrera_ComparteTuTiempo&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=maryycarrera_ComparteTuTiempo)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=maryycarrera_ComparteTuTiempo-frontend&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=maryycarrera_ComparteTuTiempo-frontend)

![Forks](https://img.shields.io/github/forks/maryycarrera/ComparteTuTiempo?style=social)
![Stars](https://img.shields.io/github/stars/maryycarrera/ComparteTuTiempo?style=social)

## Índice

- [Qué es ComparteTuTiempo](#qué-es-compartetutiempo)
- [Descargar y ejecutar el proyecto para desarrollo](#descargar-y-ejecutar-el-proyecto-para-desarrollo)
- [Utilizar la aplicación ComparteTuTiempo en producción](#utilizar-la-aplicación-compartetutiempo-en-producción)
- [Credenciales de ComparteTuTiempo](#credenciales-de-compartetutiempo)
- [Análisis de calidad](#análisis-de-calidad)
- [Contribuciones](#contribuciones)

---

## Qué es ComparteTuTiempo

ComparteTuTiempo es una aplicación web de Banco de Tiempo desarrollada como Trabajo de Fin de Grado de Ingeniería del Software en la Universidad de Sevilla. Está pensada como una aplicación desarrollada para un banco de tiempo genérico; es decir, no es un proyecto solicitado por ningún banco de tiempo real.

---

## Descargar y ejecutar el proyecto para desarrollo

> [!WARNING]
> Necesitas tener instalados Java, Node.js y MariaDB en las versiones indicadas al principio de este archivo README. También puedes utilizar versiones superiores de estas tecnologías, pero es recomendable usar las mismas con las que se ha desarrollado este proyecto.

Primero, haz un _fork_ de este repositorio. Clona el _fork_ o descárgate el código fuente del mismo.

### Base de Datos (MariaDB)

Para configurar la base de datos **MariaDB/MySQL**:
- Abre una terminal y accede a MariaDB como root:
    ```sh
    mysql -u root -p
    ```
- Crea la base de datos:
    ```sql
    CREATE DATABASE compartetutiempo;
    ```
- Crea el usuario y asigna permisos:
    ```sql
    CREATE USER 'compartetutiempo'@'localhost' IDENTIFIED BY 'compartetutiempo';
    GRANT ALL PRIVILEGES ON compartetutiempo.* TO 'compartetutiempo'@'localhost';
    FLUSH PRIVILEGES;
    EXIT;
    ```
- Ajusta los valores en `src/main/resources/application.properties` si usas otros datos.

### Backend (Spring Boot)

Para instalar las dependencias y ejecutar el backend, en la misma o en otra terminal escribe:
```sh
.\mvnw spring-boot:run
```

El backend estará disponible en http://localhost:8080.

### Frontend (Angular)

En otra terminal:

1. **Instala las dependencias:**
    ```
    cd frontend
    npm install
    ```
2. **Ejecuta la aplicación:**
    ```
    npm start
    ```

El frontend de la aplicación estará disponible en http://localhost:4200.

---

## Utilizar la aplicación ComparteTuTiempo en producción

Puedes levantar toda la aplicación usando las imágenes públicas de Docker Hub y el archivo `docker-compose.prod.yml`.

### Requisitos previos

Antes de comenzar, asegúrate de tener instalado en tu sistema:
- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

> [!NOTE]
> Si instalas Docker Desktop, Docker Compose ya vendrá instalado por defecto. Asegúrate de que la _engine_ esté ejecutándose antes de continuar.

Una vez instalado Docker, puedes seguir estos pasos:
1. Descarga el archivo `docker-compose.prod.yml` de este repositorio.
2. Renómbralo a `docker-compose.yml` (opcional, para facilitar el uso de comandos estándar).
3. Ejecuta el siguiente comando en la raíz del proyecto (la ubicación donde hayas guardado el archivo `docker-compose.yml`):
    ```sh
    docker compose up -d
    ```

Esto descargará automáticamente las imágenes necesarias:
- Backend: `marycarrera/comparte-tu-tiempo-backend:latest`
- Frontend: `marycarrera/comparte-tu-tiempo-frontend:latest`
- Base de datos: `mariadb:11.7.2`

La aplicación estará disponible en:
- Backend: http://localhost:8080
- Frontend: http://localhost

Para detener el contenedor de la aplicación:
```sh
docker compose down
```

Si prefieres detener el contenedor eliminando también los volúmenes:
```sh
docker compose down -v
```

---

## Credenciales de ComparteTuTiempo

Las credenciales de los usuarios base de ComparteTuTiempo son las siguientes:

| usuario           | contraseña  |
| - | - |
| **Administrador** |
| admin1            | Sys4dm1n*!  |
| admin2            | Sys4dm1n*!  |
| admin3            | Sys4dm1n*!  |
| admin4            | Sys4dm1n*!  |
| **Miembro**       |
| member1           | m13mbr0CTT* |
| member2           | m13mbr0CTT* |
| member3           | m13mbr0CTT* |
| member4           | m13mbr0CTT* |
| member5           | m13mbr0CTT* |
| member6           | m13mbr0CTT* |
| member7           | m13mbr0CTT* |
| member8           | m13mbr0CTT* |
| member9           | m13mbr0CTT* |
| member10          | m13mbr0CTT* |
| maryycarrera      | m13mbr0CTT* |

Contraseñas encriptadas utilizando Node.js:
```
cd frontend
node -e "console.log(require('bcryptjs').hashSync('tu_contraseña', 10))"
```

---

## Análisis de calidad

Se ha realizado un análisis de calidad del código desarrollado con SonarQube. Para ello, se han analizado por separado el _backend_ y el _frontend_ del proyecto. Los resultados se pueden observar en los siguientes enlaces:

- [Análisis de calidad del _backend_](https://sonarcloud.io/project/overview?id=maryycarrera_ComparteTuTiempo)
- [Análisis de calidad del _frontend_](https://sonarcloud.io/project/overview?id=maryycarrera_ComparteTuTiempo-frontend)

---

## Contribuciones

Para comenzar a contribuir en este repositorio, se debe hacer un _fork_ del mismo e instalar las herramientas necesarias como se indica en el [apartado correspondiente](#descargar-y-ejecutar-el-proyecto-para-desarrollo). Cualquier aportación debe hacerse por medio de Pull Requests.

Gracias a nuestros contribuidores:

<a href="https://github.com/maryycarrera/ComparteTuTiempo/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=maryycarrera/ComparteTuTiempo" />
</a>

---
