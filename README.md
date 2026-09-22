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

## Reparto de trabajo del equipo

- Rama `feature/dominio-contreras`: modelado del dominio (entidades, reglas de negocio,
  casos de uso) e integración de la capa de datos simulada — Contreras.
- (Completar con la funcionalidad y el integrante correspondiente antes de la entrega.)

## Estado de las solicitudes de cambio (Parte II)

Ninguna de las solicitudes SC-A a SC-D del examen está implementada todavía en esta rama;
se desarrollan en vivo, cada una en su propia rama `sc-<letra>-<apellido>` creada a partir
de `develop`.