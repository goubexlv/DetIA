# 🧠 DetIA — AI Content Detection API

DetIA est une API Kotlin multiplateforme construite avec [Ktor](https://ktor.io), permettant de **détecter si un texte ou une image a été généré(e) par une intelligence artificielle**.

Ce projet repose sur des modèles de machine learning locaux (ex: RoBERTa pour le texte, EfficientNet pour les images) et est conçu pour être facilement déployable, extensible et testé.

---

## ✨ Fonctionnalités

| Module                     | Description |
|----------------------------|-------------|
| 🔍 Détection de texte IA    | Analyse de texte pour déterminer s’il est généré par IA (RoBERTa / autres modèles Transformers). |
| 🖼️ Détection d’image IA    | Analyse d’image à l’aide de modèles CNN comme EfficientNet pour déterminer s’ils sont issus de générateurs IA. |
| 🧪 API RESTful             | Endpoints clairs pour envoyer du texte ou des images et recevoir une prédiction (IA / Réel + pourcentage). |
| 🐍 Intégration Python       | Utilise des scripts Python localement via `ProcessBuilder` pour exécuter les modèles ML. |
| 📦 Docker & Ktor           | Prêt pour la production avec build Docker, JAR exécutable, etc. |

---

## 🧱 Architecture simplifiée

```txt
[Client/API] ──> [Ktor Server (Kotlin)] ──> [Script Python] ──> [Model Local] ──> Résultat IA ou Réel
```
## 🚀 Lancement du projet

1. Clonage du repository
   Clonez le projet en utilisant Git :

```
https://github.com/goubexlv/DetIA.git
cd DetIA
```
2. Démarrage avec Docker
   Le projet est prêt à être utilisé avec Docker. Vous pouvez utiliser le Dockerfile inclus pour créer une image Docker et exécuter l'API.

Étapes pour démarrer avec Docker : 
   
1. Construire l'image Docker :

```
docker build -t detia .
```
2. Lancer l'API dans un conteneur Docker :

```
docker run -p 8080:8080 detia
```

L'API sera maintenant accessible sur http://localhost:8080.

## 📚 Documentation du modèle
Les modèles utilisés dans ce projet sont basés sur des architectures de machine learning courantes telles que RoBERTa (pour le texte) et EfficientNet (pour les images). Ces modèles peuvent être formés et adaptés à d'autres cas d'usage en fonction des besoins.

Les scripts Python associés à ces modèles se trouvent dans le dossier /scripts/.

## ⚖️ Licence
Ce projet est sous licence MIT. Consultez le fichier LICENSE pour plus d'informations.



# DetIA
# DetIA
