# Ignite Pay Android

Standalone Android payments app for the Spark Stack International ecosystem.

## Current milestone: 0.2.0

The first frontend foundation is intentionally test-only: it contains no real payment processing and displays no fabricated real balance.

Included:
- Jetpack Compose + Material 3
- Ignite Pay dark financial UI
- Home dashboard
- Transactions
- Wallet
- Profile and security entry point
- Explicit TEST MODE state

## Target architecture

Android App → Ignite Pay API → Spark Core → Payment providers → Ledger / reconciliation / webhooks

The Android app will remain independent from the backend so payment infrastructure can evolve safely.

## Build

Open this repository in Android Studio and sync the Gradle project. The project targets Android API 36, uses AGP 9.4.0, Kotlin 2.4.10 and Gradle 9.6+.

Real authentication, wallet balances, payment providers, ledgering, webhooks and reconciliation are deliberately not connected yet.
