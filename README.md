# Legacy Validation Service (Java)

## Descripción General

Este proyecto implementa un servicio legacy simulado desarrollado en **Java 1.8 (Java 8)** con **Spring Boot**, cuyo objetivo es representar un proveedor externo inestable, lento y no confiable.

---

## Stack Tecnológico

- Java 1.8 (Java 8)  
- Spring Boot  
- Spring Web  

---

## Cómo configurar y correr el proyecto

### Requisitos

- Java 1.8  
- Maven  
- Puerto 8080 disponible  

### Verificar versión de Java

```bash
java -version
```

Salida esperada:

```text
java version "1.8.0_xxx"
```

### Ejecutar el proyecto

```bash
mvn clean spring-boot:run
```

Al iniciar correctamente, el servicio queda disponible en:

```
http://localhost:8080
```

---

## Documentación API (Swagger)

El proyecto incluye **Swagger UI** para documentar y probar el endpoint del servicio legacy.

### Acceso a Swagger

Con el proyecto corriendo, Swagger está disponible en:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Endpoint Disponible

**POST /legacy/validate**

### Ejemplos de Respuesta

**Éxito**

```json
{
  "status": "OK",
  "message": "Legacy validation OK"
}
```

**Éxito lento**

```json
{
  "status": "OK",
  "message": "Legacy validation OK (slow)"
}
```

**Error**

```json
{
  "status": "ERROR",
  "message": "Legacy system error"
}
```

---

## Logging

Ejemplos de logs:

```text
INFO  Legacy validation request received
WARN  Legacy system responding slowly
ERROR Legacy system internal error
INFO  Legacy validation OK
```

---

## Problemas Encontrados

### 1. Incompatibilidad de formato de respuesta

**Problema:**  
Inicialmente el servicio devolvía texto plano, causando errores de deserialización en el cliente .NET.

**Investigación:**  
Revisión de logs del consumidor (JsonException).

**Decisión / Solución:**  
Unificar el contrato para devolver siempre JSON.

---

### 2. Timeouts en el cliente consumidor

**Problema:**  
El servicio legacy puede responder más lento que el timeout configurado.

**Investigación:**  
Se observaron `TaskCanceledException` en el API consumidor.

**Decisión:**  
Mantener el timeout como mecanismo de protección y documentarlo como riesgo conocido.

---

## Decisiones Técnicas

- Respuestas en JSON para facilitar integración moderna  
- Swagger habilitado para facilitar soporte y pruebas  
- Sin autenticación ni persistencia para mantener foco en integración  

---

## Riesgos Conocidos

- Puede exceder el timeout del cliente   
- No hay retries internos  
- No existe trazabilidad distribuida  
