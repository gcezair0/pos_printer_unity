package com.guilherme.pos_printer.core;

import android.graphics.Bitmap;

/**
 * Common interface that all printer adapters must implement.
 *
 * Each POS terminal brand has its own adapter that bridges
 * the vendor SDK to this unified interface.
 */
public interface IPrinterAdapter {

    /**
     * Initialize the printer hardware.
     *
     * @param callback called when initialization completes or fails.
     */
    void init(PrinterAdapterCallback callback);

    /**
     * Print a bitmap image.
     *
     * @param bitmap   the image to print.
     * @param callback called when printing completes or fails.
     */
    void printBitmap(Bitmap bitmap, PrinterAdapterCallback callback);

    /**
     * Print plain text.
     *
     * @param text     the text to print.
     * @param callback called when printing completes or fails.
     */
    void printText(String text, PrinterAdapterCallback callback);

    /**
     * Feed paper by the given number of lines.
     *
     * @param lines number of blank lines to advance.
     */
    void feedPaper(int lines);

    /**
     * Get the current printer status string.
     *
     * Expected values: READY, BUSY, OUT_OF_PAPER, OVERHEAT,
     * LOW_VOLTAGE, NOT_INITIALIZED, ERROR, UNKNOWN.
     */
    String getStatus();

    /**
     * Get the brand identifier for this adapter.
     *
     * Expected values: POSITIVO, PAX, GERTEC, POSMP, INGENICO.
     */
    String getBrand();
}
