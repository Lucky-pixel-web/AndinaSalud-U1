# AndinaSalud - Producto Unidad 1

Aplicación móvil multiplataforma (KMP + Compose Multiplatform) para la gestión de citas
médicas de la red AndinaSalud. Funciona con datos simulados en memoria, siguiendo una
arquitectura Clean + MVVM que permite sustituir la fuente de datos por una API real sin
modificar la interfaz ni los casos de uso.

## Estructura de paquetes

shared/src/commonMain/kotlin/pe/edu/upeu/andinasalud/
├── domain/
│ ├── model/ Cita, EstadoCita (sealed class), Medico, Sede, Paciente
│ ├── repository/ CitaRepository (interfaz)
│ └── usecase/ ObtenerCitasUseCase, SolicitarCitaUseCase, CancelarCitaUseCase, etc.
├── data/
│ ├── local/ CitasSimuladas (datos semilla en memoria)
│ └── repository/ CitaRepositoryFake (implementación simulada de CitaRepository)
├── presentation/
│ ├── citas/ CitasViewModel, CitasUiState, CitasScreen
│ ├── detalle/ DetalleCitaViewModel, DetalleCitaScreen
│ ├── solicitud/ SolicitudViewModel, SolicitudScreen
│ ├── perfil/ PerfilScreen
│ └── theme/ Color, Type, AndinaSaludTheme
├── navigation/ Destinos, Screen (pila simple manejada en App.kt)
├── di/ AppModule (módulos de Koin)
└── App.kt Composable raíz: Scaffold + navegación + theming



## Decisiones de arquitectura

- **Clean + MVVM**: la capa `domain` no depende de Android ni de Compose; expone una
  interfaz `CitaRepository` que `data.repository.CitaRepositoryFake` implementa con datos
  en memoria. El día que exista la API REST, basta con crear un `CitaRepositoryApi` que
  implemente la misma interfaz y cambiar el binding en `AppModule`.
- **Reglas de negocio (RN-01 a RN-05)** viven en el dominio: RN-01, RN-02, RN-04 y RN-05 se
  validan en `SolicitarCitaUseCase`; RN-03 vive como método en la propia entidad `Cita`
  (`puedeCancelarse`) y se usa desde `CancelarCitaUseCase`.
- **Estado de la cita** modelado con `sealed class EstadoCita` (Programada, Atendida,
  Cancelada), cada una con su propia información asociada.
- **ViewModels** exponen `StateFlow<UiState>` (nunca variables mutables públicas) y usan
  `sealed interface` de "fase" (Cargando / Contenido / Vacío / Error) para representar los
  cuatro estados de interfaz exigidos por RF-08.
- **Inyección de dependencias** con Koin: módulos declarados en `commonMain`
  (`AppModule.kt`) e inicialización específica por plataforma (`MainApplication` en Android,
  `MainViewController`/`Koinios` en iOS).

## Cómo ejecutar

- Android: `./gradlew :androidApp:assembleDebug` o el botón Run de Android Studio con el
  run configuration `androidApp`.
- iOS: abrir `/iosApp` en Xcode y ejecutar desde ahí (o usar el run configuration de KMP
  en Android Studio/Fleet).

## Autoría

Examen individual. Todo el desarrollo de la Parte I (dominio, datos simulados,
casos de uso, presentación, navegación, tema e inyección de dependencias) fue
realizado en la rama `feature/dominio-contreras`, fusionada a `develop` y luego
a `main`.

## Solicitud de cambio (Parte II)

Con autorización del docente, se implementaron las siguientes solicitudes de cambio
sobre el producto de la Parte I:

- **SC-A**: chip "Hoy" en la lista de citas, combinado con el filtro de estado
  existente. Resuelto en `CitasViewModel.aplicarFiltros()`.
- **SC-B**: indicador con el número de citas Programadas en la barra de navegación
  inferior, y botón "Solicitar cita" deshabilitado al llegar al límite de tres
  (RN-02). Resuelto con `ContarCitasProgramadasUseCase`, leído desde `App.kt`.
- **SC-C**: modalidad de atención (Presencial / Teleconsulta) incorporada al
  dominio (`Cita.modalidad`), al formulario de solicitud, al detalle y a la lista,
  con ícono distinto por modalidad.