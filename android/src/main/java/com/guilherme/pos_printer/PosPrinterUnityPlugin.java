package com.guilherme.pos_printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.guilherme.pos_printer.core.IPrinterAdapter;
import com.guilherme.pos_printer.core.PrinterAdapterCallback;
import com.guilherme.pos_printer.factory.PrinterFactory;

import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * PosPrinterUnityPlugin — Flutter plugin entry point.
 *
 * Receives method calls from Dart and delegates to the appropriate
 * native printer adapter via {@link PrinterFactory}.
 */
public class PosPrinterUnityPlugin implements FlutterPlugin, MethodCallHandler {

    private static final String CHANNEL = "com.guilherme.pos_printer/methods";

    private MethodChannel channel;
    private Context applicationContext;
    private IPrinterAdapter printerAdapter;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        applicationContext = binding.getApplicationContext();
        channel = new MethodChannel(binding.getBinaryMessenger(), CHANNEL);
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        channel = null;
        applicationContext = null;
        printerAdapter = null;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        switch (call.method) {
            case "initPrinter":
                handleInitPrinter(call, result);
                break;
            case "getStatus":
                handleGetStatus(result);
                break;
            case "printBitmap":
                handlePrintBitmap(call, result);
                break;
            case "printText":
                handlePrintText(call, result);
                break;
            case "feedPaper":
                handleFeedPaper(call, result);
                break;
            case "getDetectedBrand":
                handleGetDetectedBrand(result);
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    // ─── Handlers ────────────────────────────────────────────────────

    private void handleInitPrinter(MethodCall call, Result result) {
        String brand = call.argument("brand");
        try {
            printerAdapter = PrinterFactory.create(applicationContext, brand);

            if (printerAdapter == null) {
                result.success(errorMap(161, "NOT_INITIALIZED",
                        "No compatible printer found for this device."));
                return;
            }

            printerAdapter.init(new PrinterAdapterCallback() {
                @Override
                public void onSuccess() {
                    result.success(successMap());
                }

                @Override
                public void onError(int code, String message) {
                    result.success(errorMap(code, "ERROR", message));
                }
            });
        } catch (Exception e) {
            result.success(errorMap(162, "ERROR", e.getMessage()));
        }
    }

    private void handleGetStatus(Result result) {
        if (printerAdapter == null) {
            result.success("NOT_INITIALIZED");
            return;
        }
        result.success(printerAdapter.getStatus());
    }

    private void handlePrintBitmap(MethodCall call, Result result) {
        if (printerAdapter == null) {
            result.success(errorMap(161, "NOT_INITIALIZED",
                    "Printer not initialized. Call initPrinter first."));
            return;
        }

        byte[] imageBytes = call.argument("imageBytes");
        if (imageBytes == null) {
            result.success(errorMap(162, "ERROR", "imageBytes argument is null"));
            return;
        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        if (bitmap == null) {
            result.success(errorMap(162, "ERROR", "Failed to decode image bytes into Bitmap"));
            return;
        }

        printerAdapter.printBitmap(bitmap, new PrinterAdapterCallback() {
            @Override
            public void onSuccess() {
                result.success(successMap());
            }

            @Override
            public void onError(int code, String message) {
                result.success(errorMap(code, "ERROR", message));
            }
        });
    }

    private void handlePrintText(MethodCall call, Result result) {
        if (printerAdapter == null) {
            result.success(errorMap(161, "NOT_INITIALIZED",
                    "Printer not initialized. Call initPrinter first."));
            return;
        }

        String text = call.argument("text");
        if (text == null) {
            result.success(errorMap(162, "ERROR", "text argument is null"));
            return;
        }

        printerAdapter.printText(text, new PrinterAdapterCallback() {
            @Override
            public void onSuccess() {
                result.success(successMap());
            }

            @Override
            public void onError(int code, String message) {
                result.success(errorMap(code, "ERROR", message));
            }
        });
    }

    private void handleFeedPaper(MethodCall call, Result result) {
        if (printerAdapter == null) {
            result.success(errorMap(161, "NOT_INITIALIZED",
                    "Printer not initialized. Call initPrinter first."));
            return;
        }

        Integer lines = call.argument("lines");
        printerAdapter.feedPaper(lines != null ? lines : 3);
        result.success(successMap());
    }

    private void handleGetDetectedBrand(Result result) {
        if (printerAdapter == null) {
            result.success(null);
            return;
        }
        result.success(printerAdapter.getBrand());
    }

    // ─── Helpers ─────────────────────────────────────────────────────

    private Map<String, Object> successMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("status", "READY");
        return map;
    }

    private Map<String, Object> errorMap(int code, String status, String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", false);
        map.put("errorCode", code);
        map.put("errorMessage", message);
        map.put("status", status);
        return map;
    }
}
