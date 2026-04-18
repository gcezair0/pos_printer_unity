package com.guilherme.pos_printer.printers.pomsp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import com.guilherme.pos_printer.core.IPrinterAdapter;
import com.guilherme.pos_printer.core.PrinterAdapterCallback;

import java.util.Objects;

import br.com.execucao.posmp_api.printer.Printer;
import br.com.execucao.posmp_api.printer.PrinterListener;

public class PomspPrinterAdapter implements IPrinterAdapter {

    private static final String TAG = "PomspPrinterAdapter";

    private final Context context;
    private Printer printer;
    private boolean isInitialized = false;

    public PomspPrinterAdapter(Context context) {
        this.context = context;
    }

private static final String[] SUPPORTED_MODELS = {
        Printer.MODELO_CLOVER_C305,
        Printer.MODELO_CLOVER_C405,

        Printer.MODELO_CARBON_8,
        Printer.MODELO_CARBON_10,

        Printer.MODELO_INGENICO_A8,
        Printer.MODELO_INGENICO_DX8000,
        Printer.MODELO_INGENICO_DX4000,

        Printer.MODELO_GERTEC_700,
        Printer.MODELO_GERTEC_720,
        Printer.MODELO_GERTEC_760,

        Printer.MODELO_N910,
        Printer.MODELO_X990,

        Printer.MODELO_VSP,

        Printer.MODELO_ELGIN,

        Printer.MODELO_SUNMI_P2,
        Printer.MODELO_SUNMI_P2A11,
        Printer.MODELO_SUNMI_D2MINI,

        Printer.MODELO_TECTOY_T8,
        Printer.MODELO_TECTOY_T19
};

public boolean isSupportedDevice() {
    String model = Build.MODEL;
    if (model == null) return false;

    for (String supported : SUPPORTED_MODELS) {
        if (model.equalsIgnoreCase(supported)) {
            return true;
        }
    }
    return false;
}

    @Override
    public void init(PrinterAdapterCallback callback) {
        try {
            if (!isSupportedDevice()) {
                callback.onError(161, "Unsupported device: " + Build.MODEL);
                return;
            }

            printer = Printer.getInstance(context);

            if (printer == null) {
                callback.onError(161, "Failed to initialize Pomsp printer");
                return;
            }

            isInitialized = true;
            Log.i(TAG, "Pomsp printer initialized successfully");
            callback.onSuccess();

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
            printer.print(bitmap, new PrinterListener() {
                @Override
                public void onFinish() {
                    Log.i(TAG, "printBitmap success");
                    callback.onSuccess();
                }

                @Override
                public void onError(int error) {
                    Log.e(TAG, "printBitmap error: " + error);
                    callback.onError(error, mapError(error));
                }

                @Override
                public android.os.IBinder asBinder() {
                    return null;
                }
            });

        } catch (Exception e) {
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
            printer.print(text, new PrinterListener() {
                @Override
                public void onFinish() {
                    Log.i(TAG, "printText success");
                    callback.onSuccess();
                }

                @Override
                public void onError(int error) {
                    Log.e(TAG, "printText error: " + error);
                    callback.onError(error, mapError(error));
                }

                @Override
                public android.os.IBinder asBinder() {
                    return null;
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "printText exception", e);
            callback.onError(162, e.getMessage());
        }
    }

    @Override
    public void feedPaper(int lines) {
        if (!isInitialized || printer == null) return;

        try {
            // Pomsp não tem step explícito → workaround:
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lines; i++) {
                sb.append("\n");
            }
            printer.print(sb.toString());
        } catch (Exception e) {
            Log.e(TAG, "feedPaper exception", e);
        }
    }

    @Override
    public String getStatus() {
        if (!isInitialized || printer == null) {
            return "NOT_INITIALIZED";
        }

        // SDK Pomsp é limitado → não expõe status detalhado
        return "READY";
    }

    @Override
    public String getBrand() {
        return "POMSP";
    }

    private String mapError(int error) {
        switch (error) {
            case Printer.ERRO_SEM_PAPEL:
                return "OUT_OF_PAPER";
            case Printer.ERRO_DESCONHECIDO:
                return "UNKNOWN_ERROR";
            default:
                return "ERROR_CODE_" + error;
        }
    }
}
