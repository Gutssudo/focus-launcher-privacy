# Analyse du Crash - Focus Launcher

## 🔍 Problèmes Identifiés

### 1. ⚠️ CRITIQUE : Initialisation Base de Données sur Thread Principal

**Fichier:** `Launcher3App.java:77-80`

```java
@Override
public void onCreate() {
    super.onCreate();
    // ... 
    GreenDaoOpenHelper helper2 = new GreenDaoOpenHelper(this, "noti-db", null);
    Database db = helper2.getWritableDb();  // ⚠️ Opération bloquante!
    DaoMaster daoMaster = new DaoMaster(db);
    daoSession = daoMaster.newSession();
    // ...
}
```

**Pourquoi c'est un problème:**
- `getWritableDb()` s'exécute sur le thread principal
- Peut prendre plusieurs secondes si:
  - C'est la première installation (création des tables)
  - Une migration de schéma est nécessaire
  - La base de données est verrouillée
  - Le stockage est lent

**Conséquence:**
- ANR (Application Not Responding) après 5 secondes
- Message: "L'application ne répond pas"

### 2. ⚠️ Migration de Base de Données Synchrone

**Fichier:** `GreenDaoOpenHelper.java:15-29`

```java
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    switch (oldVersion) {
        case 1:
        case 2:
        case 3:
            MigrationHelper.migrate(db, TableNotificationSmsDao.class);  // ⚠️ Peut être lent
            break;
        case 4:
            break;
    }
}
```

**Problème:**
- La migration s'exécute de manière synchrone pendant `onCreate()`
- Si la table contient beaucoup de données, cela prend du temps

### 3. Accès Base de Données dans les Activities

Plusieurs activities accèdent directement à la base de données, potentiellement sur le thread principal:
- `DashboardActivity.java`
- `UpdateBackgroundActivity.java`
- `SuppressNotificationActivity.java`
- `CoreActivity.java`
- `NotificationActivity.java`

## 🛠️ Solutions Recommandées

### Solution 1 (Rapide): Désactiver StrictMode

**Fichier:** `Launcher3App.java`

Ajouter avant l'initialisation de la base de données:

```java
@Override
public void onCreate() {
    super.onCreate();
    
    // Désactiver StrictMode pour éviter les crashes en debug
    if (BuildConfig.DEBUG) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
            .permitAll()
            .build());
    }
    
    // ... reste du code
}
```

**Avantages:** Rapide à implémenter, permet de tester
**Inconvénients:** Ne résout pas le problème de fond

### Solution 2 (Recommandée): Initialiser la DB en Arrière-plan

**Fichier:** `Launcher3App.java`

```java
private DaoSession daoSession;
private final Object dbLock = new Object();

@Override
public void onCreate() {
    super.onCreate();
    
    // ... autres initialisations
    
    // Initialiser la DB en arrière-plan
    new Thread(() -> {
        GreenDaoOpenHelper helper2 = new GreenDaoOpenHelper(this, "noti-db", null);
        Database db = helper2.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        
        synchronized (dbLock) {
            daoSession = daoMaster.newSession();
        }
        
        Log.i(TAG, "Database initialized");
    }).start();
    
    // ... reste du code
}

public DaoSession getDaoSession() {
    synchronized (dbLock) {
        while (daoSession == null) {
            try {
                dbLock.wait(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        return daoSession;
    }
}
```

**Avantages:** Résout le problème ANR, pas de blocage UI
**Inconvénients:** Code plus complexe, nécessite synchronisation

### Solution 3 (Moderne): Utiliser Room au lieu de GreenDAO

Migration vers Room Database (architecture moderne Android):
- Support natif pour les opérations asynchrones
- Meilleure intégration avec coroutines Kotlin
- Plus maintenu que GreenDAO

**Note:** Ceci est un gros refactoring, à considérer pour le futur

### Solution 4 (Temporaire): Augmenter le Timeout

Dans `AndroidManifest.xml`, ajouter:

```xml
<application
    android:largeHeap="true"
    ...>
```

Cela donne plus de mémoire mais ne résout pas le problème de fond.

## 🔧 Fix Immédiat Recommandé

### Patch Minimal (5 minutes)

1. **Désactiver StrictMode en debug**
2. **Vérifier que la DB n'est pas corrompue**

```bash
# Sur l'appareil, supprimer la DB existante
adb shell run-as io.focuslauncher rm -rf /data/data/io.focuslauncher/databases/noti-db*

# Réinstaller l'app
adb install -r launcher3-alpha-debug.apk
```

3. **Tester avec logs:**

```bash
adb logcat | grep -E "GreenDao|Database|Launcher3App"
```

## 📊 Vérifications à Faire

### 1. Vérifier si la DB Existe
```bash
adb shell ls -lh /data/data/io.focuslauncher/databases/
```

### 2. Vérifier la Taille de la DB
```bash
adb shell du -h /data/data/io.focuslauncher/databases/noti-db
```

### 3. Vérifier les Logs de Migration
```bash
adb logcat -s GreenDao:* SQLiteDatabase:*
```

## 🎯 Plan d'Action

### Étape 1: Diagnostic (Maintenant)
1. Connecter l'appareil
2. Capturer les logs pendant le crash
3. Confirmer que c'est bien l'initialisation DB qui cause l'ANR

### Étape 2: Fix Temporaire (Court terme)
1. Appliquer Solution 1 (StrictMode)
2. Tester que l'app démarre
3. Capturer les logs pour voir si d'autres problèmes existent

### Étape 3: Fix Permanent (Moyen terme)
1. Implémenter Solution 2 (initialisation async)
2. Ajouter des indicateurs de chargement
3. Gérer le cas où la DB n'est pas encore prête

### Étape 4: Tests
1. Tester sur appareil neuf (première installation)
2. Tester avec migration (update de v1.0.2 → nouvelle version)
3. Tester avec DB corrompue
4. Tester sur Android 5.0 et Android 14

## 📝 Commandes de Debug Utiles

```bash
# Capturer tout pendant 30 secondes
timeout 30 adb logcat > crash_full.log

# Surveiller uniquement l'app
adb logcat | grep --line-buffered io.focuslauncher

# Surveiller la DB
adb logcat -s GreenDao SQLiteDatabase DaoMaster

# Réinstaller proprement
adb uninstall io.focuslauncher
adb install launcher3-alpha-debug.apk
```

## ⚡ Quick Fix à Essayer Maintenant

```bash
# 1. Supprimer l'app et ses données
adb shell pm clear io.focuslauncher

# 2. Désinstaller complètement
adb uninstall io.focuslauncher

# 3. Réinstaller
adb install /path/to/launcher3-alpha-debug.apk

# 4. Capturer les logs pendant le démarrage
adb logcat -c && adb logcat | tee startup.log
```

Puis ouvrir l'app et observer les logs.
