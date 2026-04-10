package com.guilherme.pos_printer.printers.positivo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.guilherme.pos_printer.core.IPrinterAdapter;
import com.guilherme.pos_printer.core.PrinterAdapterCallback;
import com.xcheng.printerservice.IPrinterCallback;
import com.xcheng.printerservice.IPrinterService;

/**
 * Printer adapter for Positivo terminals (L3, L400).
 *
 * Uses the Positivo SDK (positivo.aar) which communicates with the
 * printer via {@link IPrinterService}.
 */
public class PositivoPrinterAdapter implements IPrinterAdapter {

    private static final String TAG = "PositivoPrinterAdapter";

    private final Context context;
    private IPrinterService printerService;
    private boolean isConnected = false;

    /**
     * Dummy callback for internal operations.
     */
    private final IPrinterCallback dummyCallback = new IPrinterCallback.Stub() {
        @Override
        public void onException(int code, String msg) {
            Log.w(TAG, "Callback exception: code=" + code + ", msg=" + msg);
        }

        @Override
        public void onLength(long current, long total) throws RemoteException {
        }

        @Override
        public void onComplete() throws RemoteException {
            Log.d(TAG, "Callback complete");
        }
    };

    private ServiceConnection serviceConnection;

    public PositivoPrinterAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void init(PrinterAdapterCallback callback) {
        try {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    Log.i(TAG, "Printer service connected");
                    printerService = IPrinterService.Stub.asInterface(service);
                    isConnected = true;

                    try {
                        printerService.printerInit(dummyCallback);
                    } catch (RemoteException e) {
                        Log.e(TAG, "printerInit failed", e);
                    }

                    callback.onSuccess();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Log.w(TAG, "Printer service disconnected");
                    printerService = null;
                    isConnected = false;
                }
            };

            Intent intent = new Intent("com.xcheng.printerservice.IPrinterService");
            intent.setPackage("com.xcheng.printerservice");

            boolean bound = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            if (!bound) {
                Log.e(TAG, "Could not bind to printer service");
                callback.onError(161, "Could not bind to Positivo printer service. " +
                        "Make sure this is a Positivo terminal (L3 / L400).");
            }
        } catch (Exception e) {
            Log.e(TAG, "init() exception", e);
            callback.onError(162, e.getMessage());
        }
    }

    @Override
    public void printBitmap(Bitmap bitmap, PrinterAdapterCallback callback) {
        if (!isConnected || printerService == null) {
            callback.onError(161, "Printer not initialized");
            return;
        }

        try {
            printerService.printBitmap(bitmap, new IPrinterCallback.Stub() {
                @Override
                public void onException(int code, String msg) {
                    Log.e(TAG, "printBitmap error: code=" + code + ", msg=" + msg);
                    callback.onError(code, msg);
                }

                @Override
                public void onLength(long current, long total) throws RemoteException {
                }

                @Override
                public void onComplete() throws RemoteException {
                    Log.i(TAG, "printBitmap complete");
                    callback.onSuccess();
                }
            });
        } catch (RemoteException e) {
            Log.e(TAG, "printBitmap exception", e);
            callback.onError(162, e.getMessage());
        }
    }

    @Override
    public void printText(String text, PrinterAdapterCallback callback) {
        if (!isConnected || printerService == null) {
            callback.onError(161, "Printer not initialized");
            return;
        }

        try {
            printerService.printText(text, new IPrinterCallback.Stub() {
                @Override
                public void onException(int code, String msg) {
                    Log.e(TAG, "printText error: code=" + code + ", msg=" + msg);
                    callback.onError(code, msg);
                }

                @Override
                public void onLength(long current, long total) throws RemoteException {
                }

                @Override
                public void onComplete() throws RemoteException {
                    Log.i(TAG, "printText complete");
                    callback.onSuccess();
                }
            });
        } catch (RemoteException e) {
            Log.e(TAG, "printText exception", e);
            callback.onError(162, e.getMessage());
        }
    }

    @Override
    public void feedPaper(int lines) {
        if (!isConnected || printerService == null) {
            return;
        }

        try {
            printerService.printWrapPaper(lines, dummyCallback);
        } catch (RemoteException e) {
            Log.e(TAG, "feedPaper exception", e);
        }
    }

    @Override
    public String getStatus() {
        if (!isConnected || printerService == null) {
            return "NOT_INITIALIZED";
        }

        try {
            boolean hasPaper = printerService.printerPaper(dummyCallback);
            if (!hasPaper) {
                return "OUT_OF_PAPER";
            }
        } catch (RemoteException e) {
            Log.e(TAG, "getStatus exception", e);
        }

        return "READY";
    }

    @Override
    public String getBrand() {
        return "POSITIVO";
    }
}
