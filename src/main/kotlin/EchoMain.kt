import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton

fun main() {
    val bot = bot {
        token = ""

        dispatch {
            text {
                val button = InlineKeyboardMarkup.create(
                    listOf(InlineKeyboardButton.CallbackData("Ping", "Pong"))
                )
                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = text,
                    replyMarkup = button
                )
            }
            command("start") {
                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = "Hello sweet!"
                )
            }
            callbackQuery("pong") {
                Thread.sleep(5000)
                bot.editMessageText(
                    chatId = ChatId.fromId(callbackQuery.message!!.chat.id),
                    messageId = callbackQuery.message!!.messageId,
                    text = "Хуй",
                )
            }
        }
    }

    bot.startPolling()
}