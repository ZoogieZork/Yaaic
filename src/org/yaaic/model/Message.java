/*
Yaaic - Yet Another Android IRC Client

Copyright 2009-2010 Sebastian Kaspari

This file is part of Yaaic.

Yaaic is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Yaaic is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Yaaic.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.yaaic.model;

import java.util.Date;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.widget.TextView;

/**
 * A channel or server message
 * 
 * @author Sebastian Kaspari <sebastian@yaaic.org>
 */
public class Message {
	public static final int COLOR_GREEN  = 0xFF458509;
	public static final int COLOR_RED    = 0xFFcc0000;
	public static final int COLOR_BLUE   = 0xFF729fcf;
	public static final int COLOR_YELLOW = 0xFFbe9b01;
	public static final int COLOR_GREY   = 0xFFaaaaaa;
	
	private int icon = -1;
	private String text;
	private SpannableString canvas;
	private int color = -1;
	private long timestamp;
	
	/**
	 * Create a new message without an icon
	 * 
	 * @param text
	 */
	public Message(String text)
	{
		this.text = text;
		this.timestamp = new Date().getTime();
	}
	
	/**
	 * Set the message's icon
	 */
	public void setIcon(int icon)
	{
		this.icon = icon;
	}
	
	/**
	 * Get the message's icon
	 * 
	 * @return
	 */
	public int getIcon()
	{
		return icon;
	}
	
	/**
	 * Get the text of this message
	 * 
	 * @return
	 */
	public String getText()
	{
		return text;
	}
	
	/**
	 * Set the color of this message
	 */
	public void setColor(int color)
	{
		this.color = color;
	}
	
	/**
	 * Render message as spannable string
	 * 
	 * @return
	 */
	public SpannableString render(Context context)
	{
		Settings settings = new Settings(context);
		
		if (canvas == null) {
			String prefix = icon != -1 && settings.showIcons() ? "  " : "";
			String timestamp = settings.showTimestamp() ? Message.generateTimestamp(this.timestamp, settings.use24hFormat()) : "";
			
			canvas = new SpannableString(prefix + timestamp + text);
			
			if (icon != -1 && settings.showIcons()) {
				Drawable drawable = context.getResources().getDrawable(icon);
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
				canvas.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if (color != -1 && settings.showColors()) {
				canvas.setSpan(new ForegroundColorSpan(color), 0, canvas.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		
		return canvas;
	}
	
	/**
	 * Render message as text view
	 * 
	 * @param context
	 * @return
	 */
	public TextView renderTextView(Context context)
	{
		// XXX: We should not read settings here ALWAYS for EVERY textview
		Settings settings = new Settings(context);
		
		TextView canvas = new TextView(context);
		
		canvas.setText(this.render(context));
		canvas.setTextSize(settings.getFontSize());
		canvas.setTypeface(Typeface.MONOSPACE);
		canvas.setTextColor(0xffeeeeee);
		
		return canvas;
	}
	
	/**
	 * Generate a timestamp
	 * 
	 * @param use24hFormat
	 * @return
	 */
	public static String generateTimestamp(long timestamp, boolean use24hFormat)
	{
		Date date = new Date(timestamp);
		
		int hours = date.getHours();
		int minutes = date.getMinutes();

		if (!use24hFormat) {
			hours = Math.abs(12 - hours);
			if (hours == 12) {
				hours = 0;
			}
		}
		
		return "[" + (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes) + "] ";
	}
}
