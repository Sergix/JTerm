package main.java.jterm;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class EventHandler implements NativeKeyListener {

	public static String keyDown = null;
	
	/*
	* init() void
	* 
	* Initializes all implemented event handlers
	* from JNativeHook.
	*/
	public static void init()
	{
		  
		// Get the logger for "org.jnativehook" and set the level to off.
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
		Handler[] handlers = Logger.getLogger("").getHandlers();
		for (int i = 0; i < handlers.length; i++) {
			handlers[i].setLevel(Level.OFF);
			
		}
		  
		// Attempt to register the key listener
		try {
			GlobalScreen.registerNativeHook();

		} catch (NativeHookException e) {
		    e.printStackTrace();
			System.exit(1);
			
		} 

		// Add the key input handler
		GlobalScreen.addNativeKeyListener(new EventHandler());
		  
	}
	
	/*
	* nativeKeyPressed() void
	* 
	* Overrides the built-in library function
	* that detects key input.
	*
	* NativeKeyEvent e - key event object
	*/
	public void nativeKeyPressed(NativeKeyEvent e) {
		
		keyDown = NativeKeyEvent.getKeyText(e.getKeyCode());
		
		/* If the key pressed is the CTRL key
		if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
			// TODO
			// Auto-completion of executable filenames
			File dir = new File(JTerm.currentDirectory);
			String[] list = dir.list();
			for (int i = 0; i < list.length; i++)
				if (list[i].startsWith(JTerm.commandChars) && !JTerm.commandChars.equals(""))
				{
					String rest = list[i].substring(JTerm.commandChars.length(), list[i].length());
					JTerm.commandChars += rest;
					System.out.print(rest + "\n");
					ArrayList<String> options = new ArrayList<String>();
					options.add(JTerm.commandChars);
					JTerm.Parse(options);
					return;
					
				}
			
		}
		  
		if (e.getKeyCode() != NativeKeyEvent.VC_CONTROL)
			JTerm.commandChars += NativeKeyEvent.getKeyText(e.getKeyCode()).toLowerCase();*/
		  
	}

	/*
	* nativeKeyReleased() void
	* 
	* Overrides the built-in library function
	* that detects key release.
	*
	* NativeKeyEvent e - key event object
	*/
	public void nativeKeyReleased(NativeKeyEvent e) { 
		
		keyDown = null;
		
	}
	
	/*
	* nativeKeyTyped() void
	* 
	* Overrides the built-in library function
	* that detects key typing.
	*
	* NativeKeyEvent e - key event object
	*/
	public void nativeKeyTyped(NativeKeyEvent e) { }

}
