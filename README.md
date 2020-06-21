# safefoodbot
Хакатон leadersofdigital.ru 20.06.2020, трек 

Структура проекта:
1) python-server - работа с моделями в python gensim(https://github.com/RaRe-Technologies/gensim) 
и slovnet (https://github.com/natasha/slovnet)
в папке files, файлы для тренировки модели gensim
используемые модели в папке models.
Используемые библиотеки:
flask - для веб сервера
gensim - для поиска категорий в строках через Коэффициент Отиаи
slovnet - для поиска местоположений в строках
ntlk - для получения списка русских stop слов

2) vk-telegram-bot - вк бот (написан на kotlin)
Используемые библиотеки:
kotlin библиотеки 
postgresql для хранения данных в бд postgresql
com.vk.api.sdk - вк апи для java
springframework - фреймворк на который использует код

3) tgBot - телеграмм бот (написан на javascript, nodejs)
Используемые библиотеки:
lodash - библиотека для упрощения работы с javascript
node-telegram-bot-api - для работы с api телеграмма

