/// Supported POS terminal printer brands.
enum PrinterBrand {
  /// Positivo terminals (L3, L400).
  positivo('POSITIVO'),

  /// PAX terminals (A910, etc.) — coming soon.
  pax('PAX'),

  /// Gertec / GPOS terminals (GPOS700, GPOS720, etc.) — coming soon.
  gertec('GERTEC'),

  /// POSMP terminals (APOS A8OVS, DX8000, etc.) — coming soon.
  posmp('POSMP'),

  /// Ingenico / SafraPay terminals — coming soon.
  ingenico('INGENICO'),

  /// Automatic detection based on device model.
  auto('AUTO');

  const PrinterBrand(this.value);

  /// The string value sent to the native platform.
  final String value;
}
