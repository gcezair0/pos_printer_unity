package com.guilherme.pos_printer.core;

/**
 * Callback interface for printer operations.
 */
public interface PrinterAdapterCallback {

    /**
     * Called when the operation completes successfully.
     */
    void onSuccess();

    /**
     * Called when the operation fails.
     *
     * @param code    an error code from the vendor SDK.
     * @param message a human-readable error message.
     */
    void onError(int code, String message);
}
