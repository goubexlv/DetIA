import sys
from PIL import Image
import torch
from torchvision import transforms
from efficientnet_pytorch import EfficientNet

# Charger le modèle avec 1000 classes (comme dans le fichier .pth)
model = EfficientNet.from_name('efficientnet-b0')
model.load_state_dict(torch.load('./modelIA/efficientnet-b0-355c32eb.pth'))

# Remplacer la dernière couche par une adaptée à 2 classes
model._fc = torch.nn.Linear(model._fc.in_features, 2)

# Mettre le modèle en mode évaluation
model.eval()
def predict(image_path):
    image = Image.open(image_path).convert('RGB')
    preprocess = transforms.Compose([
        transforms.Resize(224),
        transforms.CenterCrop(224),
        transforms.ToTensor(),
    ])
    img_tensor = preprocess(image).unsqueeze(0)
    with torch.no_grad():
        outputs = model(img_tensor)
        probs = torch.softmax(outputs, dim=1)
        ai_prob = probs[0][1].item()
        return ai_prob

if __name__ == "__main__":
    path = sys.argv[1]
    score = predict(path)
    print(score)
