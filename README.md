# Esteban Warinet - Backend Riu
Este repositorio contiene una API que registra búsquedas de disponibilidad hotelera y 
almacenandolas en una base de datos de forma asíncrona

## Arquitectura

La aplicación sigue una **arquitectura hexagonal (puertos y adaptadores)**, organizada
en tres paquetes con las dependencias apuntando siempre hacia el dominio:

```
src/main/java/com/estebanwarinet/challengebackendriu/
├── domain/            # Search, SearchId, excepciones
├── application/       # use cases + ports (in/out)
└── infrastructure/    # adapters: rest, kafka (producer/consumer), db
```

- domain: el núcleo del negocio. Contiene los objetos propios del negocio (`Search`, `SearchId`)
y las excepciones de dominio. Valida reglas de negocio (checkIn anterior a checkOut, edades no negativas) 
en el constructor, por lo que un objeto inválido no puede existir.
- application: los casos de uso (`CreateSearch`, `PersistSearch`, `GetSearchCount`) y los puertos que definen
los contratos de entrada y salida. Esta capa no depende del framework; los beans se definen desde infraestructura 
mediante una clase de configuración.
- infrastructure: los adaptadores que implementan los puertos: REST (controllers, DTOs, manejo de errores), 
Kafka (productor y consumidor) y persistencia (JPA/PostgreSQL).

## Diagrama de Flujo

```
POST /search ──► Controller (adapter in)
│ CreateSearchUseCase
▼
CreateSearchService ──► SearchEventPublisher (port out)
│                        │
▼                        ▼
devuelve SearchId      Kafka topic: hotel_availability_searches
│
▼
KafkaSearchConsumer (adapter in)
│ PersistSearchUseCase
▼
Postgres (JPA adapter out)
GET /count ──► Controller ──► GetSearchCountUseCase ──► misma DB
```

## Stack Tecnologico

- Java 21
- Spring Boot 4.1
- Maven
- Apache Kafka (KRaft)
- PostgreSQL
- Docker/docker-compose
- Jacoco
- springdoc-openapi

## Ejecución de la Aplicación

Aclaración: Necesario tener docker instalado

```bash
docker compose up --build
```

## Documentación

La aplicación posee una documentación swagger que puede accederse desde
URL: http://localhost:8080/swagger-ui.

### Endpoints

POST /search

Body
```json
{
    "hotelId": "erdd212",
    "checkIn": "27/12/2023",
    "checkOut": "31/12/2024",
    "ages": [
        30,
        29,
        1,
        3
    ]
}
```

Response
```json
{
    "searchId": "1deda0aa-7709-479c-ad97-d0f23cbe85a0"
}
```

Aclaración: La persistencia es asíncrona vía Kafka tras un POST puede haber un delay de ~1-2 segundos antes de que 
la búsqueda esté disponible en /count. Es comportamiento esperado del diseño event-driven.

GET /count

RequestParam: searchId={searchId}

Response
```json
{
    "searchId": "1deda0aa-7709-479c-ad97-d0f23cbe85a0",
    "search": {
        "hotelId": "erdd212",
        "checkIn": "27/12/2023",
        "checkOut": "31/12/2024",
        "ages": [
            30,
            29,
            1,
            3
        ]
    },
    "count": 4
}

```

### Respuestas de Error

| Caso | Status | Ejemplo de mensaje |
|---|---|---|
| `hotelId` nulo o vacío | 400 | `hotelId: no debe estar nulo` |
| `checkIn` o `checkOut` nulos | 400 | `checkIn: no debe estar nulo` |
| Fecha con formato incorrecto | 400 | `Body JSON inválido o fecha con formato incorrecto (dd/MM/yyyy)` |
| `checkIn` igual o posterior a `checkOut` | 400 | `checkIn debe ser anterior a checkOut` |
| `ages` nula o vacía | 400 | `ages: no debe estar vacía` |
| Algún valor de `ages` negativo | 400 | `Todas las edades deben ser mayores o iguales a 0` |
| JSON mal formado | 400 | `Body JSON inválido o fecha con formato incorrecto (dd/MM/yyyy)` |
| `searchId` inexistente en `GET /count` | 404 | `Búsqueda no encontrada: <searchId>` |
| Falta el parámetro `searchId` en `GET /count` | 400 | (error estándar de Spring: *Required parameter 'searchId' is not present*) |


## Test

Para ejecutar los test hay que ejecutar

```bash
./mvnw test
```

Aclaración: Esto se tiene que ejecutar por fuera del docker ya que usan
Testcontainers que necesitan de un docker socket que no esta disponible dentro del build de Docker

## Decisiones de diseño

- Arquitectura Hexagonal estricta sin utilizar notaciones del framework de spring dentro del dominio, 
utilizando un archivo de config para la inicialización de los beans.
- Validaciones de forma directamente dentro de los DTO, dejando reglas o validaciones de negocio en el dominio 
(en el constructor de Search), como ser que el checkIn tiene que ser antes que el checkout.
- Para todas las validaciones retornar 400 para validaciones, 
retornar 404 para recurso inexistente como ser cuando no existe un searchId en al base de datos.
- Virtual threads para el guardado (I/O-bound), basando en la documentación de spring esto se implementa con agregar
spring.threads.virtual.enabled en el application file, de esta forma Spring Boot ejecuta automáticamente los 
listeners de Kafka sobre virtual threads
- Utilizar una columna String llamada signature (hotelId|fechas|ages-en-orden) para poder calcular el count 
de búsquedas idénticas de esta forma se preserva el orden de las edades y queda simplificada la query sin tener 
que armar un conjunto de 'ands' o concatenar varios valores por consulta