package com.example.walkietalkie.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.walkietalkie.viewmodel.MessagesViewModel
import com.example.walkietalkie.databinding.MessagesFragmentBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkietalkie.MainActivity

class MessagesFragment : Fragment() {

    private lateinit var viewModel: MessagesViewModel
    private val messagesAdapter = MessagesListAdapter()
    private var binding: MessagesFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MessagesFragmentBinding.inflate(LayoutInflater.from(container?.context), container, false)
        viewModel = ViewModelProvider(this)[MessagesViewModel::class.java]

        (activity as MainActivity).adapter = messagesAdapter
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.messagesList?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = messagesAdapter
        }

        viewModel.refresh()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            messages?.let {
                binding?.messagesList?.visibility = View.VISIBLE
                binding?.loadingView?.visibility = View.GONE
                messagesAdapter.updateMessages(it)
            }

            viewModel.messagesLoadError.observe(viewLifecycleOwner) { isError ->
                if (isError == null) {
                    binding?.listError?.visibility = View.GONE
                } else {
                    binding?.listError?.visibility = View.VISIBLE
                    binding?.messagesList?.visibility = View.GONE
                    binding?.loadingView?.visibility = View.GONE
                }
            }
        }
    }
}