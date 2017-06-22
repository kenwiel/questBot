package space.funin.questBot;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public class TypeTokens {
    public static final Type QUEST = new TypeToken<Quest>(){}.getType();
    public static final Type STRING = new TypeToken<String>(){}.getType();
    public static final Type LIST_STRING = new TypeToken<List<String>>(){}.getType();
}
