# Ignite Pay Android

Standalone Android payments app for the Spark Stack International ecosystem.

## Current milestone: 0.4.0

The first frontend foundation is intentionally test-only: it contains no real payment processing and displays no fabricated real balance.

Included:
- Jetpack Compose + Material 3
- Ignite Pay dark financial UI
- Home dashboard
- Transactions
- Wallet
- Profile and security entry point
- Explicit TEST MODE state
- Send money frontend flow
- Request money frontend flow
- Pay / merchant frontend flow
- Review, processing, success and test-reference states

## Target architecture

Android App → Ignite Pay API → Spark Core → Payment providers → Ledger / reconciliation / webhooks

The Android app will remain independent from the backend so payment infrastructure can evolve safely.

## Test-mode payment flows

The 0.4.0 payment journeys are UI simulations only. They do not authenticate users, debit wallets, call payment providers, or create real ledger entries.

## Build

Open this repository in Android Studio and sync the Gradle project. The project targets Android API 36, uses AGP 9.4.0, Kotlin 2.4.10 and Gradle 9.6+.

Real authentication, wallet balances, payment providers, ledgering, webhooks and reconciliation are deliberately not connected yet.
### 0.4.0 frontend progress
- Interactive transaction filters for All, Completed and Pending states.
- Added explicit pending demo ledger activity in test mode.
- Home “See all” now opens the Transactions tab.
- No real funds or live payment processing are enabled.
