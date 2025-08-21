
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

![Forks](https://img.shields.io/github/forks/maryycarrera/ComparteTuTiempo?style=social)
![Stars](https://img.shields.io/github/stars/maryycarrera/ComparteTuTiempo?style=social)

## Índice

- [Qué es ComparteTuTiempo](#qué-es-compartetutiempo)
- [Ejecutar el proyecto](#ejecutar-el-proyecto)
- [Utilizar ComparteTuTiempo](#utilizar-compartetutiempo)
- [Contribuciones](#contribuciones)

---

## Qué es ComparteTuTiempo

ComparteTuTiempo es una aplicación web de Banco de Tiempo desarrollada como Trabajo de Fin de Grado de Ingeniería del Software en la Universidad de Sevilla. Está pensada como una aplicación desarrollada para un banco de tiempo genérico; es decir, no es un proyecto solicitado por ningún banco de tiempo real.

---

## Ejecutar el proyecto

### Backend (Spring Boot)

1. **Configura la base de datos MariaDB/MySQL:**
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

2. **Instala las dependencias y ejecuta el backend:**
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

La aplicación estará disponible en http://localhost:4200.

## Utilizar ComparteTuTiempo

Las credenciales de los usuarios de ComparteTuTiempo son las siguientes:

| usuario           | contraseña |
| - | - |
| **Administrador** |
| admin1            | sys4dm1n*! |
| admin2            | sys4dm1n*! |
| admin3            | sys4dm1n*! |
| admin4            | sys4dm1n*! |
| **Miembro**       |
| maryycarrera      | m13mbr0CTT |

Contraseñas encriptadas utilizando Node.js:
```
cd frontend
node -e "console.log(require('bcryptjs').hashSync('tu_contraseña', 10))"
```

---

## Contribuciones

Para comenzar a contribuir en este repositorio, se debe hacer un fork del mismo. Cualquier aportación debe hacerse por medio de Pull Requests.

Gracias a nuestros contribuidores:

<a href="https://github.com/maryycarrera/ComparteTuTiempo/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=maryycarrera/ComparteTuTiempo" />
</a>
