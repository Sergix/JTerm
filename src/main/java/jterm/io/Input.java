package jterm.io;

import java.io.IOException;

//This interface is so that I can replace RawConsoleInput with a class that streams over chars from the GUI
public interface Input{
	int read(boolean wait) throws IOException;
}
