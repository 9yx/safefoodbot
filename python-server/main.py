import json
from string import punctuation
import gensim
import requests
from gensim.matutils import softcossim
from gensim import corpora
from gensim.models import WordEmbeddingSimilarityIndex
from nltk.corpus import stopwords
from pymystem3 import Mystem
from flask import Flask, jsonify, request
from navec import Navec
from slovnet import NER

app = Flask(__name__)
mystem = Mystem()
stop_words = stopwords.words('russian')


def preprocess_text(text):
    tokens = mystem.lemmatize(text.lower()
                              .replace("#", " ")
                              .replace("!", " ")
                              .replace(".", " ")
                              .replace(".", " ")
                              .replace(",", " ")
                              .replace("  ", " "))
    tokens = [token for token in tokens if token not in stop_words
              and token != " "
              and token.strip() not in punctuation]

    text = " ".join(tokens)

    return text


@app.route("/address", methods=['POST'])
def spb():
    data = request.json
    doc_1 = data.get('text', '')
    navec = Navec.load('/home/runx/navec_news_v1_1B_250K_300d_100q.tar')
    ner = NER.load('/home/runx/slovnet_ner_news_v1.tar')
    ner.navec(navec)
    markup = ner(doc_1)
    join = ""
    for span in markup.spans:
        join = join + " " + markup.text[span.start:span.stop]

    if join != '':
        r = requests.get(
            'https://geocode-maps.yandex.ru/1.x/?apikey=59a55828-0cd7-468b-a92c-c702d2937654&format=json&geocode=' + join)
        data = json.loads(r.text)

        maxCossim = 0
        maxValue = ""
        model = gensim.models.KeyedVectors.load('/home/runx/model/foodsharing2.model')
        for featureMember in data['response']['GeoObjectCollection']['featureMember']:
            value = featureMember['GeoObject']['metaDataProperty']['GeocoderMetaData']['text']
            doc_1 = preprocess_text(value).split()
            doc_2 = preprocess_text(join).split()
            documents = [doc_1, doc_2]
            dictionary = corpora.Dictionary(documents)
            similarity_matrix = model.wv.similarity_matrix(dictionary)
            bow_1 = dictionary.doc2bow(doc_1)
            bow_2 = dictionary.doc2bow(doc_2)
            cossim = softcossim(bow_1, bow_2, similarity_matrix)
            print(cossim, value, featureMember)
            if cossim >= maxCossim:
                maxValue = featureMember['GeoObject']
                maxCossim = cossim

        print(maxValue)
        if maxValue != "" and not (maxValue.get('metaDataProperty') is None or (
                maxValue['metaDataProperty']['GeocoderMetaData'].get('text') is None)) and not (
                maxValue['Point'].get('pos') is None):
            result = {
                "name": maxValue['metaDataProperty']['GeocoderMetaData']['text'],
                "coor": maxValue['Point']['pos']
            }
            return result
    result = {
        "name": "",
        "coor": ""
    }
    return result


@app.route("/softcossim", methods=['POST'])
def softcossimCall():
    data = request.json

    doc_1 = preprocess_text(data.get('doc1', '')).split()
    doc_2 = preprocess_text(data.get('doc2', '')).split()

    documents = [doc_1, doc_2]
    dictionary = corpora.Dictionary(documents)
    model = gensim.models.KeyedVectors.load('/home/runx/model/foodsharing2.model')
    similarity_matrix = model.wv.similarity_matrix(dictionary)
    bow_1 = dictionary.doc2bow(doc_1)
    bow_2 = dictionary.doc2bow(doc_2)
    return jsonify(softcossim(bow_1, bow_2, similarity_matrix))


if __name__ == "__main__":
    app.run(host='0.0.0.0')
