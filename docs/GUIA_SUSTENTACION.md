# Guía de sustentación de SavePoint

Duración objetivo: 15 minutos.

## Preparación

- Tener el emulador o dispositivo encendido.
- Abrir el proyecto en Android Studio.
- Iniciar la app con Internet disponible.
- Tener al menos un juego guardado para demostrar Biblioteca y Diario.
- Haber probado previamente la cámara en un dispositivo físico si el emulador no ofrece una imagen útil.
- Mantener abierta esta guía y `docs/ARQUITECTURA.md`.

## 0:00–1:00 · Problema y solución

> SavePoint es un diario gamer personal. No intenta reemplazar una tienda ni una red social: ayuda a descubrir juegos gratuitos y a conservar localmente el progreso, las sesiones, las notas y los objetivos del usuario.

Mencionar:

- El catálogo remoto proviene de FreeToGame y no usa API Key.
- Los datos personales permanecen en el dispositivo.
- La aplicación es oscura y permite tres acentos globales.

## 1:00–4:00 · Demostración de interfaz

1. En **Explorar**, mostrar la carga del catálogo, la búsqueda y los filtros PC/Navegador.
2. Abrir un juego y enseñar descripción, capturas, requisitos y atribución.
3. Agregarlo a **Biblioteca**.
4. Cambiar el progreso y explicar los estados Pendiente, Jugando y Completado.
5. Abrir **Ajustes**, cambiar a verde o rojo y volver a otra pantalla para demostrar que el acento es global.
6. Reiniciar la app y confirmar que el color y el orden continúan.

## 4:00–6:00 · Retrofit y estados

Abrir:

- `data/remote/FreeToGameApi.kt`
- `data/remote/FreeToGameDto.kt`
- `ui/viewmodel/JuegoViewModel.kt`
- `ui/estado/LoadState.kt`

Explicar:

- `GET /games` obtiene el catálogo.
- `GET /game?id=...` obtiene el detalle.
- Los DTO se transforman a modelos de dominio mediante mappers.
- La UI no recibe excepciones directamente; observa `Loading`, `Content` o `Error`.
- El botón Reintentar vuelve a solicitar los datos.

## 6:00–9:00 · Room, DataStore y modo offline

Abrir:

- `data/local/Entidades.kt`
- `data/local/JuegoDao.kt`, `SesionDao.kt` y `ObjetivoDao.kt`
- `data/local/PreferenciasUsuarioDataStore.kt`

Explicar:

- Room guarda un snapshot del juego, sesiones y objetivos.
- Las claves foráneas eliminan sesiones y objetivos cuando se elimina su juego.
- Los DAOs exponen `Flow`; por eso la interfaz se actualiza automáticamente.
- Biblioteca y Diario no dependen de Internet después de guardar datos.
- DataStore persiste el acento y el orden de Biblioteca.

Demostración opcional: desactivar Internet, abrir Biblioteca y Diario y luego volver a Explorar para mostrar el error con reintento.

## 9:00–12:00 · MVVM, Repository y Clean Architecture

Usar el diagrama de `docs/ARQUITECTURA.md`.

Abrir:

- `domain/repository/SavePointRepository.kt`
- `data/repository/SavePointRepositoryImpl.kt`
- `di/AppContainer.kt`

Idea central:

> Los ViewModels sólo conocen el contrato `SavePointRepository`. La implementación decide si cada operación usa Retrofit, Room o DataStore. Esto evita acoplar la interfaz a detalles de infraestructura y facilita las pruebas con repositorios falsos.

Señalar que `AppContainer` realiza inyección manual; Hilt no fue necesario para el alcance del curso.

## 12:00–14:00 · CameraX y permisos

1. En Diario, crear una sesión.
2. Pulsar **Agregar foto**.
3. Explicar el permiso contextual de Cámara.
4. Tomar una fotografía o mostrar el flujo de rechazo.
5. Guardar la sesión.

Abrir:

- `ui/camara/CapturaCamara.kt`
- `ui/camara/PermisoCamara.kt`

Puntos importantes:

- La cámara es opcional.
- Rechazar el permiso nunca bloquea el guardado de la sesión.
- El rechazo permanente ofrece acceso a los Ajustes del sistema.
- La foto se guarda en `filesDir/fotos_sesiones`; no se solicita permiso de almacenamiento.
- Room sólo conserva la URI local asociada a la sesión.

## 14:00–15:00 · Pruebas y cierre

Mostrar la carpeta `app/src/test` y mencionar:

- Conversión JSON y error HTTP con MockWebServer.
- Mappers de DTO a dominio.
- Repository combinando fuentes.
- ViewModels en éxito, sin conexión y sesiones con/sin foto.
- Persistencia de ajustes, rutas de navegación y estados de permiso.

Cerrar con:

> SavePoint cumple el objetivo de demostrar el ciclo completo de una app Android moderna: UI declarativa, navegación, estado reactivo, persistencia, API REST, arquitectura por capas y acceso controlado a hardware.

## Plan alternativo para la demostración

- Si FreeToGame no responde: mostrar el estado Error, Reintentar y luego Biblioteca/Diario offline.
- Si la cámara del emulador falla: demostrar el rechazo y usar las capturas reales/documentadas.
- Si el tiempo es corto: omitir el cambio de orden y priorizar Repository, Room y permiso de Cámara.

## Recursos visuales

- Capturas reales: `docs/capturas/`.
- Mockups originales: `docs/mockups/`.
- Paleta original: `docs/mockups/paleta.jpg`.
