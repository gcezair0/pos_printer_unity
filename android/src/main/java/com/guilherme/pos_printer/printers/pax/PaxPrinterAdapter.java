package com.guilherme.pos_printer.printers.pax;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.guilherme.pos_printer.core.IPrinterAdapter;
import com.guilherme.pos_printer.core.PrinterAdapterCallback;
import com.pax.dal.IDAL;
import com.pax.dal.IPrinter;
import com.pax.dal.exceptions.PrinterDevException;
import com.pax.neptunelite.api.NeptuneLiteUser;

/**
 * Printer adapter for PAX terminals (A910, A920, etc.).
 *
 * Uses the PAX Neptune Lite SDK which communicates with the
 * printer via {@link IPrinter} obtained from {@link IDAL}.
 *
 * The native .so files required by the SDK (libDeviceConfig.so,
 * libDCL.so, libiconv.so, etc.) are bundled in the plugin's
 * jniLibs directory and are automatically installed by the
 * Android PackageManager to the app's private lib directory.
 */
public class PaxPrinterAdapter implements IPrinterAdapter {

    private static final String TAG = "PaxPrinterAdapter";

    private final Context context;
    private IDAL dal;
    private IPrinter printer;
    private boolean isInitialized = false;

    public PaxPrinterAdapter(Context context) {
        this.context = context;
    }

    /**
     * Gets the DAL (Device Abstraction Layer) singleton.
     * The native .so files are already in the app's lib directory
     * (installed via jniLibs), so getDal() can find them directly.
     */
    private IDAL getDal() {
        if (dal == null) {
            try {
                dal = NeptuneLiteUser.getInstance().getDal(context);
            } catch (Exception e) {
                Log.e(TAG, "Failed to get DAL instance", e);
            }
        }
        return dal;
    }

    @Override
    public void init(PrinterAdapterCallback callback) {
        try {
            IDAL dalInstance = getDal();
            if (dalInstance == null) {
                callback.onError(161, "Could not obtain PAX DAL instance. " +
                        "Make sure this is a PAX terminal (A910 / A920).");
                return;
            }

            printer = dalInstance.getPrinter();
            if (printer == null) {
                callback.onError(161, "Could not obtain PAX printer from DAL.");
                return;
            }

            printer.init();
            printer.setGray(3);
            isInitialized = true;

            Log.i(TAG, "PAX printer initialized successfully");
            callback.onSuccess();
        } catch (PrinterDevException e) {
            Log.e(TAG, "init() failed", e);
            callback.onError(162, "PAX printer init failed: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "init() exception", e);
            callback.onError(162, e.getMessage());
        }
    }

    @Override
    public void printBitmap(Bitmap bitmap, PrinterAdapterCallback callback) {
        if (!isInitialized || printer == null) {
            callback.onError(161, "Printer not initialized");
            return;
        }

        try {
            printer.printBitmap(bitmap);
            int result = printer.start();

            if (result == 0) {
                Log.i(TAG, "printBitmap complete");
                callback.onSuccess();
            } else {
                String errorMsg = statusCodeToString(result);
                Log.e(TAG, "printBitmap failed: " + errorMsg);
                callback.onError(result, errorMsg);
            }
        } catch (PrinterDevException e) {
            Log.e(TAG, "printBitmap exception", e);
            callback.onError(162, e.getMessage());
        }
    }

    @Override
    public void printText(String text, PrinterAdapterCallback callback) {
        if (!isInitialized || printer == null) {
            callback.onError(161, "Printer not initialized");
            return;
        }

        try {
            printer.init();
            printer.printStr(text, null);
            printer.step(30);
            int result = printer.start();

            if (result == 0) {
                Log.i(TAG, "printText complete");
                callback.onSuccess();
            } else {
                String errorMsg = statusCodeToString(result);
                Log.e(TAG, "printText failed: " + errorMsg);
                callback.onError(result, errorMsg);
            }
        } catch (PrinterDevException e) {
            Log.e(TAG, "printText exception", e);
            callback.onError(162, e.getMessage());
        }
    }

    @Override
    public void feedPaper(int lines) {
        if (!isInitialized || printer == null) {
            return;
        }

        try {
            printer.step(lines);
            printer.start();
        } catch (PrinterDevException e) {
            Log.e(TAG, "feedPaper exception", e);
        }
    }

    @Override
    public String getStatus() {
        if (!isInitialized || printer == null) {
            return "NOT_INITIALIZED";
        }

        try {
            int status = printer.getStatus();
            switch (status) {
                case 0:
                    return "READY";
                case 1:
                    return "BUSY";
                case 2:
                    return "OUT_OF_PAPER";
                case 3:
                    return "ERROR";
                case 4:
                    return "ERROR";
                case 8:
                    return "OVERHEAT";
                case 9:
                    return "LOW_VOLTAGE";
                default:
                    return "UNKNOWN";
            }
        } catch (PrinterDevException e) {
            Log.e(TAG, "getStatus exception", e);
            return "ERROR";
        }
    }

    @Override
    public String getBrand() {
        return "PAX";
    }

    /**
     * Translates PAX printer status codes to human-readable strings.
     */
    private String statusCodeToString(int status) {
        switch (status) {
            case 0:
                return "Success";
            case 1:
                return "Printer is busy";
            case 2:
                return "Out of paper";
            case 3:
                return "Print data format error";
            case 4:
                return "Printer malfunction";
            case 8:
                return "Printer overheated";
            case 9:
                return "Printer voltage too low";
            case 240:
                return "Printing unfinished";
            case 252:
                return "Font library not installed";
            case 254:
                return "Data package too long";
            default:
                return "Unknown error (code " + status + ")";
        }
    }
}
