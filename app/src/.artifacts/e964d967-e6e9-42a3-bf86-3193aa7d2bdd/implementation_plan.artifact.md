# Plan de Implementación: Apartado de Trabajos Prácticos (TPs)

Permitir al usuario gestionar y recordar las entregas de trabajos prácticos de cada materia, con fecha de vencimiento, título, descripción, estado de entrega (pendiente/entregado) y calificación opcional, integrándolos visualmente en el calendario de la app.

## User Review Required

> [!IMPORTANT]
> **Integración en el Calendario:** El calendario mostrará puntos indicadores de colores (por ejemplo, rojo para exámenes y azul para fechas de entrega de TPs) y al seleccionar cualquier día se listarán tanto los exámenes como los TPs que venzan en esa fecha.

## Proposed Changes

### 1. Modelo de Datos
#### [NEW] [AssignmentInfo.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/data/models/AssignmentInfo.java)
- Crear el modelo con ID, materia, fecha de vencimiento, título, descripción, estado de entrega (`isSubmitted`) y nota opcional (`grade`).

#### [MODIFY] [DataRepository.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/data/repository/DataRepository.java)
- Añadir lista de trabajos prácticos (`assignments`), datos de ejemplo y métodos CRUD (`getAssignments`, `addAssignment`, `updateAssignment`, `deleteAssignment`).

### 2. Interfaz de Usuario y Adaptadores
#### [NEW] [dialog_add_assignment.xml](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/res/layout/dialog_add_assignment.xml)
- Diálogo para crear/editar un trabajo práctico (materia, título, fecha con DatePicker, descripción y nota).

#### [NEW] [item_assignment.xml](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/res/layout/item_assignment.xml)
- Tarjeta RecyclerView con checkbox para marcar entregado/pendiente, título, materia, fecha y nota.

#### [NEW] [AssignmentsAdapter.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/ui/adapters/AssignmentsAdapter.java)
- Adaptador para la lista de TPs con soporte para acciones de editar, eliminar y cambiar estado de entrega.

### 3. Integración en Calendario
#### [MODIFY] [fragment_calendar.xml](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/res/layout/fragment_calendar.xml)
- Ajustar la interfaz del calendario para soportar la visualización y un botón flotante o menú para agregar nuevos TPs.

#### [MODIFY] [CalendarFragment.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/ui/controllers/fragments/CalendarFragment.java)
- Actualizar los decoradores del calendario para mostrar puntos de colores para exámenes y TPs, filtrar ambos elementos por la fecha seleccionada y gestionar la creación/edición de TPs.

## Verification Plan

### Automated Tests
- Compilación limpia del proyecto mediante Gradle (`app:assembleDebug`).

### Manual Verification
- Abrir la sección de Calendario, verificar los puntos indicadores en las fechas con TPs y exámenes.
- Añadir un nuevo Trabajo Práctico, marcarlo como entregado y comprobar su persistencia en el repositorio.
