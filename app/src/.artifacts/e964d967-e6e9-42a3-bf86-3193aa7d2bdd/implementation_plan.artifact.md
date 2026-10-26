# Plan de Implementación: Correcciones de la Retroalimentación (2da Entrega)

Atender y resolver de manera rigurosa cada uno de los **Puntos Críticos a Corregir** señalados por el profesor en la retroalimentación de la segunda entrega.

## User Review Required

> [!IMPORTANT]
> **Correcciones clave a aplicar:**
> 1. **ViewModels para `HomeFragment` y `CalendarFragment`:** Se crearán `HomeViewModel` y `CalendarViewModel` para eliminar accesos síncronos directos a `DataRepository` desde las vistas.
> 2. **Observar `errorMessageLiveData`:** Los fragmentos observarán el canal de errores del ViewModel y mostrarán alertas visuales (Toast/Snackbar).
> 3. **Validación contra `NumberFormatException`:** Uso defensivo de `try/catch` y validaciones en los inputs numéricos de los diálogos.
> 4. **Validaciones de Login y Registro:** Uso de `Patterns.EMAIL_ADDRESS` y validación de longitud mínima de contraseña (mínimo 6 caracteres).

## Proposed Changes

### 1. Nuevos ViewModels (`ui/viewmodels/`)
#### [NEW] [HomeViewModel.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/ui/viewmodels/HomeViewModel.java)
- Gestiona la carga asincrónica de la próxima clase y el examen más cercano para el `HomeFragment`.

#### [NEW] [CalendarViewModel.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/ui/viewmodels/CalendarViewModel.java)
- Gestiona la carga asincrónica de exámenes y TPs filtrados por fecha seleccionada para el `CalendarFragment`.

### 2. Actualización de Vistas y Observación de Errores
#### [MODIFY] [HomeFragment.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/ui/controllers/fragments/HomeFragment.java), [CalendarFragment.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/ui/controllers/fragments/CalendarFragment.java), [ScheduleFragment.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/ui/controllers/fragments/ScheduleFragment.java), [ExamsFragment.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/ui/controllers/fragments/SubjectsFragment.java)
- Integrar `HomeViewModel` y `CalendarViewModel`.
- Observar `errorMessageLiveData` en todos los fragmentos para reportar fallos asíncronos al usuario.

### 3. Prevención de `NumberFormatException` y Validación de Formularios
#### [MODIFY] Diálogos de adición (`ScheduleFragment`, `ExamsFragment`, `SubjectsFragment`)
- Envolver conversiones de texto a número (`Double.parseDouble`, `Integer.parseInt`) en bloques `try/catch` defensivos.

#### [MODIFY] [LoginFragment.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/ui/controllers/fragments/LoginFragment.java), [RegisterFragment.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/ui/controllers/fragments/RegisterFragment.java)
- Validar formato de email con `Patterns.EMAIL_ADDRESS` y contraseña segura (mínimo 6 caracteres).

## Verification Plan

### Automated Tests
- Compilación limpia del proyecto mediante Gradle (`app:assembleDebug`).

### Manual Verification
- Comprobar que `HomeFragment` y `CalendarFragment` carguen correctamente sus datos mediante sus respectivos ViewModels.
- Probar ingresar datos inválidos en formularios numéricos y verificar que no haya crashes (`NumberFormatException`).
- Probar credenciales incorrectas o emails mal formados en Login/Registro.
