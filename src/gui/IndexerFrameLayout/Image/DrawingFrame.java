package gui.IndexerFrameLayout.Image;

import gui.BatchState;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



@SuppressWarnings("serial")
public class DrawingFrame extends JPanel implements Serializable
{
	private DrawingComponent component;
	private String url;
	private BatchState batchstate;
	
	public DrawingComponent getDrawingComponent()
	{
		return component;
	}
	
	public DrawingFrame(String s, BatchState batchState) 
	{
		batchstate = batchState;
		url = s;
		component = new DrawingComponent(s, batchState);
		add(component);
	}
	
	private WindowAdapter windowAdapter = new WindowAdapter() 
	{

		@Override
		public void windowActivated(WindowEvent e) 
		{
			return;
		}

		@Override
		public void windowClosed(WindowEvent e) 
		{
			return;
		}

		@Override
		public void windowClosing(WindowEvent e) 
		{
			return;
		}

		@Override
		public void windowDeactivated(WindowEvent e) 
		{
			return;
		}

		@Override
		public void windowDeiconified(WindowEvent e) 
		{
			return;
		}

		@Override
		public void windowGainedFocus(WindowEvent e) 
		{
			component.requestFocusInWindow();
		}

		@Override
		public void windowIconified(WindowEvent e) 
		{
			return;
		}

		@Override
		public void windowLostFocus(WindowEvent e) 
		{
			return;
		}

		@Override
		public void windowOpened(WindowEvent e) 
		{
			return;
		}

		@Override
		public void windowStateChanged(WindowEvent e) 
		{
			return;
		}
		
	};
	
}