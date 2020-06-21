from pprint import pprint as print
from gensim.models.fasttext import FastText as FT_gensim
from gensim.test.utils import datapath

# тренировка модели

corpus_file = datapath('/home/runx/fudsharingProcessed.cor')
model = FT_gensim(size=100)

model.build_vocab(corpus_file=corpus_file)
model.train(
    corpus_file=corpus_file, epochs=model.epochs,
    total_examples=model.corpus_count, total_words=model.corpus_total_words
)

print(model)
model.save("/home/runx/model/foodsharing2.model", separately=[])
