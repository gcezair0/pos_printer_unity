import 'printer_status.dart';

/// The result of a print operation.
class PrintResult {
  /// Whether the print operation was successful.
  final bool success;

  /// An optional error message if the print failed.
  final String? errorMessage;

  /// An optional error code from the native SDK.
  final int? errorCode;

  /// The printer status after the operation.
  final PrinterStatus status;

  const PrintResult({
    required this.success,
    this.errorMessage,
    this.errorCode,
    this.status = PrinterStatus.unknown,
  });

  /// Create a successful result.
  factory PrintResult.ok() =>
      const PrintResult(success: true, status: PrinterStatus.ready);

  /// Create a failure result.
  factory PrintResult.failure({
    required String message,
    int? code,
    PrinterStatus status = PrinterStatus.error,
  }) =>
      PrintResult(
        success: false,
        errorMessage: message,
        errorCode: code,
        status: status,
      );

  /// Parse a result from the native platform map.
  factory PrintResult.fromMap(Map<dynamic, dynamic> map) {
    return PrintResult(
      success: map['success'] == true,
      errorMessage: map['errorMessage'] as String?,
      errorCode: map['errorCode'] as int?,
      status: PrinterStatus.fromString(map['status'] as String?),
    );
  }

  @override
  String toString() =>
      'PrintResult(success: $success, errorMessage: $errorMessage, '
      'errorCode: $errorCode, status: $status)';
}
