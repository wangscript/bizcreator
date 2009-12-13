package com.bizcreator.core.json;

import java.lang.reflect.Type;  

import com.google.gson.JsonDeserializationContext;  
import com.google.gson.JsonDeserializer;  
import com.google.gson.JsonElement;  
import com.google.gson.JsonParseException;  
  
public class UtilDateDeserializer implements JsonDeserializer<java.util.Date> {  
    
    public java.util.Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)  
            throws JsonParseException {  
        return new java.util.Date(json.getAsJsonPrimitive().getAsLong());  
    }
}
