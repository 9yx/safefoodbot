import TelegramBot from 'node-telegram-bot-api'
import config from './config'
import _ from 'lodash'

// replace the value below with the Telegram token you receive from @BotFather
const token = config.token;
const bot = new TelegramBot(token, {polling: true});

// Matches "/echo [whatever]"
bot.onText(/\/echo (.+)/, (msg, match) => {
    // 'msg' is the received Message from Telegram
    // 'match' is the result of executing the regexp above on the text content
    // of the message

    const chatId = msg.chat.id;
    const resp = match[1]; // the captured "whatever"

    // send back the matched "whatever" to the chat
    bot.sendMessage(chatId, resp);
});

// Listen for any kind of message. There are different kinds of
// messages.

var options = {
    reply_markup: JSON.stringify({
        inline_keyboard: [
            [{text: 'Картошка', callback_data: '1'}],
            [{text: 'Чай', callback_data: '2'}],
            [{text: 'Хлеб', callback_data: '3'}]
        ]
    })
};


bot.onText(/\/start/, (message) => {
    bot.sendMessage(message.from.id, "Отправьте или транслируйте местоположение");
});

bot.on("location", (message) => {
    bot.sendMessage(message.from.id, "Укажите интересующую вас категорию", options);
});


bot.on('edited_message', (message) => {
    bot.sendMessage(message.from.id, `Широта:${message.location.latitude} и Долгота:${message.location.longitude}`);
});

bot.on('callback_query', (props) => {
    const opts = {
        chat_id: props.message.chat.id,
        message_id: props.message.message_id,
    };
    const text = props.message.text;
    const array = props.message.reply_markup.inline_keyboard;

    const buttons = [array[0], array[1], array[2]].find((item) => {
        return item[0].callback_data === props.data
    });

    const button = buttons[0];

    bot.editMessageText(text, opts);

    bot.sendMessage(props.from.id, `Вы указали ${button.text}, я попробую Вам помочь с подбором`);
});