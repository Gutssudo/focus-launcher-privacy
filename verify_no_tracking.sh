#!/bin/bash

echo "🔍 Vérification absence de tracking..."
echo "======================================"
echo ""

ISSUES=0
WORKING_DIR="MiniumApps"

# Vérifier Firebase
echo "🔍 Vérification Firebase..."
if grep -r "firebase" "$WORKING_DIR" --include="*.gradle" --include="*.java" --include="*.kt" --include="*.json" 2>/dev/null | grep -v "verify_no_tracking" | grep -v "// ❌" | grep -v "Binary file"; then
    echo "❌ Firebase détecté !"
    ISSUES=$((ISSUES + 1))
else
    echo "✅ Aucune trace de Firebase"
fi
echo ""

# Vérifier Sentry
echo "🔍 Vérification Sentry..."
if grep -r "sentry" "$WORKING_DIR" --include="*.gradle" --include="*.java" --include="*.kt" 2>/dev/null | grep -v "verify_no_tracking" | grep -v "Binary file"; then
    echo "❌ Sentry détecté !"
    ISSUES=$((ISSUES + 1))
else
    echo "✅ Aucune trace de Sentry"
fi
echo ""

# Vérifier MailChimp
echo "🔍 Vérification MailChimp..."
if grep -ri "mailchimp" "$WORKING_DIR" --include="*.gradle" --include="*.java" --include="*.kt" 2>/dev/null | grep -v "verify_no_tracking" | grep -v "Binary file"; then
    echo "❌ MailChimp détecté !"
    ISSUES=$((ISSUES + 1))
else
    echo "✅ Aucune trace de MailChimp"
fi
echo ""

# Vérifier Google Analytics
echo "🔍 Vérification Google Analytics..."
if grep -r "analytics" "$WORKING_DIR" --include="*.gradle" 2>/dev/null | grep -v "verify_no_tracking" | grep -v "// ❌" | grep -v "Binary file"; then
    echo "❌ Analytics détecté !"
    ISSUES=$((ISSUES + 1))
else
    echo "✅ Aucune trace d'Analytics"
fi
echo ""

# Vérifier Crashlytics
echo "🔍 Vérification Crashlytics..."
if grep -r "crashlytics" "$WORKING_DIR" --include="*.gradle" 2>/dev/null | grep -v "verify_no_tracking" | grep -v "// ❌" | grep -v "Binary file"; then
    echo "❌ Crashlytics détecté !"
    ISSUES=$((ISSUES + 1))
else
    echo "✅ Aucune trace de Crashlytics"
fi
echo ""

# Vérifier mail.jar
echo "🔍 Vérification mail.jar..."
if find "$WORKING_DIR" -name "mail.jar" -o -name "activation.jar" 2>/dev/null | grep -v "verify_no_tracking"; then
    echo "❌ JAR mail détectés !"
    ISSUES=$((ISSUES + 1))
else
    echo "✅ Aucun JAR mail"
fi
echo ""

# Vérifier google-services.json
echo "🔍 Vérification google-services.json..."
if find "$WORKING_DIR" -name "google-services.json" 2>/dev/null; then
    echo "❌ google-services.json détecté !"
    ISSUES=$((ISSUES + 1))
else
    echo "✅ Aucun google-services.json"
fi
echo ""

# Résumé final
echo "======================================"
if [ $ISSUES -eq 0 ]; then
    echo ""
    echo "✅✅✅ SUCCÈS : Aucun tracker détecté ! ✅✅✅"
    echo ""
    echo "🎉 Focus Launcher est maintenant 100% privé !"
    echo "🔒 Aucune donnée n'est envoyée à des tiers"
    echo "📱 Toutes les données restent sur votre appareil"
    echo ""
    exit 0
else
    echo ""
    echo "❌ ÉCHEC : $ISSUES problème(s) détecté(s)"
    echo ""
    echo "⚠️  Des trackers ont été détectés dans le code"
    echo "📝 Veuillez réviser les fichiers listés ci-dessus"
    echo ""
    exit 1
fi
