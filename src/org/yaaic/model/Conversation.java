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

import java.util.LinkedList;

import org.yaaic.adapter.MessageListAdapter;

/**
 * Base class for conversations
 * 
 * A conversation can be a channel, a query or server messages
 * 
 * @author Sebastian Kaspari <sebastian@yaaic.org>
 */
public abstract class Conversation
{
	public static final int TYPE_CHANNEL = 1;
	public static final int TYPE_QUERY   = 2;
	public static final int TYPE_SERVER  = 3;
	
	public static final int STATUS_DEFAULT   = 1;
	public static final int STATUS_SELECTED  = 2;
	public static final int STATUS_MESSAGE   = 3;
	public static final int STATUS_HIGHLIGHT = 4;
	
	public static final int HISTORY_SIZE = 30;
	
	private LinkedList<Message> buffer;
	private LinkedList<Message> history;
	private String name;
	private MessageListAdapter adapter;
	private int status = 1;
	
	/**
	 * Get the type of conversation (channel, query, ..)
	 * 
	 * @return See the constants: Conversation.TYPE_*
	 */
	public abstract int getType();
	
	/**
	 * Create a new conversation with the given name
	 * 
	 * @param name The name of the conversation (channel, user)
	 */
	public Conversation(String name)
	{
		this.buffer = new LinkedList<Message>();
		this.history = new LinkedList<Message>();
		this.name = name.toLowerCase();
	}
	
	/**
	 * Get name of the conversation (channel, user)
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Add a message to the channel
	 */
	public void addMessage(Message message)
	{
		buffer.add(0, message);
		history.add(message);
		
		if (history.size() > HISTORY_SIZE) {
			history.remove(0);
		}
	}
	
	/**
	 * Get the history
	 */
	public LinkedList<Message> getHistory()
	{
		return history;
	}
	
	/**
	 * Get message of the history at the given position
	 * 
	 * @param position
	 * @return The message at the given position
	 */
	public Message getHistoryMessage(int position)
	{
		return history.get(position);
	}
	
	/**
	 * Get last buffered message
	 * 
	 * @return
	 */
	public Message pollBufferedMessage()
	{
		Message message = buffer.get(buffer.size() - 1);
		buffer.remove(buffer.size() - 1);
		return message;
	}
	
	/**
	 * Get the buffer
	 * 
	 * @return
	 */
	public LinkedList<Message> getBuffer()
	{
		return buffer;
	}
	
	/**
	 * Does the channel have buffered messages?
	 */
	public boolean hasBufferedMessages()
	{
		return buffer.size() > 0;
	}
	
	/**
	 * Clear the message buffer
	 */
	public void clearBuffer()
	{
		buffer.clear();
	}
	
	/**
	 * Store the adapter of this conversation
	 */
	public void setMessageListAdapter(MessageListAdapter adapter)
	{
		this.adapter = adapter;
	}
	
	/**
	 * Get the MessageList Adapter of this conversation if known
	 */
	public MessageListAdapter getMessageListAdapter()
	{
		return adapter;
	}
	
	/**
	 * Set status of conversation
	 * 
	 * @param status
	 */
	public void setStatus(int status)
	{
		// Selected status can only be changed by deselecting
		if (this.status == STATUS_SELECTED && status != STATUS_DEFAULT) {
			return;
		}
		
		// Highlight status can only be changed by selecting
		if (this.status == STATUS_HIGHLIGHT && status != STATUS_SELECTED) {
			return;
		}
		
		this.status = status;
	}
	
	/**
	 * Get status of conversation
	 * 
	 * @return
	 */
	public int getStatus()
	{
		return status;
	}
}
