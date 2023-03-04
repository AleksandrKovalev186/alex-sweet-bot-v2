import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
import com.github.kotlintelegrambot.logging.LogLevel

fun main() {

    val bot = bot {
        //https://github.com/kotlin-telegram-bot/kotlin-telegram-bot/blob/main/docs/logging.md
        logLevel = LogLevel.None
        //todo читать из файла
        token = ""

        dispatch {


            //TODO в отдельный метод
            text {
                if ((text == humanResourceContacts
                            || text == accountingContacts
                            || text == paymentContacts
                            || text == "/start").not()
                ) {
                    bot.sendMessage(
                        chatId = ChatId.fromId(message.chat.id),
                        text = "Для получения информации введите команду /start",
                    )
                }

            }


            command("start") {
                val keyboardMarkup = KeyboardReplyMarkup(
                    keyboard = generateUsersButton(),
                    resizeKeyboard = true
                )
                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    //todo читать из файла
                    text = """Добрый день! вы можете узнать:
                         $humanResourceContacts, 
                         $accountingContacts,
                         $paymentContacts
                    """.trimMargin(),
                    replyMarkup = keyboardMarkup
                )

                text(humanResourceContacts) {
                    bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Вот контакты")
                }
                text(accountingContacts) {
                    bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Вот справки")
                }
                text(paymentContacts) {
                    bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Ответы по заработной плате")
                }
            }
        }
    }


    bot.startPolling()
}


const val humanResourceContacts = "Контакты отдела кадров"
const val accountingContacts = "Заказать справку в бухгалтерии"
const val paymentContacts = "Задать вопросы по заработной плате"


fun generateUsersButton(): List<List<KeyboardButton>> {
    return listOf(
        listOf(KeyboardButton(humanResourceContacts), KeyboardButton(accountingContacts)),
        listOf(KeyboardButton(paymentContacts))
    )
}
