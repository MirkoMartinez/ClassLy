# Documentación Técnica del Proyecto ClassLy

ClassLy es una aplicación móvil desarrollada para Android en Java (Java 11) orientada a la gestión académica estudiantil (horarios de clases, exámenes, materias y calendario).

---

## 1. Arquitectura General del Proyecto

El proyecto sigue una arquitectura organizada por capas y componentes funcionales (UI, Datos y Utilidades), adoptando los siguientes patrones y buenas prácticas:
- **Single-Activity Architecture**: Utiliza una única actividad principal (`MainActivity`) que actúa como contenedor y ancla para múltiples `Fragments` mediante transacciones de `FragmentManager`.
- **ViewBinding**: Emplea ViewBinding en todas las actividades, fragmentos y adaptadores para garantizar seguridad de tipos (`type-safe`) al acceder a las vistas del layout, eliminando el uso de `findViewById`.
- **Patrón Singleton**: El repositorio de datos (`DataRepository`) se implementa como Singleton para centralizar y mantener el estado de la aplicación en memoria de forma consistente entre pantallas.
- **Material Design 3**: Utiliza componentes modernos de Material Design (BottomNavigationView, TabLayout, FloatingActionButton, MaterialCalendarView, MaterialCardView).

---

## 2. Estructura de Datos (`data/models`)

Las entidades principales se encuentran en el paquete `com.example.classly.data.models`:

- **`ClassInfo.java`**:
  - Representa una clase o turno académico.
  - Campos: `id` (UUID), `subject` (materia), `time` (horario, ej. "08:00 - 10:00"), `room` (aula), `professor` (profesor) y `dayIndex` (0 = Lunes, 1 = Martes, etc.).

- **`ExamInfo.java`**:
  - Representa un examen o evaluación.
  - Campos: `id` (UUID), `subject` (materia), `date` (fecha YYYY-MM-DD), `type` (tipo, ej. Parcial/Final) y `topic` (tema).
  - Incluye el método `getDaysRemaining()` que calcula dinámicamente los días restantes hasta la fecha del examen usando la API `java.time`.

- **`SubjectInfo.java`**:
  - Representa una asignatura cursada.
  - Campos: `id` (UUID), `name` (nombre), `hours` (carga horaria), `averageGrade` (promedio), `isPromotional` (booleano) y `promotionGrade` (nota de promoción).

---

## 3. Adaptadores de RecyclerView (`ui/adapters`)

Para optimizar el rendimiento y la reutilización de vistas en listas, se implementan tres adaptadores con ViewBinding y interfaces de callback para acciones CRUD:

- **`ExamsAdapter.java`**: Muestra la lista de exámenes, destacando en rojo si faltan menos de 7 días.
- **`ScheduleAdapter.java`**: Muestra las clases de un día específico (materia, hora, aula, profesor).
- **`SubjectsAdapter.java`**: Muestra las materias, carga horaria, notas y estado de promoción (promocionando vs. va a final).

---

## 4. Controladores, Actividad y Fragmentos (`ui/controllers`)

- **`MainActivity.java`**: Actividad anfitriona. Gestiona la barra de navegación inferior (`BottomNavigationView`), controla la visibilidad de la barra (oculta en Login/Registro), gestiona permisos de notificaciones en Android 13+ y programa recordatorios con WorkManager (`ExamReminderWorker`).
- **`LoginFragment.java` & `RegisterFragment.java`**: Pantallas de autenticación con validación de campos e integración de Google Sign-In mediante la API de Credential Manager.
- **`HomeFragment.java`**: Panel principal con tarjetas de resumen para la próxima clase del día y el examen más cercano.
- **`ScheduleFragment.java`**: Gestión de horarios con pestañas (`TabLayout`) de Lunes a Viernes y acceso a la importación por PDF.
- **`ExamsFragment.java`**: Gestión de exámenes con diálogo interactivo y selector de fecha (`DatePickerDialog`).
- **`SubjectsFragment.java`**: Gestión de asignaturas con control de promoción.
- **`CalendarFragment.java`**: Calendario interactivo (`MaterialCalendarView`) con decoradores (`ExamDayDecorator`) que marcan los días de exámenes con puntos indicadores.

---

## 5. Funcionalidad de Importación por PDF (OCR con ML Kit y Parsing Heurístico)

La característica clave de importación automática de horarios desde archivos PDF se compone de dos clases principales ubicadas en `utils/`:

### A. Extracción OCR: `PdfScheduleParser.java`
- Utiliza la API nativa de Android `PdfRenderer` para abrir el documento PDF y renderizar cada página a un objeto `Bitmap` de alta resolución (escala 2x para mejorar la precisión del reconocimiento óptico).
- Integra **Google ML Kit Text Recognition** (`TextRecognizer`) para procesar la imagen bitmap y extraer todo el texto plano contenido en el documento PDF de forma asíncrona en un hilo secundario.

### B. Análisis Heurístico: `ScheduleParserHelper.java`
- Recibe el texto bruto extraído por OCR y aplica un análisis basado en heurísticas y expresiones regulares (`Pattern`).
- **Detección de Días**: Analiza línea por línea buscando palabras clave como "lunes", "martes", "miércoles", "jueves", "viernes", "sábado" (en español e inglés) para actualizar el `dayIndex` activo.
- **Detección de Horarios**: Utiliza una expresión regular (`\d{1,2}:\d{2}\s*[-–]\s*\d{1,2}:\d{2}`) para identificar bloques horarios (ej. `08:00 - 10:00`).
- **Extracción de Materias**: Extrae el nombre de la materia eliminando el bloque horario y caracteres especiales sobrantes.
- **Fallback**: Cuenta con un mecanismo de respaldo si no se detectan rangos horarios formales, tomando líneas de texto válidas y distribuyéndolas entre los días de la semana.
- **Previsualización**: En `ScheduleFragment`, antes de guardar las clases en el repositorio (`DataRepository`), se muestra un diálogo de previsualización con las clases detectadas para confirmación del usuario.
