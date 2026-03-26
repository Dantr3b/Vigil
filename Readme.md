<div align="center">

```
██╗   ██╗██╗ ██████╗ ██╗██╗
██║   ██║██║██╔════╝ ██║██║
██║   ██║██║██║  ███╗██║██║
╚██╗ ██╔╝██║██║   ██║██║██║
 ╚████╔╝ ██║╚██████╔╝██║███████╗
  ╚═══╝  ╚═╝ ╚═════╝ ╚═╝╚══════╝
```

**Le garde silencieux de ton téléphone.**

*Pose un tag NFC. Le reste disparaît.*

![Android](https://img.shields.io/badge/Android-12%2B-0E0E0E?style=flat-square&logo=android&logoColor=3DDC84)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0%2B-0E0E0E?style=flat-square&logo=kotlin&logoColor=7F52FF)
![Compose](https://img.shields.io/badge/Jetpack_Compose-latest-0E0E0E?style=flat-square&logo=jetpackcompose&logoColor=4285F4)
![License](https://img.shields.io/badge/license-MIT-0E0E0E?style=flat-square)

</div>

---

## Concept

Vigil est un bloqueur d'applications physique pour Android. Pas de timer. Pas de mot de passe. Un objet réel dans le monde réel.

Tu poses un **tag NFC** sur ton bureau → les apps distrayantes disparaissent.  
Tu reprends le tag → elles reviennent.  
Tu perds le tag → tu désinstalles l'app. C'est le contrat.

Chaque tag correspond à un **profil** avec sa propre liste d'apps bloquées. Bureau, chambre, voiture — chaque contexte a ses règles.

---

## Fonctionnalités

- **Multi-profils NFC** — un tag par contexte de vie, profils illimités
- **Un seul profil actif à la fois** — scanner un nouveau tag remplace l'ancien
- **Blocage des apps et navigateurs** — Chrome, Firefox, Samsung Internet inclus
- **Liste figée en mode actif** — impossible de modifier les règles pendant une session
- **Dashboard de résistance** — statistiques globales des tentatives de triche
- **Survie au redémarrage** — le profil actif persiste après extinction du téléphone
- **Anti-désinstallation** — Device Admin activé tant qu'un profil est actif

---

## Stack technique

| Composant | Choix |
|-----------|-------|
| Langage | Kotlin 2.0+ |
| UI | Jetpack Compose |
| Base de données | Room + DataStore |
| Injection de dépendances | Hilt |
| NFC | UID natif Android |
| minSDK | 31 (Android 12 — Samsung S21+) |

---

## Architecture

Clean Architecture simplifiée en trois couches.

```
app/
├── data/                       # Infrastructure
│   ├── nfc/                    # Lecture UID, NfcRepository
│   ├── db/                     # Room — ProfileDao, BlockedAppDao, CheatLogDao
│   └── datastore/              # État du profil actif
│
├── domain/                     # Logique métier
│   ├── BrickManager.kt         # Toggle profils, logique NFC
│   ├── AppWatcher.kt           # AccessibilityService
│   └── CheatLogger.kt          # Enregistrement des tentatives
│
└── presentation/               # UI Jetpack Compose
    ├── onboarding/             # Guide permissions
    ├── profiles/               # Liste et config des profils
    ├── dashboard/              # Stats de résistance
    └── overlay/                # Shield — écran de blocage
```

### Schéma de base de données

```
Profile
├── id            UUID (PK)
├── name          String          -- "Bureau", "Chambre", "Voiture"…
├── nfc_uid       String (UNIQUE) -- UID du tag NFC
├── is_active     Boolean         -- un seul true à la fois
└── created_at    Long

BlockedApp
├── id            UUID (PK)
├── profile_id    UUID (FK → Profile)
└── package_name  String          -- ex: com.instagram.android

CheatLog
├── id            UUID (PK)
├── profile_id    UUID (FK → Profile)
├── package_name  String
└── attempted_at  Long
```

### Règle métier centrale

```kotlin
// Scan NFC reçu
when {
    uid in knownProfiles -> {
        if (profile.isActive) deactivate(profile)       // toggle OFF
        else activate(profile, deactivateOthers = true) // remplace l'ancien
    }
    appInForeground -> suggestProfileCreation(uid)      // tag inconnu
    else -> { /* silencieux */ }
}

// Blocage
if (launchedApp in activeProfile.blockedApps) showShield()
```

---

## Sécurité (anti-triche)

| Mécanisme | Comportement |
|-----------|-------------|
| `BroadcastReceiver` BOOT | Relance le service au démarrage du téléphone |
| Sticky Service | Android recrée l'`AccessibilityService` si tué par manque de RAM |
| Device Admin API | Désinstallation bloquée tant qu'au moins un profil est actif |
| Liste figée | Aucune modification possible pendant une session active |
| Blocage des recents | Impossible de swiper pour fermer l'Overlay |

**Mode secours (tag perdu) :**  
`Paramètres → Sécurité → Administrateurs de l'appareil → Révoquer Vigil → Désinstaller`

---

## Permissions requises

```xml
<uses-permission android:name="android.permission.NFC" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
<uses-permission android:name="android.permission.BIND_ACCESSIBILITY_SERVICE" />
<uses-permission android:name="android.permission.BIND_DEVICE_ADMIN" />
```

L'onboarding guide chaque permission dans l'ordre. Aucune session ne peut démarrer tant qu'une permission est manquante.

---

## Développement

### Prérequis

- Android Studio Hedgehog ou plus récent
- JDK 17+
- Un téléphone Android physique avec NFC (l'émulateur ne supporte pas le NFC)
- Un ou plusieurs tags NFC NTAG213 (ou compatible)

### Lancer le projet

```bash
git clone https://github.com/ton-user/vigil.git
cd vigil
./gradlew assembleDebug
```

### Ordre de développement des branches

```
feature/setup          → Hilt + Room (3 tables) + DataStore + Clean Arch
feature/nfc-logic      → Lecture UID, CRUD profils, logique toggle
feature/app-watcher    → AccessibilityService + détection apps
feature/overlay-ui     → Shield + sticky service + blocage recents
feature/persistence    → BroadcastReceiver boot + Device Admin
feature/dashboard      → Logs + stats + UI résistance
feature/web-blocking   → Blocage navigateurs
```

### Conventions de commit

```
feat(nfc): ajout de la gestion multi-profils
fix(overlay): correction du crash au redémarrage
style(dashboard): refonte de la carte de statistiques
refactor(domain): extraction du BrickManager
chore: mise à jour des dépendances Hilt
test(nfc): test unitaire du toggle multi-profils
docs: mise à jour du README
```

Branche `main` = code qui fonctionne uniquement.  
Tout le développement passe par des branches `feature/xxx`.

---

## Roadmap

- [x] Spécifications techniques v2.0
- [x] Identité visuelle (Stitch)
- [ ] `feature/setup` — socle Hilt + Room
- [ ] `feature/nfc-logic`
- [ ] `feature/app-watcher`
- [ ] `feature/overlay-ui`
- [ ] `feature/persistence`
- [ ] `feature/dashboard`
- [ ] `feature/web-blocking`
- [ ] Publication Play Store

---

## Licence

MIT — voir [`LICENSE`](LICENSE)

---

<div align="center">
<sub>Vigil ne te juge pas. Il veille, c'est tout.</sub>
</div>