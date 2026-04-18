## 0.0.3

* Added support for multiple new terminal models:
  - Clover (C305, C405)
  - Carbon (8, 10)
  - Ingenico (A8, DX8000, DX4000)
  - Gertec (GPOS700, GPOS720, GPOS760)
  - POSMP (N910, X990)
  - VSP / Elgin terminals
  - Sunmi (P2, P2-A11, D2 Mini)
  - TecToy (T8, T19)

* Improved auto-detection engine for device model matching.
* Expanded printer adapter system with new vendor support hooks.

## 0.0.2

* Added PAX terminal support (A910, A920, A920Pro, A930, A910S).
* PAX Neptune Lite SDK integration.
* Auto-detection for PAX device models.

## 0.0.1

* Initial release.
* Support for Positivo (L3, L400) terminal printers.
* Unified `PosPrinter` API for printing bitmaps.
* Factory pattern for automatic printer detection based on device model.
