# Walkthrough - Requisitos de la Segunda Entrega (50%)

Se han implementado y robustecido rigurosamente todos los requerimientos solicitados para la segunda entrega del proyecto **ClassLy**, enfocados en la arquitectura, asincronismo, resiliencia y depuración.

## Cambios Realizados

### 1. Arquitectura Limpia y POO (Herencia y Encapsulamiento)
#### [BaseEntity.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/data/models/BaseEntity.java)
- Clase base abstracta de la cual heredan todas las entidades de datos (`ClassInfo`, `ExamInfo`, `SubjectInfo`, `AssignmentInfo`), aplicando herencia y encapsulamiento estricto.

### 2. Asincronismo y Gestión de Hilos (`ExecutorService`)
#### [BaseViewModel.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/ui/viewmodels/BaseViewModel.java)
- ViewModel base abstracto que provee un pool de hilos (`ExecutorService` con 4 hilos concurrentes) para delegar todas las operaciones pesadas y consultas de datos a **hilos secundarios (Background Threads)**, protegiendo sagradamente el hilo principal (*Main Thread*).

### 3. Resiliencia y Máquina de Estados (`UiState` & `try/catch`)
#### [UiState.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/ui/viewmodels/UiState.java)
- Máquina de estados en `enum` (`LOADING`, `SUCCESS`, `ERROR`) gestionada en los ViewModels para el control de flujos de carga en la UI.
- Todos los ViewModels y el `DataRepository` incorporan bloques `try/catch` estrictos en tiempo de ejecución para evitar cualquier cierre abrupto o excepción no controlada.

### 4. Auditoría y Depuración Profesional (`Logcat`)
- Se incorporaron trazas de depuración estructuradas con `Log.d`, `Log.i` y `Log.e` utilizando un tag consistente (`"ClassLyDebug"`) en todos los repositorios y viewmodels para el seguimiento lógico de ejecución.

## Resultados de Validación

### Automated Tests
- Compilación del proyecto exitosa (`BUILD SUCCESSFUL`).
