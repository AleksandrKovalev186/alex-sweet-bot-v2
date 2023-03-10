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
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime

class BotFactory {

    private val humanResourceContacts = "Контакты отдела кадров"
    private val accountingContacts = "Заказать справку в бухгалтерии"
    private val paymentContacts = "Задать вопросы по заработной плате"
    private val logFile = File("logs.txt")
    private var localDateTime: LocalDateTime = LocalDateTime.now();


    val bot = bot {
        //https://github.com/kotlin-telegram-bot/kotlin-telegram-bot/blob/main/docs/logging.md
        logLevel = LogLevel.Network.Body

        val content = Files.readString(Paths.get("token.txt"), Charsets.UTF_8)
        token = content

        dispatch {

            logFile.writeText("QET BOT IS LAUNCHED at $localDateTime", Charsets.UTF_8)

            text {
                if (listOf(
                        humanResourceContacts,
                        accountingContacts,
                        paymentContacts,
                        "/start"
                    ).contains(text).not()
                ) {

                    val startMessage = bot.sendMessage(
                        chatId = ChatId.fromId(message.chat.id),
                        text = "Для получения информации введите команду /start"

                    )
                    Files.write(
                        logFile.toPath(),
                        "\n send /start info $startMessage".toByteArray(),
                        StandardOpenOption.APPEND
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
                    text = """Добрый день! вы можете узнать:
                              $humanResourceContacts, 
                              $accountingContacts,
                              $paymentContacts
                    """.trimMargin(),
                    replyMarkup = keyboardMarkup
                )
            }
            text(humanResourceContacts) {

                val humanResourceMessage =
                    bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Вот контакты")

                Files.write(
                    logFile.toPath(),
                    "\n send $humanResourceContacts $humanResourceMessage".toByteArray(),
                    StandardOpenOption.APPEND
                )

            }
            text(paymentContacts) {
                val paymentContactMessage = bot.sendContact(
                    chatId = ChatId.fromId(message.chat.id),
                    "+79218671553",
                    "Имя",
                    "Фамилия"
                )
                Files.write(
                    logFile.toPath(),
                    "\n send $paymentContacts $paymentContactMessage".toByteArray(),
                    StandardOpenOption.APPEND
                )

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
        }

    }

    private fun generateUsersButton(): List<List<KeyboardButton>> {
        return listOf(
            listOf(KeyboardButton(humanResourceContacts), KeyboardButton(accountingContacts)),
            listOf(KeyboardButton(paymentContacts))
        )
    }

}



