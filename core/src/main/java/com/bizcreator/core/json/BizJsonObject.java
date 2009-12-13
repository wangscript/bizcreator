package com.bizcreator.core.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class BizJsonObject {

	private JsonObject json;
	
	public BizJsonObject(JsonObject json) {
		this.json = json;
	}
	
	public JsonObject getJson() {
		return json;
	}
	
	public JsonObject getAsJsonObject(String property) {
		JsonElement el = json.get(property);
		if (el != null) return el.getAsJsonObject();
		return null;
	}
	
	public JsonArray getAsJsonArray(String property) {
		JsonElement el = json.get(property);
		if (el != null) return el.getAsJsonArray();
		return null;
	}
	
	public JsonPrimitive getAsJsonPrimitive(String property) {
		JsonElement el = json.get(property);
		if (el != null) return el.getAsJsonPrimitive();
		return null;
	}
	
	public JsonNull getAsJsonNull(String property) {
		JsonElement el = json.get(property);
		if (el != null) return el.getAsJsonNull();
		return null;
	}
	
	public boolean getAsBoolean(String property) {
		JsonPrimitive el = getAsJsonPrimitive(property);
		if (el != null) {
			return el.getAsBoolean();
		}
		return false;
	}
	
	public Number getAsNumber(String property) {
		JsonPrimitive el = getAsJsonPrimitive(property);
		if (el != null) {
			return el.getAsNumber();
		}
		return null;
	}
	
	public String getAsString(String property) {
		JsonPrimitive el = getAsJsonPrimitive(property);
		if (el != null) {
			return el.getAsString();
		}
		return null;
	}
	
	public double getAsDouble(String property) {
		JsonPrimitive el = getAsJsonPrimitive(property);
		if (el != null) {
			return el.getAsDouble();
		}
		return 0;
	}
	
	public BigDecimal getAsBigDecimal(String property) {
		JsonPrimitive el = getAsJsonPrimitive(property);
		if (el != null) {
			return el.getAsBigDecimal();
		}
		return null;
	}
	
	public BigInteger getAsBigInteger(String property) {
		JsonPrimitive el = getAsJsonPrimitive(property);
		if (el != null) {
			return el.getAsBigInteger();
		}
		return null;
	}
	
	public float getAsFloat(String property) {
		JsonPrimitive el = getAsJsonPrimitive(property);
		if (el != null) {
			return el.getAsFloat();
		}
		return 0;
	}
	
	public long getAsLong(String property) {
		JsonPrimitive el = getAsJsonPrimitive(property);
		if (el != null) {
			return el.getAsLong();
		}
		return 0;
	}
	
	public short getAsShort(String property) {
		JsonPrimitive el = getAsJsonPrimitive(property);
		if (el != null) {
			return el.getAsShort();
		}
		return 0;
	}
	
	public int getAsInt(String property) {
		JsonPrimitive el = getAsJsonPrimitive(property);
		if (el != null) {
			return el.getAsInt();
		}
		return 0;
	}
	
	public byte getAsByte(String property) {
		JsonPrimitive el = getAsJsonPrimitive(property);
		if (el != null) {
			return el.getAsByte();
		}
		return 0;
	}
	public char getAsCharacter(String property) {
		JsonPrimitive el = getAsJsonPrimitive(property);
		if (el != null) {
			return el.getAsCharacter();
		}
		return 0;
	}
	
	public Date getAsDate(String property) {
		JsonPrimitive el = getAsJsonPrimitive(property);
		if (el != null && el.getAsLong() > 0) {
			return new Date(el.getAsLong());
		}
		return null;
	}
}
