#!/bin/bash
set -e

echo "📥 Téléchargement ou chargement du modèle..."
/app/venv/bin/python /app/telemodel.py

echo "🚀 Lancement de l'application Ktor..."
exec java -jar /app/Detia.jar