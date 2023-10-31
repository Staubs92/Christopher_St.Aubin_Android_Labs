package algonquin.cst2335.stau0055;


import algonquin.cst2335.stau0055.data.ChatRoomModel;
import algonquin.cst2335.stau0055.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.stau0055.databinding.SentMessageBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatRoom extends AppCompatActivity {

   ActivityChatRoomBinding binding;
    ArrayList<String> messages;
    ChatRoomModel chatModel ;
    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatRoomModel.class);
        if(messages == null)
        {
            chatModel.messages.postValue( messages = new ArrayList<String>());
        }

        binding.sendButton.setOnClickListener(clk -> {
            messages.add(binding.editTextArray.getText().toString());
            myAdapter.notifyItemInserted(messages.size()-1);

            binding.editTextArray.setText("");
        });

        class MyRowHolder extends RecyclerView.ViewHolder {
            TextView messageText;
            TextView timeText;
            public MyRowHolder(@NonNull View itemView) {
                super(itemView);

                messageText = itemView.findViewById(R.id.message);
                timeText = itemView.findViewById(R.id.time);
            }
        }

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                String obj = messages.get(position);
                holder.messageText.setText(obj);
                holder.timeText.setText("");
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position){
                return 0;
            }

        });
}}