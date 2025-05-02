import sys
import torch
from transformers import AutoTokenizer, AutoModelForSequenceClassification

# Charger le modèle une fois
model_path = "./modelIA/local-model"

tokenizer = AutoTokenizer.from_pretrained(model_path)
model = AutoModelForSequenceClassification.from_pretrained(model_path)
model.eval()

def predict(text):
    inputs = tokenizer(text, return_tensors="pt", truncation=True, max_length=512)
    with torch.no_grad():
        outputs = model(**inputs)
        logits = outputs.logits
        probabilities = torch.softmax(logits, dim=1)
        ai_score = probabilities[0][1].item()
        return ai_score

if __name__ == "__main__":
    input_text = sys.stdin.read()
    score = predict(input_text)
    print(score)
