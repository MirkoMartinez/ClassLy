# Plan de Implementación: Última Entrega del Proyecto (100%)

Implementar todos los requisitos obligatorios definidos para la entrega final de **ClassLy**: Base de datos local con **Room (ORM & SQLite)**, persistencia ligera con **SharedPreferences**, consumo de servicios web con **Retrofit (REST API)**, y **Gestión de Conectividad (Offline/Online)**.

## User Review Required

> [!IMPORTANT]
> **Nuevas tecnologías clave:**
> 1. **Room Database (ORM):** Mapeo Objeto-Relacional para persistir materias, exámenes, TPs y clases en SQLite local.
> 2. **Retrofit (REST API):** Cliente HTTP para consumir servicios web de forma asíncrona (ej. tips de estudio o anuncios académicos).
> 3. **SharedPreferences:** Persistencia ligera para preferencias de usuario.
> 4. **Gestión de Conectividad:** Detector de estado de red (`ConnectivityManager`).

## Proposed Changes

### 1. Dependencias (`app/build.gradle.kts`)
- Añadir dependencias de Room (Runtime + Compiler) y Retrofit (Core + Gson Converter).

### 2. Persistencia Ligera
#### [NEW] [UserPreferencesHelper.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/data/local/UserPreferencesHelper.java)
- Helper para gestionar SharedPreferences (nombre de usuario, preferencias de sesión, modo offline).

### 3. Base de Datos Local con Room (ORM)
#### [NEW] [AppDatabase.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/data/local/room/AppDatabase.java)
- Configuración de la base de datos SQLite con Room.
- Entidades y DAOs para Subject, Exam, Assignment y Class.
- Actualizar `DataRepository` para interactuar con Room DAOs asíncronamente mediante `ExecutorService`.

### 4. Consumo de Servicios Web (Retrofit & REST API)
#### [NEW] [ApiService.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/data/remote/ApiService.java)
- Interfaz Retrofit con métodos HTTP (`GET`, `POST`, etc.) para servicios web.

#### [NEW] [RetrofitClient.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/data/remote/RetrofitClient.java)
- Instancia singleton del cliente Retrofit.

### 5. Gestión de Conectividad
#### [NEW] [NetworkUtils.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/utils/NetworkUtils.java)
- Utilidad para verificar la conexión a internet activa en tiempo real.

## Verification Plan

### Automated Tests
- Compilación limpia mediante Gradle (`app:assembleDebug`).

### Manual Verification
- Comprobar que los datos persisten localmente al reiniciar la app (Room).
- Probar la llamada a la API REST con Retrofit y verificar la respuesta en Logcat.
- Validar la detección de conectividad de red.
