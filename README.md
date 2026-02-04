# Inventory Config Service (Examen AE)

Microservicio en Kotlin + Spring Boot que administra reglas internas de inventario (**InventoryRule**) con:
- Arquitectura por capas (controller / service / repository)
- DTOs + mappers
- Manejo centralizado de errores con custom exceptions
- Seguridad con AWS Cognito (JWT Resource Server)
- Auditoría: `updatedBy` se llena desde el JWT

## Requisitos
- Java 21
- Gradle
- (DB) H2 en memoria (por defecto) o PostgreSQL (si se configura)

## Cómo ejecutar
### Opción 1: correr con H2 (default)
1. Configura la variable de entorno del issuer de Cognito:
    - `COGNITO_ISSUER_URI=https://cognito-idp.us-east-2.amazonaws.com/us-east-2_sokaxcWag`
2. Ejecuta:
    - `./gradlew bootRun`
3. El servicio levanta en:
    - `http://localhost:8081`

### Opción 2: PostgreSQL (si aplica)
Configura variables:
- `DB_URL=jdbc:postgresql://localhost:5432/inventorydb`
- `DB_USER=postgres`
- `DB_PASSWORD=postgres`
  y ejecuta igual `./gradlew bootRun`.

## Seguridad (Cognito)
El proyecto usa **Spring Security Resource Server con JWT**.

### Claim usado para auditoría (updatedBy)
- `updatedBy` se llena desde el claim: **sub**
- Si el JWT no contiene `sub`, la operación falla con una excepción controlada.

### Rol ADMIN
- Se obtiene desde el claim **cognito:groups**
- Los grupos se convierten a authorities `ROLE_<GRUPO_EN_MAYUS>`
    - Ejemplo: grupo `admin` -> `ROLE_ADMIN`

## Endpoints
### 1) Público (sin token)
- `GET /public/health`
    - Respuesta: estado + timestamp

### 2) Autenticado (requiere JWT válido)
- `GET /api/rules` (listar)
- `GET /api/rules/{id}` (obtener por id)
- `GET /api/rules/search?q=texto` (búsqueda por nombre)

### 3) Solo ADMIN (requiere rol ADMIN)
- `POST /api/rules` (crea y setea updatedBy)
- `PUT /api/rules/{id}` (actualiza y setea updatedBy)
- `PATCH /api/rules/{id}/toggle` (activa/desactiva y setea updatedBy)
- `DELETE /api/rules/{id}` (opcional)

## Cómo probar (Postman)
1. Obtén un `access_token` desde Cognito (OAuth2 en Postman).
2. Usa el token en:
    - Header: `Authorization: Bearer <access_token>`

Casos esperados:
- Sin token en endpoints `/api/**` -> 401
- Con token válido (usuario normal) en GET -> 200
- Con token usuario normal en POST/PUT/PATCH/DELETE -> 403
- Con token ADMIN en POST/PUT/PATCH/DELETE -> 201/200

## Evidencias de requests
Revisar el archivo:
- `requests.http`
