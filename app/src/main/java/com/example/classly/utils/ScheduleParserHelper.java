package com.example.classly.utils;

import com.example.classly.data.models.ClassInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilidad de análisis heurístico (Heuristic Parsing) para convertir el texto plano extraído por OCR
 * de un PDF de horarios en una lista estructurada de objetos ClassInfo.
 * Detecta días de la semana y patrones de rangos horarios (ej. "08:00 - 10:00").
 */
public class ScheduleParserHelper {

    /**
     * Parsea el texto plano extraído del PDF aplicando expresiones regulares y heurísticas de días/horarios.
     *
     * @param rawText Texto bruto extraído por OCR.
     * @return Lista de objetos ClassInfo detectados.
     */
    public static List<ClassInfo> parseScheduleText(String rawText) {
        List<ClassInfo> extractedClasses = new ArrayList<>();
        if (rawText == null || rawText.isEmpty()) {
            return extractedClasses;
        }

        String[] lines = rawText.split("\n");
        int currentDayIndex = 0; // Por defecto Lunes (0)

        // Expresión regular para detectar rangos horarios (ej. "08:00 - 10:00" o "8:00–10:00")
        Pattern timePattern = Pattern.compile("(\\d{1,2}:\\d{2})\\s*[-–]\\s*(\\d{1,2}:\\d{2})");

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;

            String lower = trimmed.toLowerCase();
            // Detección heurística de días de la semana en español e inglés
            if (lower.contains("lunes") || lower.contains("monday")) {
                currentDayIndex = 0;
                continue;
            } else if (lower.contains("martes") || lower.contains("tuesday")) {
                currentDayIndex = 1;
                continue;
            } else if (lower.contains("miércoles") || lower.contains("miercoles") || lower.contains("wednesday")) {
                currentDayIndex = 2;
                continue;
            } else if (lower.contains("jueves") || lower.contains("thursday")) {
                currentDayIndex = 3;
                continue;
            } else if (lower.contains("viernes") || lower.contains("friday")) {
                currentDayIndex = 4;
                continue;
            } else if (lower.contains("sábado") || lower.contains("sabado") || lower.contains("saturday")) {
                currentDayIndex = 5;
                continue;
            }

            // Buscar coincidencias de horarios en la línea
            Matcher matcher = timePattern.matcher(trimmed);
            if (matcher.find()) {
                String startTime = matcher.group(1);
                String endTime = matcher.group(2);
                String timeSlot = startTime + " - " + endTime;

                // Extraer el nombre de la materia eliminando el horario y símbolos sobrantes
                String subjectName = trimmed.replaceAll(timePattern.pattern(), "").trim();
                subjectName = subjectName.replaceAll("^[-–:\\s]+|[-–:\\s]+$", "");
                if (subjectName.isEmpty()) {
                    subjectName = "Materia Importada";
                }

                String classroom = "Aula por definir";
                String professor = "Profesor por definir";

                extractedClasses.add(new ClassInfo(subjectName, timeSlot, classroom, professor, currentDayIndex));
            }
        }

        // Mecanismo de respaldo (Fallback): si no se detectó ningún rango horario, toma líneas de texto válidas
        if (extractedClasses.isEmpty()) {
            int count = 0;
            for (String line : lines) {
                String l = line.trim();
                if (l.length() > 4 && !l.startsWith("---") && !l.toLowerCase().contains("página")) {
                    extractedClasses.add(new ClassInfo(l, "08:00 - 10:00", "Aula 101", "Profesor", count % 5));
                    count++;
                    if (count >= 10) break;
                }
            }
        }

        return extractedClasses;
    }
}
