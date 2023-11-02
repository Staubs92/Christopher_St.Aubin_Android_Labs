package algonquin.cst2335.stau0055;


import algonquin.cst2335.stau0055.data.ChatRoomModel;
import algonquin.cst2335.stau0055.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.stau0055.databinding.ReceiveMessageBinding;
import algonquin.cst2335.stau0055.databinding.SentMessageBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages;
    ChatRoomModel chatModel ;
    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        chatModel = new ViewModelProvider(this).get(ChatRoomModel.class);

        messages = chatModel.messages.getValue();
        if(messages == null)
            chatModel.messages.postValue( messages = new ArrayList<ChatMessage>());

        setContentView(binding.getRoot());

        binding.sendButton.setOnClickListener(clk -> {

            String editTextArray = binding.editTextArray.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(editTextArray, currentDateandTime, true);
            messages.add(chatMessage);

            myAdapter.notifyItemInserted(messages.size()-1);
            binding.editTextArray.setText("");
        });

        binding.receiveButton.setOnClickListener(clk -> {
            String editTextArray = binding.editTextArray.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(editTextArray, currentDateandTime, false);
            messages.add(chatMessage);

            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.editTextArray.setText("");
        });

        class MyRowHolder extends RecyclerView.ViewHolder {
            TextView messageText;
            TextView timeText;
            ImageView img;
            public MyRowHolder(@NonNull View theRootConstraintLayout) {
                super(theRootConstraintLayout);

                messageText = theRootConstraintLayout.findViewById(R.id.message);
                timeText = theRootConstraintLayout.findViewById(R.id.time);


            }
        }

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == 0){
                SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder(binding.getRoot());
            }

            else {
                ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder(binding.getRoot());
            } }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
               ChatMessage chatMessage = messages.get(position);
                holder.messageText.setText(chatMessage.getMessage());
                holder.timeText.setText(chatMessage.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {

                ChatMessage message = messages.get(position);
                return message.isSentButton() ? 0 : 1;
            }

        });
    }}