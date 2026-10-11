# Guía Técnica de Defensa del Proyecto: ClassLy

Esta guía está diseñada para que cualquier desarrollador o estudiante pueda comprender, explicar y **defender exitosamente el proyecto ClassLy** frente a un profesor experto en programación móvil. Contiene la arquitectura, patrones de diseño, flujos técnicos de cada funcionalidad y preguntas/respuestas clave para la defensa.

---

## 1. Introducción y Descripción del Proyecto

**ClassLy** es una aplicación móvil nativa para Android desarrollada en **Java** utilizando el ecosistema moderno de Android Jetpack y Material 3. Está diseñada específicamente para estudiantes universitarios y terciarios, resolviendo la necesidad de organizar la vida académica en un solo lugar:
- Gestión de horarios de clases semanales.
- **Importación automática de horarios desde PDF (con fotos escaneadas y OCR local)**.
- Gestión de exámenes parciales/finales, cálculo de días restantes y registro de calificaciones.
- Seguimiento de entregas de **Trabajos Prácticos (TPs)** con estado de entrega (pendiente/entregado).
- Control de asignaturas, carga horaria, promedio de calificaciones y estado de promoción.
- Calendario interactivo integrado con múltiples indicadores visuales.
- Notificaciones y recordatorios en segundo plano mediante `WorkManager`.

---

## 2. Arquitectura y Patrones de Diseño

### 2.1. Arquitectura General
El proyecto sigue una **Arquitectura por Capas** modular inspirada en Clean Architecture y los principios de separación de incumbencias (SoC - Separation of Concerns):
1. **Capa de Datos (`data/`):** Modelos de negocio (`models/`) y repositorio central (`repository/DataRepository`).
2. **Capa de Interfaz de Usuario (`ui/`):** Controladores (`ui/controllers/`: Activities y Fragments) y Adaptadores de RecyclerView (`ui/adapters/`).
3. **Capa de Utilidades y Servicios (`utils/`):** Procesamiento de PDFs/OCR, recordatorios y helpers de notificaciones.

### 2.2. Patrones de Diseño Clave
- **Patrón Singleton (Thread-Safe):** Implementado en `DataRepository`. Garantiza que exista una única instancia en toda la aplicación que centralice el estado y los datos en memoria, evitando duplicidad de fuentes de datos entre fragmentos.
- **ViewBinding:** Utilizado en todas las actividades y fragmentos para reemplazar el costoso y propenso a errores `findViewById()`, garantizando seguridad de tipos en tiempo de compilación.
- **Single-Activity Architecture:** La aplicación utiliza una única actividad principal (`MainActivity`) que hospeda múltiples fragmentos mediante **Jetpack Navigation Component**, maximizando la fluidez y reutilización de componentes de ciclo de vida.
- **Callback / Listener Pattern:** Usado ampliamente en los adaptadores de RecyclerView (`OnClassActionListener`, `OnExamActionListener`, `OnAssignmentActionListener`) para desacoplar la lógica visual de las acciones de negocio ejecutadas en los Fragmentos.

---

## 3. Estructura de Paquetes y Clases Principales

```text
com.example.classly/
├── data/
│   ├── models/           # Clases de entidad (ClassInfo, ExamInfo, SubjectInfo, AssignmentInfo)
│   └── repository/       # DataRepository (Singleton centralizador)
├── ui/
│   ├── adapters/         # Adaptadores RecyclerView (Schedule, Exams, Subjects, Assignments)
│   └── controllers/
│       ├── activities/   # MainActivity
│       └── fragments/    # Login, Register, Home, Schedule, Exams, Subjects, Calendar
└── utils/                # Utilidades (PdfScheduleParser, ScheduleParserHelper, ExamReminderWorker, NotificationHelper)
```

---

## 4. Explicación Técnica de Funcionalidades Clave

### 4.1. Importación de Horarios por PDF Escaneado con OCR (IA Local)
Esta es una de las características más avanzadas de la aplicación:
1. **Selección del Archivo:** Mediante `ActivityResultContracts.OpenDocument()`, el usuario selecciona un archivo PDF de su dispositivo.
2. **Renderizado de Páginas (`PdfRenderer`):** La API nativa de Android renderiza cada página del PDF a un mapa de bits (`Bitmap`) de alta resolución en un hilo en segundo plano (`ExecutorService / Thread`).
3. **Reconocimiento Óptico de Caracteres (`Google ML Kit Text Recognition`):** Cada imagen de página se procesa localmente mediante ML Kit (`TextRecognition.getClient()`), extrayendo todo el texto impreso o escaneado sin necesidad de conexión a internet ni servidores externos (garantizando privacidad total).
4. **Procesamiento Heurístico (`ScheduleParserHelper`):** Un motor basado en expresiones regulares (`Regex`) y heurísticas de texto analiza las líneas extraídas para identificar días de la semana (Lunes a Viernes), rangos horarios (ej. `08:00 - 10:00`) y nombres de materias.
5. **Previsualización y Guardado:** Se muestra un diálogo de confirmación al usuario para validar las clases detectadas antes de insertarlas en `DataRepository`.

