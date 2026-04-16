# RetoFinal-Grupo1

# Proyecto: Sistema de Gestión Comercial Okapi 🦙
📝 Descripción

El Sistema de Gestión Comercial Okapi es una aplicación de escritorio desarrollada en Java cuyo objetivo es digitalizar y optimizar la gestión interna de una tienda de ropa local que actualmente opera mediante procesos manuales.

En un contexto de creciente competitividad en el comercio de proximidad, este sistema permite centralizar la información de clientes, trabajadores, productos y compras en una base de datos relacional MySQL, facilitando la realización de operaciones CRUD, el control de ventas y la gestión eficiente de la actividad comercial.

Además, proporciona una base tecnológica sólida para futuras ampliaciones, como la integración web o la generación de informes dinámicos mediante XML y XSL, contribuyendo así a mejorar la eficiencia operativa y la calidad del servicio.

---

Tecnologías utilizadas

- Java (compatible con JDBC y MySQL Connector en classpath)
- 🗄️ MySQL
- 🔌 JDBC (conector MySQL)
- AWT / Swing

Herramientas de desarrollo:

- Eclipse
- MySQL Workbench
- GitHub

---

## 📚 Dependencias

Este proyecto utiliza las siguientes librerías externas:

- Conector JDBC MySQL  
  (ej: mysql-connector-java-x.x.x.jar)

  En este caso cualquier conector sirve con tal de que sea "mysql-connector-java-5.x.x.jar" o superior.

⚠️ Otras dependencias necesarias (si aplica)

Asegúrate de añadir los archivos .jar al proyecto correctamente:

- En Eclipse:
  Project → Properties → Java Build Path → Libraries → Add External JARs

---

## 🗄️ Base de datos

El proyecto incluye el script de base de datos:

- Okapi.sql

▶️ Cómo usarlo:

1. Crear una base de datos en MySQL
2. Importar el fichero Okapi.sql

- Importar script SQL y ejecutarlo

---

## 📦 Instalación

📥 Clonar el repositorio:

https://github.com/usuario/repositorio.git](https://github.com/EkaitzGarcia/RetoFinal-Grupo1.git

📁 Importar el proyecto en:

- Eclipse

➕ Añadir dependencias:

1. Descargar MySQL Connector/J
2. Añadir el .jar al proyecto

---

## ▶️ Ejecución

1. Configurar conexión a la base de datos:
   - URL
   - Usuario
   - Contraseña/Token

2. Ejecutar la clase principal (main)

3. Usar la aplicación:

- Cliente
- Trabajador
- Administrador
- Gestión de productos

---

## ⚙️ Funcionalidades principales

- Gestión de clientes (CRUD)
- Gestión de trabajadores
- Gestión de productos
- Gestión de compras
- Control de ventas
- Cálculo automático de importes
- Exportación de datos a XML

---

## 👨‍💻 Autoría

Proyecto desarrollado por Grupo 1 como parte del módulo de Desarrollo de Aplicaciones Multiplataforma (DAM).
