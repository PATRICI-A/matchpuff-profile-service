# matchpuff-profile-service

## Gestión del perfil de usuario y flujo de onboarding

### Tecnologías utilizadas
- Java 17
- Spring Boot 3.2.5
- Maven
- JPA/Hibernate
- H2 Database (desarrollo)

### Descripción del módulo
Este microservicio gestiona la información del perfil de usuario y el flujo de onboarding, siguiendo principios de Clean Architecture.

### Funcionamiento del módulo
- Arquitectura basada en capas (Clean Architecture): dominio, aplicación, infraestructura y entrada.
- Utiliza DTOs, mapeadores y servicios para la lógica de negocio.
- Otros módulos pueden consumir sus endpoints REST para la gestión de perfiles.

### Diagramas
- Diagrama de clases: [pendiente de agregar]
- Diagrama de componentes: [pendiente de agregar]
- Diagrama de datos: [pendiente de agregar]

### Documentos

- Documento arquitectura del modulo: [Documento](https://pruebacorreoescuelaingeduco-my.sharepoint.com/:w:/g/personal/javier_romero-d_mail_escuelaing_edu_co/IQAKfAM2ZdJsRJ8kEBYsqeImAQPXeqsLfA1y-RIbHioZB2c?e=siijic)


### Funcionalidades
- Registro y actualización de perfil de usuario
- Gestión del flujo de onboarding
- Validación de datos y manejo de errores

### Endpoints expuestos
- Enpoints Futuros
	- Happy Path: [describir]
	- Manejo de Errores: [describir]

### Mensajería
- Pendiente por hablar

### Evidencia de pruebas
- 
### Ejecución del proyecto
1. Clonar el repositorio
2. Ejecutar `mvn clean install`
3. Ejecutar la aplicación con `mvn spring-boot:run` o desde la clase principal

### Evidencia de despliegue CI/CD
- 

### Organización del código
- El código está organizado en las siguientes carpetas:
	- `application`: Lógica de aplicación, DTOs, servicios, mapeadores
	- `domain`: Modelos de dominio, excepciones, puertos
	- `infrastructure`: Adaptadores, configuración, persistencia
	- `entrypoints`: Controladores REST, mapeadores, WebSocket

### Documentación del código
- Cada función, propiedad y clase debe tener comentarios de documentación.

### Conexiones con servicios externos
- Base de datos por hablar

### Pruebas y cobertura
- 

### Pipelines
- El repositorio debe tener dos pipelines: uno de desarrollo y otro de producción.

---
*En proceso :<.*

