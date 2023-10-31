package algonquin.cst2335.stau0055.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ChatRoomModel extends ViewModel {
    public MutableLiveData<ArrayList<String>> messages = new MutableLiveData< >();
}