# Guide de Débogage - Application Crash/ANR

## 🔍 Méthode 1 : Capturer les Logs en Temps Réel

### Prérequis
1. Activer le **débogage USB** sur votre appareil Android
2. Connecter l'appareil à votre ordinateur via USB

### Étapes de Débogage

#### 1. Vérifier la Connexion
```bash
adb devices
```
Vous devriez voir votre appareil listé.

#### 2. Capturer Tous les Logs
```bash
# Effacer les anciens logs
adb logcat -c

# Démarrer la capture des logs
adb logcat > crash_logs.txt
```

#### 3. Reproduire le Crash
- Ouvrir l'application
- Effectuer l'action qui cause le crash
- Attendre que l'erreur "L'application ne répond pas" apparaisse

#### 4. Arrêter la Capture
- Appuyez sur `Ctrl+C` pour arrêter la capture
- Les logs seront dans le fichier `crash_logs.txt`

#### 5. Filtrer les Erreurs Importantes
```bash
# Voir uniquement les erreurs et crashs
adb logcat *:E

# Filtrer par package de l'app
adb logcat | grep "io.focuslauncher"

# Chercher les ANR (Application Not Responding)
adb logcat | grep -i "anr"
```

## 🔍 Méthode 2 : Récupérer les Logs Après Crash

Si l'appareil n'est plus connecté :

```bash
# Se connecter en ADB
adb connect <IP_ADDRESS>:5555

# Récupérer les derniers logs
adb logcat -d > crash_logs.txt

# Récupérer les ANR traces
adb pull /data/anr/traces.txt anr_traces.txt
```

## 🔍 Méthode 3 : Utiliser Android Studio

1. Ouvrir Android Studio
2. Menu: **View > Tool Windows > Logcat**
3. Sélectionner votre appareil
4. Filtrer par package: `io.focuslauncher`
5. Niveau de log: **Error** ou **Warn**
6. Reproduire le crash et observer les logs

## 📊 Analyser les Logs

### Chercher les Patterns Suivants

#### 1. Crash Exception (FATAL EXCEPTION)
```
E/AndroidRuntime: FATAL EXCEPTION: main
    Process: io.focuslauncher, PID: 12345
    java.lang.NullPointerException: ...
```

#### 2. ANR (Application Not Responding)
```
I/Process: Sending signal. PID: 12345 SIG: 9
E/ActivityManager: ANR in io.focuslauncher
```

#### 3. OutOfMemoryError
```
E/AndroidRuntime: java.lang.OutOfMemoryError: Failed to allocate...
```

#### 4. Database Errors
```
E/SQLiteDatabase: Error inserting/querying...
E/GreenDao: ...
```

## 🛠️ Causes Communes et Solutions

### 1. ANR - Opérations Longues sur UI Thread
**Symptôme:** "L'application ne répond pas" après 5 secondes

**Causes Potentielles:**
- Requêtes base de données sur le thread principal
- Opérations réseau synchrones
- Boucles infinies
- Chargement de grosses images

**Solution:**
- Utiliser AsyncTask, Coroutines, ou Threads
- Déplacer les opérations lourdes en arrière-plan

### 2. NullPointerException
**Symptôme:** Crash immédiat

**Causes:**
- Accès à un objet null
- Vue non initialisée
- Contexte null

**Solution:**
- Ajouter des vérifications null
- Utiliser safe calls en Kotlin (`?.`)

### 3. Database Lock/Concurrency
**Symptôme:** Freeze ou crash aléatoire

**Causes:**
- Multiples threads accédant à GreenDAO
- Transactions non fermées

**Solution:**
- Utiliser un seul DaoSession
- Synchroniser les accès

## 📝 Commandes Utiles

### Informations sur le Crash
```bash
# Récupérer le dernier crash
adb logcat -d | grep -A 50 "FATAL EXCEPTION"

# ANR traces
adb shell cat /data/anr/traces.txt

# Memory info
adb shell dumpsys meminfo io.focuslauncher

# Process info
adb shell ps | grep focuslauncher
```

### Redémarrer l'Application
```bash
# Force stop
adb shell am force-stop io.focuslauncher

# Clear app data
adb shell pm clear io.focuslauncher

# Restart
adb shell am start -n io.focuslauncher/.phone.launcher.FakeLauncherActivity
```

## 🔧 Debug Build vs Release Build

Si le crash n'arrive qu'en release :
- Problème de ProGuard/R8 minification
- Vérifier `proguard-rules.pro`
- Désactiver temporairement minification pour tester

## 📱 Tester sur Émulateur

Si vous n'avez pas d'appareil physique :
```bash
# Lister les émulateurs
emulator -list-avds

# Démarrer un émulateur
emulator -avd <AVD_NAME>

# Installer l'APK
adb install /path/to/launcher3-alpha-debug.apk
```

## 🎯 Prochaines Étapes

1. Capturer les logs avec une des méthodes ci-dessus
2. Chercher les messages d'erreur (FATAL EXCEPTION, ANR)
3. Partager les logs pour analyse détaillée
4. Identifier la ligne de code qui cause le problème
5. Appliquer le fix approprié