### 4.2. Gestión de Trabajos Prácticos (TPs) y Exámenes
- **Modelos (`AssignmentInfo` y `ExamInfo`):** Contienen propiedades como fecha, título, descripción, estado booleano de entrega (`isSubmitted`), calificación (`grade`) y cálculo dinámico de días restantes mediante `LocalDate` y `ChronoUnit.DAYS.between()`.
- **Calendario Interactivo (`CalendarFragment`):** Utiliza la librería `MaterialCalendarView` junto con `DayViewDecorator` para inyectar **múltiples puntos indicadores de colores** (Punto Rojo para Exámenes, Punto Azul para vencimientos de TPs) en las fechas correspondientes del calendario.

### 4.3. Tareas en Segundo Plano y Notificaciones (`WorkManager`)
- `ExamReminderWorker` hereda de `Worker` y se ejecuta en segundo plano mediante Android `WorkManager` para verificar periódicamente las fechas de los exámenes y disparar alertas locales con `NotificationCompat.Builder` a través de `NotificationHelper`.

---

## 5. Comandos Clave del Proyecto (Gradle, Git y ADB)

### Comandos de Gradle (Build & Test)
- **Compilar la aplicación (Debug):**
  ```bash
  ./gradlew app:assembleDebug
  ```
- **Ejecutar pruebas unitarias:**
  ```bash
  ./gradlew test
  ```
- **Sincronizar dependencias de Gradle:**
  Utilizado en Android Studio para actualizar repositorios de dependencias (`libs.versions.toml`).

### Comandos de Git (Control de Versiones)
- **Verificar estado del repositorio:**
  ```bash
  git status
  ```
- **Añadir y confirmar cambios:**
  ```bash
  git add -A
  git commit -m "feat: descripción de la mejora"
  ```
- **Enviar cambios al repositorio remoto (GitHub):**
  ```bash
  git push origin main
  ```

---

## 6. Preguntas Frecuentes para la Defensa con el Profesor

### Pregunta 1: ¿Por qué utilizaste un repositorio central (`DataRepository`) en lugar de una base de datos local como Room o una base de datos en la nube?
* **Respuesta técnica:** "Para la escala actual de la aplicación y la fase del proyecto, implementé un repositorio Singleton en memoria con datos mock y reactivos para simplificar la gestión de estado y demostrar el principio de inversión de dependencias y separación de responsabilidades. La arquitectura está diseñada mediante una capa de repositorio, lo que significa que migrar de esta lista en memoria a **Room (SQLite)** o **Firebase Firestore** es transparente: solo requiere reescribir los métodos internos de `DataRepository` sin alterar ninguna vista (Fragment) ni adaptador."

### Pregunta 2: ¿Cómo funciona técnicamente el lector de PDFs escaneados y por qué elegiste ML Kit?
* **Respuesta técnica:** "Muchos estudiantes reciben sus horarios en formato PDF pero escaneados como imágenes, donde las librerías tradicionales de texto (`PDFBox`) fallan porque no hay texto vectorial. Para resolverlo, utilizamos `PdfRenderer` nativo de Android para convertir las páginas del PDF en mapas de bits (`Bitmap`), y luego aplicamos **Google ML Kit Text Recognition** de forma local en el dispositivo. Elegí ML Kit porque opera 100% *offline*, es gratuito, respeta la privacidad del usuario y ofrece un rendimiento sumamente rápido con modelos optimizados para móviles."

### Pregunta 3: ¿Cómo gestionas el ciclo de vida de la UI y evitas fugas de memoria (*Memory Leaks*) con ViewBinding?
* **Respuesta técnica:** "En los Fragmentos de Android, la vista suele destruirse al navegar entre pestañas mientras el Fragmento sigue instanciado en memoria. Para prevenir fugas de memoria, implementé el patrón recomendado por Google anulando la referencia al binding en el método `onDestroyView()` (`binding = null;`). Además, utilizamos arquitectura de Actividad única y componentes del ciclo de vida de Jetpack."

### Pregunta 4: ¿Cómo se calculan los días restantes para los exámenes y TPs?
* **Respuesta técnica:** "Utilizamos la API moderna de fechas de Java (`java.time.LocalDate` y `ChronoUnit.DAYS.between()`) comparando la fecha actual del sistema (`LocalDate.now()`) con la fecha de vencimiento almacenada en formato string (`YYYY-MM-DD`), asegurando un cálculo preciso de los días restantes."
