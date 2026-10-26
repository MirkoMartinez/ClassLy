# Walkthrough - Correcciones de la Retroalimentación (2da Entrega)

Se han aplicado y verificado rigurosamente todas las correcciones señaladas en la retroalimentación del profesor para la segunda entrega de **ClassLy**.

## Cambios Realizados

### 1. Refactorización de `HomeFragment` y `CalendarFragment` con ViewModels
- Se crearon **`HomeViewModel.java`** y **`CalendarViewModel.java`** heredando de `BaseViewModel`.
- Ambos fragmentos eliminaron el acceso síncrono directo al repositorio y ahora gestionan de forma asincrónica (**hilos secundarios** con `ExecutorService`) la carga de sus datos, respetando estrictamente el patrón MVVM.

### 2. Observación de `errorMessageLiveData`
- Se configuró la observación de `errorMessageLiveData` en todos los fragmentos principales (`Schedule`, `Exams`, `Subjects`, `Home`, `Calendar`) para reportar automáticamente cualquier fallo en los procesos asíncronos mediante `Toast`.

### 3. Prevención de `NumberFormatException`
- Se envolvieron todas las conversiones de texto a número (`Double.parseDouble`, `Integer.parseInt`) en los diálogos de adición y edición en bloques `try/catch` defensivos con notificaciones de error controladas al usuario.

### 4. Validaciones Robustas en Login y Registro
- Se incorporó la validación de formato de correo electrónico utilizando `android.util.Patterns.EMAIL_ADDRESS` y la validación de seguridad de contraseñas (mínimo 6 caracteres) tanto en `LoginFragment` como en `RegisterFragment`.

## Resultados de Validación

### Automated Tests
- Compilación del proyecto exitosa (`BUILD SUCCESSFUL`).
