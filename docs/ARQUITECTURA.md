# Arquitectura de SavePoint

## Flujo principal

```mermaid
flowchart TD
    ACT["MainActivity"] --> APP["SavePointApp"]
    ACT --> CONTAINER["AppContainer"]
    CONTAINER --> REPO["SavePointRepositoryImpl"]

    APP --> JUEGOVM["JuegoViewModel"]
    APP --> DIARIOVM["DiarioViewModel"]
    APP --> AJUSTESVM["AjustesViewModel"]

    JUEGOVM --> CONTRACT["SavePointRepository"]
    DIARIOVM --> CONTRACT
    AJUSTESVM --> CONTRACT
    CONTRACT --> REPO

    REPO --> API["FreeToGameApi · Retrofit"]
    REPO --> JDAO["JuegoDao"]
    REPO --> SDAO["SesionDao"]
    REPO --> ODAO["ObjetivoDao"]
    REPO --> DATASTORE["PreferenciasUsuarioDataStore"]

    CAMERA["CameraX"] --> FILE["Foto en filesDir/fotos_sesiones"]
    FILE --> REPO
    REPO --> SDAO
```

## Responsabilidades

| Capa | Responsabilidad | No debe conocer |
|---|---|---|
| `ui` | Renderizar estados, capturar acciones y navegar | Implementación de Room o Retrofit |
| `ui/viewmodel` | Coordinar casos de uso y exponer `StateFlow` | DAOs, DTO y clientes HTTP |
| `domain` | Modelos y contrato estable del repositorio | Android, Compose, Room y Retrofit |
| `data/repository` | Combinar orígenes remotos y locales | Componentes visuales |
| `data/remote` | Definir endpoints y DTO de FreeToGame | Estado de pantalla |
| `data/local` | Persistir biblioteca, sesiones, objetivos y ajustes | Navegación y Compose |

## Recorridos de datos

### Catálogo remoto

```mermaid
sequenceDiagram
    participant UI as PantallaExplorar
    participant VM as JuegoViewModel
    participant R as SavePointRepository
    participant API as FreeToGameApi
    UI->>VM: Reintentar o iniciar
    VM-->>UI: Loading
    VM->>R: obtenerCatalogo()
    R->>API: GET /games
    API-->>R: DTO JSON
    R-->>VM: List<Juego>
    VM-->>UI: Content o Error
```

### Registro de una sesión

```mermaid
sequenceDiagram
    participant UI as PantallaDiario
    participant CAM as CameraX
    participant VM as DiarioViewModel
    participant R as SavePointRepository
    participant DB as Room
    UI->>CAM: Foto opcional
    CAM-->>UI: file URI o cancelación
    UI->>VM: registrar(..., fotoUri?)
    VM->>R: registrarSesion(...)
    R->>DB: insertar sesión
    R->>DB: actualizar progreso
    DB-->>UI: Flow actualizado
```

## Persistencia local

- `JuegoGuardado`: snapshot mínimo para que Biblioteca funcione sin conexión.
- `SesionJuego`: duración, nota, progreso y URI local opcional.
- `ObjetivoJuego`: objetivos asociados mediante clave foránea.
- DataStore: acento global y orden de Biblioteca.

Eliminar un juego aplica `CASCADE` sobre sus sesiones y objetivos. Las fotografías se borran cuando se elimina explícitamente su sesión mediante el repositorio.
