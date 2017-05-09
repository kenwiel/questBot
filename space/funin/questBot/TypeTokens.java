package space.funin.questBot;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;

public class TypeTokens {
    public static final Type QUEST = new TypeToken<Quest>(){}.getType();
    public static final Type STRING = new TypeToken<String>(){}.getType();
}
