package com.guilherme.pos_printer.factory;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.guilherme.pos_printer.core.IPrinterAdapter;
import com.guilherme.pos_printer.printers.pax.PaxPrinterAdapter;
import com.guilherme.pos_printer.printers.positivo.PositivoPrinterAdapter;

/**
 * Factory that creates the appropriate {@link IPrinterAdapter} based on
 * the requested brand or auto-detection via {@link Build#MODEL}.
 */
public class PrinterFactory {

    private static final String TAG = "PrinterFactory";

    /**
     * Creates a printer adapter for the given brand.
     *
     * @param context the application context.
     * @param brand   the brand identifier (POSITIVO, PAX, etc.) or AUTO.
     * @return an {@link IPrinterAdapter}, or {@code null} if no compatible
     *         printer was found.
     */
    public static IPrinterAdapter create(Context context, String brand) {
        if (brand == null) {
            brand = "AUTO";
        }

        switch (brand.toUpperCase()) {
            case "POSITIVO":
                return new PositivoPrinterAdapter(context);

            case "PAX":
                return new PaxPrinterAdapter(context);

            // TODO: Implement other brands
            // case "GERTEC":
            //     return new GertecPrinterAdapter(context);
            // case "POSMP":
            //     return new PosmpPrinterAdapter(context);
            // case "INGENICO":
            //     return new IngenicoPrinterAdapter(context);

            case "AUTO":
                return autoDetect(context);

            default:
                Log.w(TAG, "Unknown printer brand: " + brand);
                return null;
        }
    }

    /**
     * Auto-detect the printer based on the device model.
     */
    private static IPrinterAdapter autoDetect(Context context) {
        String model = Build.MODEL;
        Log.i(TAG, "Auto-detecting printer for device model: " + model);

        // Positivo: L3, L400
        if ("L3".equals(model) || "L400".equals(model)) {
            Log.i(TAG, "Detected Positivo terminal: " + model);
            return new PositivoPrinterAdapter(context);
        }

        // PAX: A910, A910S, A920, A920Pro, etc.
        if (model.startsWith("A910") || model.startsWith("A920") ||
                model.startsWith("A930") || model.startsWith("A800") ||
                model.startsWith("A35")) {
            Log.i(TAG, "Detected PAX terminal: " + model);
            return new PaxPrinterAdapter(context);
        }

        // Gertec / GPOS
        // if ("GPOS700".equals(model) || "GPOS720".equals(model)) {
        //     return new GertecPrinterAdapter(context);
        // }

        // POSMP
        // if ("APOS A8OVS".equals(model) || "DX8000".equals(model)) {
        //     return new PosmpPrinterAdapter(context);
        // }

        Log.w(TAG, "No compatible printer found for model: " + model);
        return null;
    }
}
