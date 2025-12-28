#!/bin/bash

echo "=== Focus Launcher - Script de Debug Crash ==="
echo ""

# Vérifier si ADB est disponible
if ! command -v adb &> /dev/null; then
    echo "❌ ADB n'est pas installé ou pas dans le PATH"
    exit 1
fi

# Vérifier si un appareil est connecté
DEVICE_COUNT=$(adb devices | grep -v "List" | grep "device$" | wc -l)
if [ "$DEVICE_COUNT" -eq 0 ]; then
    echo "❌ Aucun appareil Android connecté"
    echo "   Connectez votre appareil et activez le débogage USB"
    exit 1
fi

echo "✅ Appareil connecté: $(adb devices | grep device$ | awk '{print $1}')"
echo ""

# Menu
echo "Choisissez une option:"
echo "1) Capturer les logs en temps réel"
echo "2) Récupérer les derniers logs de crash"
echo "3) Supprimer la base de données et réinstaller"
echo "4) Voir les informations de l'app"
echo "5) Redémarrer l'app avec logs"
echo ""
read -p "Votre choix (1-5): " choice

case $choice in
    1)
        echo ""
        echo "📝 Capture des logs en temps réel..."
        echo "   Appuyez sur Ctrl+C pour arrêter"
        echo "   Les logs seront sauvegardés dans crash_realtime.log"
        echo ""
        adb logcat | tee crash_realtime.log
        ;;
    
    2)
        echo ""
        echo "📝 Récupération des derniers logs..."
        adb logcat -d > crash_last.log
        echo "✅ Logs sauvegardés dans crash_last.log"
        echo ""
        echo "Recherche de crashes..."
        grep -n "FATAL EXCEPTION" crash_last.log | head -5
        echo ""
        echo "Recherche d'ANR..."
        grep -n "ANR in" crash_last.log | head -5
        ;;
    
    3)
        echo ""
        echo "🗑️  Suppression de la base de données..."
        adb shell run-as io.focuslauncher rm -rf /data/data/io.focuslauncher/databases/noti-db* 2>/dev/null
        if [ $? -eq 0 ]; then
            echo "✅ Base de données supprimée"
        else
            echo "⚠️  Impossible de supprimer (app peut-être pas installée)"
        fi
        
        echo ""
        echo "🔄 Réinstallation de l'app..."
        APK_PATH="MiniumApps/launcher3/build/outputs/apk/alpha/debug/launcher3-alpha-debug.apk"
        if [ -f "$APK_PATH" ]; then
            adb install -r "$APK_PATH"
            echo "✅ App réinstallée"
        else
            echo "❌ APK non trouvé: $APK_PATH"
            echo "   Buildez l'app d'abord: cd MiniumApps && ./gradlew assembleAlphaDebug"
        fi
        ;;
    
    4)
        echo ""
        echo "📊 Informations sur l'app:"
        echo ""
        echo "--- Packages installés ---"
        adb shell pm list packages | grep focus
        echo ""
        echo "--- Taille de la base de données ---"
        adb shell run-as io.focuslauncher du -h /data/data/io.focuslauncher/databases/ 2>/dev/null || echo "Pas de base de données"
        echo ""
        echo "--- Mémoire utilisée ---"
        adb shell dumpsys meminfo io.focuslauncher | head -20
        ;;
    
    5)
        echo ""
        echo "🔄 Redémarrage de l'app avec capture de logs..."
        echo ""
        echo "Arrêt de l'app..."
        adb shell am force-stop io.focuslauncher
        sleep 1
        
        echo "Effacement des anciens logs..."
        adb logcat -c
        
        echo "Démarrage de l'app..."
        adb shell am start -n io.focuslauncher/.phone.launcher.FakeLauncherActivity
        
        echo ""
        echo "📝 Logs en cours (Ctrl+C pour arrêter):"
        echo ""
        adb logcat | grep --line-buffered -E "focuslauncher|GreenDao|Database|FATAL|ANR"
        ;;
    
    *)
        echo "❌ Choix invalide"
        exit 1
        ;;
esac

echo ""
echo "✅ Terminé"
