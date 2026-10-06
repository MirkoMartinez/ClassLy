package com.example.classly.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

/**
 * Utilidad encargada de procesar archivos PDF de horarios mediante renderizado de páginas a mapas de bits (Bitmap)
 * y reconocimiento óptico de caracteres (OCR) utilizando Google ML Kit Text Recognition.
 */
public class PdfScheduleParser {
    private static final String TAG = "PdfScheduleParser";

    public interface OnTextExtractedListener {
        void onSuccess(String extractedText);
        void onError(Exception e);
    }

    /**
     * Extrae el texto completo de un archivo PDF dado su URI ejecutándose en un hilo secundario.
     * Renderiza cada página a alta resolución (2x escala) para optimizar la precisión del OCR.
     *
     * @param context  Contexto de la aplicación.
     * @param pdfUri   URI del PDF seleccionado.
     * @param listener Callback de éxito o error.
     */
    public static void extractTextFromPdfUri(Context context, Uri pdfUri, OnTextExtractedListener listener) {
        new Thread(() -> {
            try {
                ParcelFileDescriptor fileDescriptor = context.getContentResolver().openFileDescriptor(pdfUri, "r");
                if (fileDescriptor == null) {
                    listener.onError(new Exception("No se pudo abrir el archivo PDF."));
                    return;
                }

                PdfRenderer renderer = new PdfRenderer(fileDescriptor);
                StringBuilder fullText = new StringBuilder();
                TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

                int pageCount = renderer.getPageCount();
                for (int i = 0; i < pageCount; i++) {
                    PdfRenderer.Page page = renderer.openPage(i);
                    // Renderizar página a Bitmap (escala 2x para mejorar la resolución y precisión del OCR)
                    int width = page.getWidth() * 2;
                    int height = page.getHeight() * 2;
                    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                    page.close();

                    InputImage image = InputImage.fromBitmap(bitmap, 0);
                    Task<Text> task = recognizer.process(image);
                    Tasks.await(task);

                    if (task.isSuccessful() && task.getResult() != null) {
                        fullText.append("\n--- Página ").append(i + 1).append(" ---\n");
                        fullText.append(task.getResult().getText());
                    }
                    bitmap.recycle();
                }

                renderer.close();
                fileDescriptor.close();

                listener.onSuccess(fullText.toString());

            } catch (Exception e) {
                Log.e(TAG, "Error extrayendo texto del PDF", e);
                listener.onError(e);
            }
        }).start();
    }
}
