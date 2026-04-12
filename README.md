# Voltio

Plataforma de comercio electrónico móvil con roles diferenciados (Cliente / Administrador), comunicación en tiempo real vía WebSocket, persistencia local con Room y uso de hardware nativo del dispositivo.

---

## Tabla de Contenidos

- [Descripción](#descripción)
- [Capturas de Pantalla](#capturas-de-pantalla)
- [Arquitectura](#arquitectura)
- [Tecnologías y Dependencias](#tecnologías-y-dependencias)
- [Funcionalidades por Rol](#funcionalidades-por-rol)
- [Hardware Utilizado](#hardware-utilizado)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Configuración e Instalación](#configuración-e-instalación)
- [Equipo](#equipo)

---

## Descripción

Voltio es una aplicación Android de e-commerce que conecta clientes con una tienda en línea. Los **clientes** pueden explorar productos, gestionar su carrito (persistido localmente) y realizar pedidos con dirección de entrega. Los **administradores** gestionan el catálogo de productos, el inventario y reciben notificaciones en tiempo real de nuevos pedidos con feedback háptico.

**URL API:** `https://voltio.ameth.shop/api/`  
**URL WebSocket:** `wss://voltio-ws.ameth.shop`

---

## Capturas de Pantalla

> *(Agregar capturas de Login, Home Cliente, Home Admin, Carrito, Checkout, Pedidos)*

---

## Arquitectura

El proyecto sigue **Clean Architecture** con **MVVM** organizado por features. Cada feature es autónomo con sus propias capas:

```
feature/
├── data/
│   ├── datasource/
│   │   ├── local/       # Room DAOs y mappers
│   │   └── remote/      # API service, DTOs, mappers
│   └── repositories/    # Implementación del repositorio
├── di/                  # Módulo Hilt del feature
├── domain/
│   ├── entities/        # Modelos de negocio
│   ├── repositories/    # Interfaces de repositorio
│   └── usecase/         # Casos de uso
└── presentation/
    ├── components/      # Composables del feature
    ├── screens/         # Pantallas + UiState
    └── viewmodel/       # ViewModels con StateFlow
```

El `ViewModel` nunca accede directamente a Room ni a Retrofit — solo interactúa con `UseCases`. Los `UseCases` dependen de interfaces de repositorio, haciendo la capa de dominio completamente independiente de Android.

### Flujo de estado

```
UI (Composable)
  └── collectAsState()
        └── StateFlow<XUiState>  ← ViewModel
              └── UseCase
                    └── Repository
                          ├── Remote (Retrofit)
                          └── Local (Room)
```

---

## Tecnologías y Dependencias

| Categoría | Tecnología | Versión |
|---|---|---|
| Lenguaje | Kotlin | 2.1.0 |
| UI | Jetpack Compose BOM | 2026.01.01 |
| UI Design | Material 3 | BOM |
| Navegación | Navigation Compose | 2.9.7 |
| ViewModel | Lifecycle ViewModel Compose | 2.10.0 |
| DI | Hilt | 2.59.1 |
| Red | Retrofit 2 + Gson | 3.0.0 |
| WebSocket | Socket.IO Client | 2.1.0 |
| Base de datos | Room | 2.7.0 |
| Imágenes | Coil Compose | 2.7.0 |
| Biometría | AndroidX Biometric | 1.1.0 |
| Procesamiento | KSP | 2.1.0-1.0.29 |

### Módulos Hilt

| Módulo | Provee |
|---|---|
| `NetworkModule` | Retrofit, OkHttp, VoltioAuthCookieJar |
| `DatabaseModule` | AppDatabase, CartDao |
| `HardwareModule` | AuthManager, CameraManager, VibrationManager |
| `SocketModule` | VoltioSocketManager |
| `NavigationModule` | NavigationManager |

---

## Funcionalidades por Rol

### Cliente
- Registro e inicio de sesión
- Explorar catálogo de productos con imágenes
- Ver detalle de producto
- Agregar/eliminar productos del carrito (persistido con Room)
- Ajustar cantidades en el carrito
- Seleccionar o crear dirección de entrega
- Realizar pedido con checkout multi-paso
- Ver historial de pedidos propios

### Administrador
- Inicio de sesión rápido con **huella digital** (BiometricPrompt)
- CRUD completo de productos (con carga de imagen desde cámara)
- Gestión de stock
- Ver todos los pedidos en tiempo real (WebSocket)
- Actualizar estado de pedidos
- Notificación háptica instantánea al recibir nuevos pedidos

---

## Hardware Utilizado

El proyecto abstrae el hardware con interfaces en `core/hardware/domain/` e implementaciones Android en `core/hardware/data/`:

### 1. Biometría (`AndroidAuthManager`)
Usa `BiometricPrompt` para autenticación rápida del administrador.
```kotlin
biometricPrompt.authenticate(
    promptInfo,  // BIOMETRIC_STRONG + DEVICE_CREDENTIAL
    ...
)
```
**Permiso requerido:** `USE_BIOMETRIC`

### 2. Cámara (`AndroidCameraManager`)
Crea URIs temporales con `FileProvider` y lee los bytes de imagen para subirla como `Multipart` al crear/editar productos.

**Permiso requerido:** `CAMERA`

### 3. Vibración (`AndroidVibrationManager`)
Feedback háptico de 500ms al recibir una nueva orden. Usa `VibrationEffect.createOneShot()` en Android O+ con fallback al API legado.

**Permiso requerido:** `VIBRATE`

---

## Estructura del Proyecto

```
app/src/main/java/com/miltonvaz/voltio_1/
│
├── core/
│   ├── database/          # AppDatabase, CartEntity, CartDao
│   ├── di/                # Módulos Hilt globales
│   ├── hardware/          # Interfaces y implementaciones de hardware
│   ├── navigation/        # NavGraph, Screens, NavigationWrapper
│   ├── network/           # VoltioApi, TokenManager, CookieJar, SocketManager
│   └── ui/
│       ├── components/    # CustomTextField, PrimaryButton, SocialButton, TextDivider
│       └── theme/         # Color, Theme, Type (Material 3)
│
└── features/
    ├── auth/              # Login, Register
    ├── products/          # Home Admin, Home Cliente, ProductForm, Stock
    ├── orders/            # Carrito, Checkout, Pedidos Admin, Pedidos Usuario
    └── directions/        # Gestión de direcciones de entrega
```

---

## Configuración e Instalación

### Requisitos
- Android Studio Narwhal o superior
- JDK 17+
- Android SDK 35
- Dispositivo/emulador con API 26+

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/Miltonvaz/Voltio.git

# 2. Abrir en Android Studio

# 3. Crear local.properties con las variables de entorno si se requieren
# (el proyecto usa Secrets Gradle Plugin)

# 4. Sincronizar Gradle y ejecutar
```

### Build Flavors
| Flavor | applicationId | Nombre |
|---|---|---|
| `dev` | `com.miltonvaz.voltio_1.dev` | Demo (DEV) |
| `prod` | `com.miltonvaz.voltio_1` | Demo |

---

## Equipo

| Nombre | GitHub |
|---|---|
| Milton De Jesús Vázquez Pérez | [@Miltonvaz](https://github.com/Miltonvaz) |
| Ameth De Jesús Méndez Toledo | [@Ameth-Toledo](https://github.com/Ameth-Toledo) |
| Jared Torres | — |
| Víctor Pérez | — |

---

*Proyecto desarrollado para la materia Programación para Móviles I — Ingeniería en Software, 2025.*
