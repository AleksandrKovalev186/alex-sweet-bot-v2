import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
import com.github.kotlintelegrambot.logging.LogLevel
import java.nio.file.Files
import java.nio.file.Paths

class BotFactory {

    private val humanResourceContacts = "Контакты отдела кадров"
    private val accountingContacts = "Заказать справку в бухгалтерии"
    private val paymentContacts = "Задать вопросы по заработной плате"

    val bot = bot {

        //todo писать логи в файл
        //https://github.com/kotlin-telegram-bot/kotlin-telegram-bot/blob/main/docs/logging.md
        logLevel = LogLevel.None

        //val filePath = "/app/logs/january.log"

        val content = Files.readString(Paths.get("src/main/kotlin/token.txt"), Charsets.UTF_8)
        token = content

        dispatch {
            println("QET BOT IS LAUNCHED")

            text {
                if (listOf(
                        humanResourceContacts,
                        accountingContacts,
                        paymentContacts,
                        "/start"
                    ).contains(text).not()
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
                    val button = InlineKeyboardMarkup.create(
                        listOf(
                            InlineKeyboardButton.CallbackData("Справка 1", "Справка 1"),
                            InlineKeyboardButton.CallbackData("Справка 2", "Справка 2")
                        )
                    )
                    bot.sendMessage(
                        chatId = ChatId.fromId(message.chat.id),
                        text = "Выберите необходимую справку",
                        replyMarkup = button
                    )

                    callbackQuery("Справка 1") {
                        bot.sendMessage(
                            chatId = ChatId.fromId(callbackQuery.message!!.chat.id),
                            text = "Информация по первой справке",
                        )
                    }

                    callbackQuery("Справка 2") {
                        bot.sendMessage(
                            chatId = ChatId.fromId(callbackQuery.message!!.chat.id),
                            text = "Информация по второй справке",
                        )
                    }
                }
                text(paymentContacts) {
                    bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Ответы по заработной плате")

                }
            }
        }

    }

    private fun generateUsersButton(): List<List<KeyboardButton>> {
        return listOf(
            listOf(KeyboardButton(humanResourceContacts), KeyboardButton(accountingContacts)),
            listOf(KeyboardButton(paymentContacts))
        )
    }

}