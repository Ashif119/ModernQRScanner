# QR Scanner App — MVVM + Clean Architecture

## Architecture Overview
This project follows Clean Architecture with 3 layers:

### 1. Data Layer (`data/`)
- Room Database (local storage)
- Repository Implementation
- Data Models + Mappers

### 2. Domain Layer (`domain/`)
- Business Logic (UseCases)
- Repository Interface
- Domain Models (pure Kotlin, no Android)

### 3. Presentation Layer (`presentation/`)
- ViewModels (MVVM)
- UI Screens
- UI State classes

## Tech Stack
| Technology | Purpose |
|---|---|
| Kotlin | Primary language |
| MVVM | Presentation pattern |
| Clean Architecture | Project structure |
| Room | Local database |
| Hilt | Dependency Injection |
| Coroutines + Flow | Async operations |
| CameraX | Camera handling |
| ML Kit | QR code detection |

## Package Structure
com.itandcstech.modernqrscanner/
│
├── data/
│   ├── local/          # Room DB, DAO, Entity
│   ├── model/          # Mappers (Entity ↔ Domain)
│   └── repository/     # Repository Implementation
│
├── domain/
│   ├── model/          # Pure data classes
│   ├── repository/     # Repository Interface
│   └── usecase/        # One class = One business action
│
└── presentation/
├── splash/          # Splash Screen + ViewModel
├── home/            # Home Screen + ViewModel
└── camera/          # Camera Screen + ViewModel
## Data Flow
UI (Screen)
↕
ViewModel
↕
UseCase          ← Business logic lives here
↕
Repository Interface
↕
Repository Implementation
↕
Room Database

## Key Concepts

### Why UseCases?
Each UseCase does ONE thing only.
- `GetAllScansUseCase` → only fetches scan history
- `SaveQRResultUseCase` → only saves a new scan
- `DeleteQRResultUseCase` → only deletes a scan

### Why Repository Interface in Domain?
Domain layer has NO Android dependencies.
The interface is in domain, implementation is in data.
This means we can swap Room for any other DB without touching domain or UI.

### Why Mappers?
- `QRResultEntity` → Room understands this (has @Entity annotation)
- `QRResult` → Pure Kotlin, domain layer uses this
- Mapper converts between the two so layers stay independent

## Screens
1. **Splash Screen** → App intro, navigate to Home
2. **Home Screen** → Shows scan history, tap to scan
3. **Camera Screen** → Opens camera, detects QR code

## Project Setup

### Requirements
- Android Studio Hedgehog or newer
- Min SDK: 24
- Target SDK: 34
- Kotlin: 1.9.22

### How to Run
1. Clone the repo
2. Open in Android Studio
3. Sync Gradle
4. Run on emulator or device (API 24+)

### Application Class
`QRScannerApp` is the entry point annotated with `@HiltAndroidApp`.
This initializes Hilt's dependency injection graph across the entire app.

## Domain Layer

### Model
`QRResult` - Pure Kotlin data class. No Android dependencies.
- `id` - Auto generated unique ID
- `content` - Actual QR code data (URL, text etc)
- `type` - Type of QR content (URL, TEXT, EMAIL)
- `timestamp` - When the QR was scanned (epoch millis)

### Repository Interface
`QRRepository` defines 3 operations:
- `getAllScans()` - Returns Flow of scan history (real-time updates)
- `saveQRResult()` - Saves a new scan
- `deleteQRResult()` - Deletes a scan by ID

### UseCases
Each UseCase does ONE thing only (Single Responsibility):
- `GetAllScansUseCase` - Fetches all scans via Flow
- `SaveQRResultUseCase` - Saves a QR result to DB
- `DeleteQRResultUseCase` - Deletes a QR result by ID

### Why `operator fun invoke()`?
Allows calling UseCase like a function:
```kotlin
// Without invoke
getAllScansUseCase.invoke()

// With invoke — cleaner!
getAllScansUseCase()
```

## Data Layer

### Room Database
`QRScannerDatabase` - Room database with version 1
- Single table: `qr_results`
- Accessed via `QRResultDao`

### Entity
`QRResultEntity` - Maps directly to `qr_results` table
- `@PrimaryKey(autoGenerate = true)` - Auto increments ID
- Same fields as domain model but with Room annotations

