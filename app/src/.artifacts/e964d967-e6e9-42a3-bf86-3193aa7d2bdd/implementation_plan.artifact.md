# Plan de Implementación: Requisitos para la Segunda Entrega (50%)

Adaptar y robustecer el proyecto **ClassLy** para cumplir estrictamente con los **4 requerimientos obligatorios** indicados en la rúbrica de la segunda entrega:
1. **Arquitectura Limpia y POO:** Herencia, polimorfismo y encapsulamiento.
2. **Asincronismo y Gestión de Hilos:** Uso de `ExecutorService` (Java) para proteger el hilo principal (*Main Thread*).
3. **Resiliencia y Feedback Visual:** Máquina de estados (`enum UiState`), `ProgressBar` y bloques `try/catch`.
4. **Auditoría y Depuración:** Logging profesional con `Logcat`.

## User Review Required

> [!IMPORTANT]
> **Modificaciones técnicas exactas:**
> - **POO:** Creación de `BaseEntity` (clase abstracta de la que heredan `ClassInfo`, `ExamInfo`, `SubjectInfo`, `AssignmentInfo`) y `BaseViewModel`.
> - **Hilos (`ExecutorService`):** Invocación de operaciones de datos en segundo plano mediante `ExecutorService`.
> - **Feedback Visual (`enum UiState`):** Implementación de estados (`LOADING`, `SUCCESS`, `ERROR`) con `ProgressBar` en pantallas y manejo estricto de excepciones `try/catch`.
> - **Logcat:** Trazas de auditoría con `Log.d`, `Log.i`, `Log.e` bajo el tag `"ClassLyDebug"`.

## Proposed Changes

### 1. Arquitectura Limpia y POO
#### [NEW] [BaseEntity.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/data/models/BaseEntity.java)
- Clase base abstracta demostrando herencia y encapsulamiento para los modelos.

#### [NEW] [BaseViewModel.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/ui/viewmodels/BaseViewModel.java)
- ViewModel base que gestiona el `ExecutorService` compartido y el estado `UiState`.

### 2. Resiliencia y Máquina de Estados
#### [NEW] [UiState.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/ui/viewmodels/UiState.java)
- Enum con estados `LOADING`, `SUCCESS`, `ERROR`.

### 3. Asincronismo y Auditoría en Repositorios y ViewModels
#### [MODIFY] [DataRepository.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/data/repository/DataRepository.java)
- Incorporar bloques `try/catch` y `Logcat` profesional en todas las operaciones.

#### [MODIFY] [ScheduleViewModel.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/ui/viewmodels/ScheduleViewModel.java), [ExamsViewModel.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/ui/viewmodels/ExamsViewModel.java), [SubjectsViewModel.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/ui/viewmodels/SubjectsViewModel.java)
- Heredar de `BaseViewModel`, ejecutar consultas mediante `ExecutorService` y emitir `UiState`.

### 4. Feedback Visual en Vistas
#### [MODIFY] [fragment_schedule.xml](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/res/layout/fragment_schedule.xml), [fragment_exams.xml](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/res/layout/fragment_exams.xml), [fragment_subjects.xml](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/res/layout/fragment_subjects.xml)
- Añadir `ProgressBar` para mostrar estado de carga.

## Verification Plan

### Automated Tests
- Compilación limpia mediante Gradle (`app:assembleDebug`).

### Manual Verification
- Comprobar en Logcat (`ClassLyDebug`) los rastros de ejecución, hilos y captura de errores.
- Verificar que las barras de progreso (`ProgressBar`) reflejen los estados de carga.
