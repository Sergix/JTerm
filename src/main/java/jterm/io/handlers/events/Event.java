package jterm.io.handlers.events;

/**
 * Functional interface allowing the implementation of an event.
 * This and CharEvent exist so that another module can implement their own versions
 * of certain event handlers, in the event that the provided implementation does something
 * undesirable or inconvenient.
 *
 * @see CharEvent
 */
public interface Event {
	void process();
}