### DAO (Data Access Object)
`QRResultDao` - Database operations:
- `getAllScans()` - Returns Flow (live updates on DB change)
- `insertQRResult()` - Insert with REPLACE conflict strategy
- `deleteQRResult()` - Delete by ID

### Mapper
Extension functions to convert between layers:
- `QRResultEntity.toDomain()` - For reading from DB
- `QRResult.toEntity()` - For writing to DB

### Repository Implementation
`QRRepositoryImpl` - Implements `QRRepository` interface:
- Uses mapper to convert Entity ↔ Domain
- Injected by Hilt via constructor injection

AppModule
│
├── provideDatabase()     → QRScannerDatabase  ──┐
│                                                 │
├── provideQRResultDao()  ← QRScannerDatabase  ──┘
│        └──→ QRResultDao  ──┐
│                            │
└── provideQRRepository() ← QRResultDao
└──→ QRRepository (actually QRRepositoryImpl)
│
↓
UseCases (@Inject constructor)
│
↓
ViewModel (@Inject constructor)
│
↓
UI Screen

## Dependency Injection (Hilt)

### AppModule
Provides all app-level dependencies as Singletons:

| Provider | Returns | Depends On |
|---|---|---|
| `provideDatabase()` | `QRScannerDatabase` | `Context` |
| `provideQRResultDao()` | `QRResultDao` | `QRScannerDatabase` |
| `provideQRRepository()` | `QRRepository` | `QRResultDao` |

### Key Annotations
- `@Module` - Marks class as Hilt module
- `@InstallIn(SingletonComponent)` - Lives as long as app
- `@Provides` - Tells Hilt how to create an object
- `@Singleton` - Only one instance throughout app
- `@AndroidEntryPoint` - Required on Activity/Fragment
- `@HiltViewModel` - Required on ViewModel (next step)

### Dependency Graph
```
Context
  └── QRScannerDatabase
        └── QRResultDao
              └── QRRepositoryImpl (as QRRepository)
                    └── UseCases
                          └── ViewModel
                                └── UI
```

## Presentation Layer

### UI State Pattern
Each screen has its own UiState data class:
- `HomeUiState` - Manages scan history list, loading, error
- `CameraUiState` - Manages scanning status, detected QR content

### ViewModels

#### HomeViewModel
- Loads scan history via `GetAllScansUseCase` on init
- Exposes `uiState` as `StateFlow` (UI observes this)
- Functions: `saveQRResult()`, `deleteQRResult()`, `clearError()`

#### CameraViewModel
- Handles QR detection via `onQRCodeDetected()`
- Auto-detects QR type (URL, EMAIL, PHONE, WI-FI, TEXT)
- Auto-saves result to DB via `SaveQRResultUseCase`
- `resetScanner()` to scan again

### StateFlow vs LiveData
We use StateFlow because:
- Kotlin native (no Android dependency)
- Always has a value (no null issues)
- Works perfectly with Compose

## Navigation

### Setup
Single Activity architecture using Jetpack Navigation Compose.

### Routes
| Screen | Route | Description |
|---|---|---|
| Splash | "splash" | App entry point, 2s delay |
| Home | "home" | Scan history list |
| Camera | "camera" | QR code scanner |

### Flow

Splash (2s)
↓
Home Screen ←→ Camera Screen

### Key Concepts
- `Screen` sealed class — type-safe routes, no typos
- `NavGraph` — all routes registered in one place
- `popUpTo(inclusive = true)` — removes Splash from back stack
  so user cannot go back to Splash from Home
- `rememberNavController()` — survives recomposition

## Screens

### Splash Screen
- Dark navy background (`#1A1A2E`)
- Fade-in animation (1000ms)
- Auto navigates to Home after 2 seconds
- Removed from back stack after navigation
  (user cannot go back to splash)

### Key Concepts Used
- `LaunchedEffect` - Runs once when composable enters composition
- `animateFloatAsState` - Smooth alpha animation
- `remember + mutableStateOf` - Local UI state

### Camera Screen
- Full screen CameraX preview
- Dark overlay with green corner brackets
- Real-time QR detection via ML Kit
- Auto-saves result to Room DB
- Result bottom sheet with:
  - QR type badge
  - Full content display
  - Copy to clipboard
  - Scan again button
- Torch toggle support