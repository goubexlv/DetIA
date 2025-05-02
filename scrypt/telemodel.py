from transformers import AutoTokenizer, AutoModelForSequenceClassification

model_name = "roberta-base-openai-detector"
save_path = "/app/modelIA/local-model"

# Téléchargement
tokenizer = AutoTokenizer.from_pretrained(model_name)
model = AutoModelForSequenceClassification.from_pretrained(model_name)

# Sauvegarde
tokenizer.save_pretrained(save_path)
model.save_pretrained(save_path)

