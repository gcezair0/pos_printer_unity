/// Represents the current status of the printer.
enum PrinterStatus {
  /// Printer is ready to print.
  ready,

  /// Printer is currently busy.
  busy,

  /// Printer is out of paper.
  outOfPaper,

  /// Printer is overheating.
  overheat,

  /// Printer voltage is too low.
  lowVoltage,

  /// Printer is not initialized / not connected.
  notInitialized,

  /// Printer encountered an unknown error.
  error,

  /// Status is unknown.
  unknown;

  /// Parse a status string from the native platform.
  static PrinterStatus fromString(String? status) {
    switch (status) {
      case 'READY':
        return PrinterStatus.ready;
      case 'BUSY':
        return PrinterStatus.busy;
      case 'OUT_OF_PAPER':
        return PrinterStatus.outOfPaper;
      case 'OVERHEAT':
        return PrinterStatus.overheat;
      case 'LOW_VOLTAGE':
        return PrinterStatus.lowVoltage;
      case 'NOT_INITIALIZED':
        return PrinterStatus.notInitialized;
      case 'ERROR':
        return PrinterStatus.error;
      default:
        return PrinterStatus.unknown;
    }
  }
}
