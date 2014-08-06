package com.nspl.inmobisample;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.nspl.inmobisample.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;


public class SavedValues {	
	protected Context context;
	protected Resources resources;
	protected SharedPreferences prefs;
	protected static SavedValues instance;
	public static final String SHOWTERPREFS = "samplePreferences";

	protected SavedValues() {
	}

	protected SavedValues(Context context) {
		this.context = context;
		this.resources = this.context.getResources();
		this.prefs = this.context.getSharedPreferences(SHOWTERPREFS, Activity.MODE_PRIVATE);

	}

	public static SavedValues Instance(Context context) {
		if (instance == null) {
			instance = new SavedValues(context);
		}
		return instance;
	}

	public String loadKeyValueDefaultResource(String key, int defaultResource) {
		String defaultValue = resources.getString(defaultResource);
		return  prefs.getString(key, defaultValue);
	}

	public String loadKeyValue(String key, String defaultValue) {		
		return prefs.getString(key, defaultValue);		 
	}

	public boolean loadKeyValue(String key, boolean defaultValue) {
		return prefs.getBoolean(key, defaultValue);
	}

	public int loadKeyValue(String key, int defaultValue) {
		return prefs.getInt(key, defaultValue);
	}

	public long loadKeyValue(String key, long defaultValue) {
		return prefs.getLong(key, defaultValue);
	}


	public void saveKeyValue(String key, String value) {
		SharedPreferences.Editor editor = prefs.edit();

		editor.putString(key, value);
		boolean committed = editor.commit();
		if (!committed) {
			Toast toast = Toast.makeText(context, resources.getString(R.string.unableToSave), Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	public void saveKeyValue(String key, int value) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(key, value);
		boolean committed = editor.commit();
		if (!committed) {
			Toast toast = Toast.makeText(context, resources.getString(R.string.unableToSave), Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	public void saveKeyValue(String key, long value) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong(key, value);
		boolean committed = editor.commit();
		if (!committed) {
			Toast toast = Toast.makeText(context, resources.getString(R.string.unableToSave), Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	public void saveKeyValue(String key, boolean value) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(key, value);
		boolean committed = editor.commit();
		if (!committed) {
			Toast toast = Toast.makeText(context, resources.getString(R.string.unableToSave), Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	public void removeKey(String key){
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(key);		
		boolean committed = editor.commit();
		if (!committed) {
			Toast toast = Toast.makeText(context, resources.getString(R.string.unableToSave), Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}
