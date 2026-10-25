# Walkthrough - Última Entrega del Proyecto (100%)

Se han implementado con éxito todos los requisitos obligatorios y avanzados para la **Última Entrega (100%)** de la aplicación **ClassLy**.

## Cambios Realizados

### 1. Persistencia Ligera (`SharedPreferences`)
#### [UserPreferencesHelper.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/data/local/UserPreferencesHelper.java)
- Utilidad para almacenar de forma eficiente preferencias de usuario, estados de sesión y configuraciones del sistema.

### 2. Base de Datos Local con Room (ORM & SQLite)
#### Entidades y DAOs (`data/local/room/entities/` y `daos/`)
- Se crearon las entidades (`SubjectEntity`, `ExamEntity`, `AssignmentEntity`, `ClassEntity`) y sus respectivos DAOs (`SubjectDao`, `ExamDao`, `AssignmentDao`, `ClassDao`) aplicando consultas SQL explícitas (`SELECT`, `INSERT`, `UPDATE`, `DELETE`).
- **`AppDatabase.java`**: Configuración central de la base de datos SQLite con Room y patrón Singleton.
- **`DataRepository.java`**: Actualizado para interactuar de forma transparente con la base de datos local de Room respaldada por hilos secundarios (`ExecutorService`), manteniendo retrocompatibilidad.

### 3. Consumo de Servicios Web (Retrofit & REST API)
#### [ApiService.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/data/remote/ApiService.java) & [RetrofitClient.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/data/remote/RetrofitClient.java)
- Implementación del cliente HTTP mediante Retrofit con convertidor Gson para conectar la aplicación con servicios web remotos y APIs REST utilizando verbos HTTP (`GET`).

### 4. Gestión de Conectividad (Online/Offline)
#### [NetworkUtils.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/utils/NetworkUtils.java)
- Utilidad para detectar en tiempo real el estado de la red e internet (`ConnectivityManager`), permitiendo un manejo coherente de los estados offline.

## Resultados de Validación

### Automated Tests
- Compilación completa del proyecto exitosa (`BUILD SUCCESSFUL`).
