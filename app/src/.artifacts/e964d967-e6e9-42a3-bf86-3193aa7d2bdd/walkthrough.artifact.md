# Walkthrough - Apartado de Trabajos Prácticos (TPs) y Calendario

Se ha implementado con éxito la funcionalidad completa para gestionar y recordar entregas de trabajos prácticos (TPs) y su integración en el calendario de la aplicación **ClassLy**.

## Cambios Realizados

### 1. Modelo de Datos
#### [AssignmentInfo.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/data/models/AssignmentInfo.java)
- Nuevo modelo para almacenar información de los TPs (materia, fecha de vencimiento, título, descripción, estado de entrega y nota).

#### [DataRepository.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/data/repository/DataRepository.java)
- Se añadió la lista de TPs, datos de ejemplo iniciales y métodos CRUD completos (`getAssignments`, `addAssignment`, `updateAssignment`, `deleteAssignment`).

### 2. Interfaz y Adaptadores
#### [strings.xml](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/res/values/strings.xml)
- Nuevos recursos de texto y etiquetas para la gestión de TPs.

#### [dialog_add_assignment.xml](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/res/layout/dialog_add_assignment.xml) & [item_assignment.xml](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/res/layout/item_assignment.xml)
- Layouts para el formulario de adición/edición y para las tarjetas de visualización en RecyclerView con indicador de estado (entregado/pendiente) y calificación.

#### [AssignmentsAdapter.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/ui/adapters/AssignmentsAdapter.java)
- Adaptador RecyclerView para renderizar la lista de TPs con alertas visuales de vencimiento próximo.

### 3. Integración en el Calendario
#### [fragment_calendar.xml](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/res/layout/fragment_calendar.xml) & [CalendarFragment.java](file:///C:/Users/MirkoM/AndroidStudioProjects/ClassLy/app/src/main/java/com/example/classly/ui/controllers/fragments/CalendarFragment.java)
- Se actualizó el calendario para mostrar **múltiples decoradores de colores** (puntos rojos para exámenes y puntos azules para entregas de TPs).
- Se muestran secciones separadas para exámenes y TPs según el día seleccionado.
- Se añadió un botón flotante ("Agregar TP") para registrar nuevas entregas directamente desde la vista del calendario.

## Resultados de Validación

### Automated Tests
- Compilación del proyecto exitosa (`BUILD SUCCESSFUL`).
