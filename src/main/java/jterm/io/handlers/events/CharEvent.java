package jterm.io.handlers.events;

/**
 * Functional interface allowing the implementation of a lambda function that processes charEvents.
 */
public interface CharEvent {
	void process(char input);
}
