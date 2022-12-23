package com.example.walkietalkie.view

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.walkietalkie.R
import com.example.walkietalkie.databinding.ItemMessageBinding
import com.example.walkietalkie.model.Message
import java.text.SimpleDateFormat
import java.util.*

class MessagesListAdapter : RecyclerView.Adapter<MessagesListAdapter.MessageViewHolder>() {

    private val messages = arrayListOf<Message>()
    private lateinit var allMessages: MutableList<Message>
    lateinit var mediaPlayer: MediaPlayer
    private var isAdmin = true

    fun updateMessages(newMessages: MutableList<Message>) {
        messages.clear()
        messages.addAll(newMessages)
        allMessages = newMessages
        notifyDataSetChanged()
    }

    fun searchMessage(query: String) {
        messages.clear()

        for (message in allMessages) {
            if (message?.userNameTo?.contains(query) == true || message?.userNameFrom?.contains(
                    query
                ) == true
            ) {
                messages.add(message)
            }
        }

        notifyDataSetChanged()
    }

    fun updateMessagesForCurrentUser(currentUser: String?) {
        messages.clear()

        for (message in allMessages) {
            if (message.userNameTo.equals(currentUser)) {
                messages.add(message)
            }
        }

        isAdmin = if (currentUser.equals(ADMIN_USER)) {
            messages.addAll(allMessages)
            true
        } else {
            false
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        mediaPlayer = MediaPlayer()

        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position], isAdmin, mediaPlayer)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class MessageViewHolder(binding: ItemMessageBinding) : ViewHolder(binding.root) {

        private val messageFrom = binding.from
        private val messageTo = binding.sentTo
        private val sentOn = binding.sentOn
        private val sentToIcon = binding.sentToIcon
        private val play = binding.play

        fun bind(message: Message, isAdmin: Boolean, mediaPlayer: MediaPlayer) {
            messageFrom.text = message.userNameFrom
            messageTo.text = message.userNameTo
            sentOn.text = getDateTime(message.sentOn)

            if (!isAdmin) {
                messageTo.visibility = View.GONE
                sentToIcon.visibility = View.GONE
            } else {
                messageTo.visibility = View.VISIBLE
                sentToIcon.visibility = View.VISIBLE
            }

            play.setOnClickListener {
                if (mediaPlayer.isPlaying) {
                    play.setImageResource(R.drawable.ic_play)
                    pauseMessage(mediaPlayer)
                } else {
                    play.setImageResource(R.drawable.ic_pause)
                    playMessage(mediaPlayer)
                }
            }
        }

        private fun playMessage(mediaPlayer: MediaPlayer) {
            var mediaUrl =
                "https://github.com/fluentstream/walkie_talkie_backend/blob/main/sample_recording.mp3?raw=true"

            try {
                mediaPlayer.setDataSource(mediaUrl)

                mediaPlayer.prepare()
                mediaPlayer.start()
            } catch (e: Exception) {
                e.stackTrace
            }

        }

        private fun pauseMessage(mediaPlayer: MediaPlayer) {
            mediaPlayer.stop()
            mediaPlayer.reset()
        }

        private fun getDateTime(s: String?): String? {
            val sdf = SimpleDateFormat("E MM/dd/yyyy h:m a", Locale.US)
            val netDate = s?.toLong()?.times(1000)?.let { Date(it) }
            return netDate?.let { sdf.format(it) }
        }
    }

    companion object {
        const val ADMIN_USER: String = "Admin"
    }
}