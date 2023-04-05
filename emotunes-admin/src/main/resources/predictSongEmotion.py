import sys

def predictEmotion(modelId, songUrl):
    return "HAPPY" # todo prediction algorithm

if __name__ == "__main__":
    modelId = sys.argv[1]
    songUrl = sys.argv[2]
    result = predictEmotion(modelId, songUrl)
    print(result)
