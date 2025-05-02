#!/bin/sh
set -e

echo "📥 Téléchargement ou chargement du modèle..."
python3 /app/telemodel.py

echo "🚀 Lancement de l'application Ktor..."
exec java -jar /app/Detia.jar
